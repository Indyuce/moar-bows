package net.Indyuce.moarbows.bow;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Spartan_Bow extends MoarBow {
	public Spartan_Bow() {
		super(new String[] { "Summons a flurry of arrows from", "the sky when hitting a target." }, 0, 25, "redstone:180,180,180", new String[] { "BOW,EMERALD,BOW", "EMERALD,BOW,EMERALD", "BOW,EMERALD,BOW" });

		addModifier(new BowModifier("duration", 1.5));
	}

	@Override
	public void hit(EntityDamageByEntityEvent event, Arrow arrow, Entity entity, Player player) {
		land(player, arrow);
	}

	@Override
	public void land(Player player, Arrow arrow) {
		arrow.remove();
		final Location loc1 = arrow.getLocation().clone();
		double randomOffset = Math.PI * 4 * (random.nextDouble() - .5);
		Location sky = arrow.getLocation().clone().add(Math.cos(randomOffset) * 6, 13, Math.sin(randomOffset) * 6);
		final double duration = getValue("duration");
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				if ((ti += 3d / 20d) > duration)
					cancel();

				sky.getWorld().spawnParticle(Particle.SMOKE_LARGE, sky, 0);
				Arrow arrow1 = (Arrow) sky.getWorld().spawnEntity(sky, EntityType.ARROW);
				arrow1.setColor(Color.BLACK);
				if (player != null) {
					EntityShootBowEvent event = new EntityShootBowEvent(player, new ItemStack(Material.BOW), arrow1, 1);
					MoarBows.plugin.getServer().getPluginManager().callEvent(event);
					if (event.isCancelled()) {
						arrow1.remove();
						return;
					}
				}
				arrow1.setVelocity(loc1.clone().add(8 * (random.nextDouble() - .5), 0, 8 * (random.nextDouble() - .5)).toVector().subtract(sky.toVector()).normalize());
			}
		}.runTaskTimer(MoarBows.plugin, 0, 3);
	}
}
