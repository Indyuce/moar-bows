package net.Indyuce.mb.resource.bow;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;

public class FireBow implements SpecialBow {
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

	private static void explode(Arrow a) {
		int duration = Main.bows.getInt("FIRE_BOW.duration") * 20;
		int maxTicks = Main.bows.getInt("FIRE_BOW.max-burning-time") * 20;
		a.remove();

		a.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, a.getLocation(), 0);
		a.getWorld().spawnParticle(Particle.LAVA, a.getLocation(), 12, 0, 0, 0);
		a.getWorld().spawnParticle(Particle.FLAME, a.getLocation(), 48, 0, 0, 0, .13);
		a.getWorld().playSound(a.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 1);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				int ticks = ent.getFireTicks() + duration;
				ticks = (ticks > maxTicks ? maxTicks : ticks);
				ent.setFireTicks(ticks);
			}
	}
}
