package net.Indyuce.moarbows.bow;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Meteor_Bow extends MoarBow {
	public Meteor_Bow() {
		super(new String[] { "Shoots arrows that summon a fire", "comet upon landing, dealing damage", "and knockback to nearby entities." }, 0, 10.0, "lava", new String[] { "FIRE_CHARGE,FIRE_CHARGE,FIRE_CHARGE", "FIRE_CHARGE,BOW,FIRE_CHARGE", "FIRE_CHARGE,FIRE_CHARGE,FIRE_CHARGE" });

		addModifier(new BowModifier("damage", 8), new BowModifier("knockback", 1));
	}

	@Override
	public void hit(EntityDamageByEntityEvent event, Arrow arrow, Entity target, Player player) {
		land(player, arrow);
	}

	@Override
	public void land(Player player, Arrow arrow) {
		arrow.remove();
		double dmg = getValue("damage");
		double knockback = getValue("knockback");
		arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 1);
		new BukkitRunnable() {
			Location loc = arrow.getLocation().clone();
			Location source = arrow.getLocation().clone().add(0, 20, 0);
			Vector v = loc.toVector().subtract(source.toVector()).multiply(.06);
			double ti = 0;

			public void run() {
				ti += .06;
				source.add(v);
				loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, source, 0);
				loc.getWorld().spawnParticle(Particle.FLAME, source, 4, .2, .2, .2, 0);
				if (ti >= 1) {
					loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 3, 1);
					loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 16, 2, 2, 2);
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 32, 0, 0, 0, .25);
					loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 32, 0, 0, 0, .25);
					for (LivingEntity entity : arrow.getWorld().getEntitiesByClass(LivingEntity.class))
						if (entity.getLocation().add(0, 1, 0).distanceSquared(loc) < 25) {
							entity.damage(dmg, player);
							entity.setVelocity(entity.getLocation().toVector().subtract(loc.toVector()).setY(.75).normalize().multiply(knockback));
						}
					cancel();
				}
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
	}
}
