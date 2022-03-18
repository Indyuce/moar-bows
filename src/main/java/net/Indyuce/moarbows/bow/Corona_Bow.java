package net.Indyuce.moarbows.bow;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.particle.ParticleData;
import net.Indyuce.moarbows.api.util.LinearFormula;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Corona_Bow extends MoarBow {
	public Corona_Bow() {
		super(new String[] { "Creates a deadly mark on the", "ground during &c{duration} &7seconds, dealing",
				"&c{damage} &7damage per second to entities", "within &c{radius} &7blocks.", },
				new ParticleData(Particle.REDSTONE, Color.fromRGB(0, 255, 0), 2),
				new String[] { "SLIME_BALL,SLIME_BALL,SLIME_BALL", "SLIME_BALL,BOW,SLIME_BALL", "SLIME_BALL,SLIME_BALL,SLIME_BALL" });

		addModifier(new DoubleModifier("cooldown", new LinearFormula(10, 0)), new DoubleModifier("radius", new LinearFormula(5, 1)),
				new DoubleModifier("damage", new LinearFormula(2, 1)), new DoubleModifier("duration", new LinearFormula(5, 1)));
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
		data.getArrow().getWorld().playSound(data.getArrow().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 1);
		new CoronaZone(data);
	}

	public class CoronaZone extends BukkitRunnable {
		private final LivingEntity shooter;
		private final Location loc;
		private final long duration;
		private final double damage, radiusSquared;

		private int t;

		public CoronaZone(ArrowData data) {
			shooter = data.getShooter();
			loc = data.getArrow().getLocation();
			duration = (long) (data.getDouble("duration") * 10);
			damage = data.getDouble("damage");
			radiusSquared = Math.pow(data.getDouble("radius"), 2);

			runTaskTimer(MoarBows.plugin, 0, 2);
		}

		@Override
		public void run() {
			if (t++ > duration)
				cancel();

			loc.getWorld().spawnParticle(Particle.TOTEM, loc, 8, 3, .1, 3, .1);
			loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 8, 3, .1, 3, new Particle.DustOptions(Color.GREEN, 1.2f));

			if (t % 5 == 0)
				BowUtils.forEachNearbyChunkEntity(loc, entity -> {
					if (entity instanceof LivingEntity && entity.getLocation().distanceSquared(loc) < radiusSquared
							&& BowUtils.canTarget(shooter, null, entity))
						((Damageable) entity).damage(damage / 2);
				});
		}
	}
}
