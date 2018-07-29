package net.Indyuce.mb.resource.bow;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;

public class MeteorBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		explode(a);
	}

	@Override
	public void land(Arrow a) {
		explode(a);
	}

	private void explode(Arrow a) {
		a.remove();
		double damage = Main.bows.getInt("METEOR_BOW.damage");
		double knockback = Main.bows.getInt("METEOR_BOW.knockback");
		a.getWorld().playSound(a.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 1);
		new BukkitRunnable() {
			Location loc = a.getLocation().clone();
			Location source = a.getLocation().clone().add(0, 20, 0);
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
					for (LivingEntity t : a.getWorld().getEntitiesByClass(LivingEntity.class))
						if (t.getLocation().add(0, 1, 0).distanceSquared(loc) < 25) {
							t.damage(damage);
							t.setVelocity(t.getLocation().toVector().subtract(loc.toVector()).setY(.75).normalize().multiply(knockback));
						}
					cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 0, 1);
	}
}
