package net.Indyuce.moarbows.bow;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.LinearValue;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;

public class Snow_Bow extends MoarBow {
	public Snow_Bow() {
		super(new String[] { "Shoots a few snowballs.", "The number depends on", "the bow pull force." }, 0, "snow_shovel", new String[] { "SNOW_BLOCK,SNOW_BLOCK,SNOW_BLOCK", "SNOW_BLOCK,BOW,SNOW_BLOCK", "SNOW_BLOCK,SNOW_BLOCK,SNOW_BLOCK" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(2, 0)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		event.setCancelled(true);
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				if (ti++ > 20 * event.getForce())
					cancel();

				Location loc = data.getSender().getEyeLocation().clone();
				loc.getWorld().spawnParticle(Particle.SNOWBALL, loc, 6, .2, .2, .2, 0);
				data.getSender().getWorld().playSound(data.getSender().getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1.5f);
				Snowball snowball = data.getSender().launchProjectile(Snowball.class);
				loc.setPitch(loc.getPitch() + random.nextInt(3) - 1);
				loc.setYaw(loc.getYaw() + random.nextInt(3) - 1);
				snowball.setVelocity(loc.getDirection().multiply(3.3 * event.getForce()));
			}
		}.runTaskTimer(MoarBows.plugin, 0, 2);
		return false;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
	}

	@Override
	public void whenLand(ArrowData data) {
	}
}