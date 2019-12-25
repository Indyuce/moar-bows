package net.Indyuce.moarbows.bow;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.util.LinearValue;

public class Spartan_Bow extends MoarBow {
	public Spartan_Bow() {
		super(new String[] { "Summons a flurry of arrows from", "the sky when hitting a target." }, 0,  "redstone:180,180,180", new String[] { "BOW,EMERALD,BOW", "EMERALD,BOW,EMERALD", "BOW,EMERALD,BOW" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(25, -3, 10, 25)), new DoubleModifier("duration", new LinearValue(1.5, .5)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		return true;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
		whenLand(data);
	}

	@Override
	public void whenLand(ArrowData data) {
		data.getArrow().remove();

		final Location loc1 = data.getArrow().getLocation().clone();
		double randomOffset = Math.PI * 4 * (random.nextDouble() - .5);
		Location sky = data.getArrow().getLocation().clone().add(Math.cos(randomOffset) * 6, 13, Math.sin(randomOffset) * 6);
		final double duration = data.getDouble("duration");
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				if ((ti += 3d / 20d) > duration)
					cancel();

				sky.getWorld().spawnParticle(Particle.SMOKE_LARGE, sky, 0);
				Arrow arrow1 = (Arrow) sky.getWorld().spawnEntity(sky, EntityType.ARROW);
				// arrow1.setColor(Color.BLACK);

//				EntityShootBowEvent event = new EntityShootBowEvent(data.getSender(), new ItemStack(Material.BOW), arrow1, 1);
//				MoarBows.plugin.getServer().getPluginManager().callEvent(event);
//				if (event.isCancelled()) {
//					arrow1.remove();
//					return;
//				}

				arrow1.setVelocity(loc1.clone().add(8 * (random.nextDouble() - .5), 0, 8 * (random.nextDouble() - .5)).toVector().subtract(sky.toVector()).normalize());
			}
		}.runTaskTimer(MoarBows.plugin, 0, 3);
	}
}
