package net.Indyuce.mb.resource.bow;

import java.util.Random;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
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

import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;

public class EarthquakeBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		explode(a, p.getLocation());
	}

	@Override
	public void land(Arrow a) {
		explode(a, a.getLocation());
	}

	private void explode(Arrow a, Location effLoc) {
		double radius = Main.bows.getDouble("EARTHQUAKE_BOW.radius");
		double knockup = Main.bows.getDouble("EARTHQUAKE_BOW.knockup");

		Location loc = a.getLocation();
		for (int j = 0; j < 20; j++)
			if (loc.add(0, -1, 0).getBlock().getType().isSolid()) {
				loc.setY(Math.floor(loc.getY()) + 1);
				Random r = new Random();
				for (int k = 0; k < 64; k++) {
					double rx = (r.nextDouble() - .5) * 6;
					double rz = (r.nextDouble() - .5) * 6;
					loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc.clone().add(rx, 1, rz), 0, Material.DIRT.createBlockData());
				}
				break;
			}

		// needs a small delay because of the arrow knockback
		a.remove();
		a.getWorld().playSound(a.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 2, 0);
		new BukkitRunnable() {
			public void run() {
				for (Entity ent : a.getNearbyEntities(radius, radius, radius))
					if (ent instanceof LivingEntity) {
						ent.playEffect(EntityEffect.HURT);
						ent.setVelocity(ent.getVelocity().setY(knockup));
					}
			}
		}.runTaskLater(Main.plugin, 1);
	}
}
