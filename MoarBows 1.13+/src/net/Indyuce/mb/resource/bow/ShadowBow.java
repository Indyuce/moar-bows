package net.Indyuce.mb.resource.bow;

import org.bukkit.Color;
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
import org.bukkit.util.Vector;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;
import net.Indyuce.mb.util.Utils;

public class ShadowBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		e.setCancelled(true);
		final double dmg = Main.bows.getDouble("SHADOW_BOW.damage");
		if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
			return false;

		new BukkitRunnable() {
			Location loc = p.getEyeLocation();
			double ti = 0;
			Vector v = p.getEyeLocation().getDirection().multiply(1.25);

			public void run() {
				for (double j = 0; j < 3; j++) {
					ti += .5;
					loc.add(v);
					loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 8, .1, .1, .1, 0);
					loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_HURT, 3, 2);
					for (LivingEntity t : loc.getWorld().getEntitiesByClass(LivingEntity.class))
						if (Utils.canDmgEntity(p, loc, t) && t != p) {
							new BukkitRunnable() {
								final Location loc2 = t.getLocation();
								double y = 0;

								public void run() {
									for (int i = 0; i < 2; i++) {
										y += .05;
										for (int j = 0; j < 2; j++) {
											double xz = y * Math.PI * .8 + (j * Math.PI);
											loc.getWorld().spawnParticle(Particle.REDSTONE, loc2.clone().add(Math.cos(xz) * 1.3, y, Math.sin(xz) * 1.3), 0, new Particle.DustOptions(Color.PURPLE, 1));
										}
									}
									if (y >= 2.5)
										cancel();
								}
							}.runTaskTimer(Main.plugin, 0, 1);
							loc.getWorld().playSound(t.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
							loc.getWorld().spawnParticle(Particle.SPELL_WITCH, t.getLocation().add(0, 1, 0), 0);
							cancel();
							t.damage(dmg);
							return;
						}
				}
				if (ti >= 20 * e.getForce())
					cancel();
			}
		}.runTaskTimer(Main.plugin, 0, 1);
		return false;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
	}

	@Override
	public void land(Arrow a) {
	}
}
