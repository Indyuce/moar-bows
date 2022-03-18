package net.Indyuce.moarbows.bow.list;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.bow.ArrowData;
import net.Indyuce.moarbows.bow.MoarBow;
import net.Indyuce.moarbows.bow.modifier.DoubleModifier;
import net.Indyuce.moarbows.bow.particle.ParticleData;
import net.Indyuce.moarbows.util.LinearFormula;

public class Pulsar_Bow extends MoarBow {
	public Pulsar_Bow() {
		super(new String[] { "Shoots arrows that summon a black", "hole that attracts nearby enemies." }, new ParticleData(Particle.SMOKE_NORMAL),
				new String[] { "AIR,WITHER_SKELETON_SKULL,AIR", "WITHER_SKELETON_SKULL,BOW,WITHER_SKELETON_SKULL", "AIR,WITHER_SKELETON_SKULL,AIR" });

		addModifier(new DoubleModifier("cooldown", new LinearFormula(10, -1, 3, 10)), new DoubleModifier("duration", new LinearFormula(3, 1)));
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
		double duration = data.getDouble("duration") * 20;
		data.getArrow().remove();
		data.getArrow().getWorld().playSound(data.getArrow().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 1);
		new BukkitRunnable() {
			int ti = 0;
			double r = 4;
			final Location loc = data.getArrow().getLocation().clone();

			public void run() {
				ti++;
				loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HAT, 2, 2);
				loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 0);
				for (int j = 0; j < 3; j++) {
					double ran = random.nextDouble() * Math.PI * 2;
					double ran_y = random.nextDouble() * 2. - 1.;
					double x = Math.cos(ran) * Math.sin(ran_y * Math.PI * 2);
					double z = Math.sin(ran) * Math.sin(ran_y * Math.PI * 2);
					Location loc1 = loc.clone().add(x * r, ran_y * r, z * r);
					Vector v = loc.toVector().subtract(loc1.toVector());
					loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc1, 0, v.getX(), v.getY(), v.getZ(), .1);
				}
				for (Entity target : data.getArrow().getNearbyEntities(5, 5, 5))
					if (target instanceof LivingEntity) {
						target.playEffect(EntityEffect.HURT);
						target.setVelocity(
								data.getArrow().getLocation().toVector().subtract(target.getLocation().toVector()).normalize().multiply(.5));
					}
				if (ti > duration)
					cancel();
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
	}
}
