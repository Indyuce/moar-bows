package net.Indyuce.moarbows.bow;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.Eff;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Spartan_Bow extends MoarBow {
	public Spartan_Bow() {
		super(new String[] { "Summons a flurry of arrows from", "the sky when hitting a target." }, 0, 25, "redstone:180,180,180", new String[] { "BOW,EMERALD,BOW", "EMERALD,BOW,EMERALD", "BOW,EMERALD,BOW" });

		addModifier(new BowModifier("duration", 1.5));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(t, a);
	}

	@Override
	public void land(Player p, Arrow a) {
		a.remove();
		Random r = new Random();
		final Location loc1 = a.getLocation().clone();
		double random = Math.PI * 4 * (r.nextDouble() - .5);
		Location sky = a.getLocation().clone().add(Math.cos(random) * 6, 13, Math.sin(random) * 6);
		final double duration = MoarBows.getLanguage().getBows().getDouble("SPARTAN_BOW.duration");
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				ti += 3d / 20d;
				Eff.SMOKE_LARGE.display(0, 0, 0, 0, 1, sky, 100);
				Location loc2 = loc1.clone().add(8 * (r.nextDouble() - .5), 0, 8 * (r.nextDouble() - .5));
				Vector v = loc2.toVector().subtract(sky.toVector()).normalize();
				Arrow a1 = (Arrow) sky.getWorld().spawnEntity(sky, EntityType.ARROW);
				if (p != null) {
					EntityShootBowEvent event = new EntityShootBowEvent(p, new ItemStack(Material.BOW), a, 1);
					MoarBows.plugin.getServer().getPluginManager().callEvent(event);
				}
				a1.setVelocity(v);

				if (ti > duration)
					cancel();
			}
		}.runTaskTimer(MoarBows.plugin, 0, 3);
	}
}
