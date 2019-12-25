package net.Indyuce.moarbows.bow;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.util.LinearValue;

public class Blaze_Bow extends MoarBow {
	public Blaze_Bow() {
		super(new String[] { "Shoots a long ranged firebolt that", "deals &c{damage} &7damage to the first entity it", "hits, igniting him for &c{duration} &7seconds." }, 0, "flame", new String[] { "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM", "MAGMA_CREAM,BOW,MAGMA_CREAM", "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(10, -1, 3, 10)), new DoubleModifier("damage", new LinearValue(8, 2)), new DoubleModifier("duration", new LinearValue(4, .3)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		event.setCancelled(true);
		final double dmg = data.getDouble("damage") * BowUtils.getPowerDamageMultiplier(data.getSource().getItem());
		final double duration = data.getDouble("duration");
		if (!BowUtils.consumeAmmo(data.getShooter(), new ItemStack(Material.ARROW)))
			return false;

		new BukkitRunnable() {
			Location loc = data.getShooter().getEyeLocation();
			double ti = 0;
			Vector v = data.getShooter().getEyeLocation().getDirection().multiply(1.25);

			public void run() {
				for (double j = 0; j < 3; j++) {
					ti += .5;
					loc.add(v);
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 8, .1, .1, .1, 0);
					loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 0);
					loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HAT, 3, 2);
					for (LivingEntity entity : loc.getWorld().getEntitiesByClass(LivingEntity.class))
						if (BowUtils.canTarget(data.getShooter(), loc, entity) && !entity.equals(data.getShooter())) {
							new BukkitRunnable() {
								final Location loc2 = entity.getLocation();
								double y = 0;

								public void run() {
									for (int item = 0; item < 2; item++) {
										y += .05;
										for (int j = 0; j < 2; j++) {
											double xz = y * Math.PI * .8 + (j * Math.PI);
											loc.getWorld().spawnParticle(Particle.FLAME, loc2.clone().add(Math.cos(xz) * 1.3, y, Math.sin(xz) * 1.3), 0);

										}
									}
									if (y >= 2.5)
										cancel();
								}
							}.runTaskTimer(MoarBows.plugin, 0, 1);
							entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
							loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, entity.getLocation().add(0, 1, 0), 0);
							cancel();
							entity.damage(dmg, data.getShooter());
							entity.setFireTicks((int) (duration * 20));
							return;
						}
				}
				if (ti >= 20 * event.getForce())
					cancel();
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
		return false;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
	}

	@Override
	public void whenLand(ArrowData data) {
	}
}
