package net.Indyuce.moarbows.bow;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.particle.ParticleData;
import net.Indyuce.moarbows.api.util.LinearFormula;

public class Meteor_Bow extends MoarBow {
	public Meteor_Bow() {
		super(new String[] { "Shoots arrows that summon a fire", "comet upon landing, dealing damage", "and knockback to nearby entities." },
				new ParticleData(Particle.LAVA),
				new String[] { "FIRE_CHARGE,FIRE_CHARGE,FIRE_CHARGE", "FIRE_CHARGE,BOW,FIRE_CHARGE", "FIRE_CHARGE,FIRE_CHARGE,FIRE_CHARGE" });

		addModifier(new DoubleModifier("cooldown", new LinearFormula(10, -1, 3, 10)), new DoubleModifier("damage", new LinearFormula(8, 4)),
				new DoubleModifier("knockback", new LinearFormula(1, 1.3)));
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
		double damage = data.getDouble("damage");
		double knockback = data.getDouble("knockback");

		data.getArrow().getWorld().playSound(data.getArrow().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2, 1);
		new BukkitRunnable() {
			final Location loc = data.getArrow().getLocation();
			final Location source = loc.clone().add(5 * Math.cos(random.nextDouble() * 2 * Math.PI), 20,
					5 * Math.sin(random.nextDouble() * 2 * Math.PI));
			final Vector vec = loc.subtract(source).toVector().multiply((double) 1 / 30);

			int ti = 0;

			public void run() {
				if (ti == 0)
					loc.setDirection(vec);

				for (int k = 0; k < 2; k++) {
					ti++;
					source.add(vec);
					for (double i = 0; i < Math.PI * 2; i += Math.PI / 6) {
						Vector vec = BowUtils.rotateFunc(new Vector(Math.cos(i), Math.sin(i), 0), loc);
						source.getWorld().spawnParticle(Particle.SMOKE_LARGE, source, 0, vec.getX(), vec.getY(), vec.getZ(), .1);
					}
				}

				if (ti >= 30) {
					source.getWorld().playSound(source, Sound.ENTITY_GENERIC_EXPLODE, 3, 1);
					source.getWorld().spawnParticle(Particle.FLAME, source, 64, 0, 0, 0, .25);
					source.getWorld().spawnParticle(Particle.LAVA, source, 32);
					for (double j = 0; j < Math.PI * 2; j += Math.PI / 24)
						source.getWorld().spawnParticle(Particle.SMOKE_LARGE, source, 0, Math.cos(j), 0, Math.sin(j), .5);

					for (LivingEntity entity : data.getArrow().getWorld().getEntitiesByClass(LivingEntity.class))
						if (entity.getLocation().add(0, 1, 0).distanceSquared(loc) < 25) {
							entity.damage(damage, data.getShooter());
							entity.setVelocity(entity.getLocation().toVector().subtract(loc.toVector()).setY(.75).normalize().multiply(knockback));
						}
					cancel();
				}
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
	}
}
