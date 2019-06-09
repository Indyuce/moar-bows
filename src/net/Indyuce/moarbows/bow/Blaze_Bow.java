package net.Indyuce.moarbows.bow;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Blaze_Bow extends MoarBow {
	public Blaze_Bow() {
		super(new String[] { "Shoots a long ranged firebolt that", "deals 8 damage to the first entity it", "hits, igniting him for 4 seconds." }, 0, 10.0, "flame", new String[] { "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM", "MAGMA_CREAM,BOW,MAGMA_CREAM", "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM" });

		addModifier(new BowModifier("damage", 8), new BowModifier("duration", 4));
	}

	@Override
	public boolean shoot(EntityShootBowEvent event, Arrow arrow, Player player, ItemStack item) {
		event.setCancelled(true);
		final double dmg = getValue("damage") * getPowerDamageMultiplier(item);
		final double duration = getValue("duration");
		if (!BowUtils.consumeAmmo(player, new ItemStack(Material.ARROW)))
			return false;

		new BukkitRunnable() {
			Location loc = player.getEyeLocation();
			double ti = 0;
			Vector v = player.getEyeLocation().getDirection().multiply(1.25);

			public void run() {
				for (double j = 0; j < 3; j++) {
					ti += .5;
					loc.add(v);
					loc.getWorld().spawnParticle(Particle.FLAME, loc, 8, .1, .1, .1, 0);
					loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 0);
					loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HAT, 3, 2);
					for (LivingEntity t : loc.getWorld().getEntitiesByClass(LivingEntity.class))
						if (BowUtils.canDmgEntity(player, loc, t) && t != player) {
							new BukkitRunnable() {
								final Location loc2 = t.getLocation();
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
							t.getWorld().playSound(t.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
							loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, t.getLocation().add(0, 1, 0), 0);
							cancel();
							t.damage(dmg, player);
							t.setFireTicks((int) (duration * 20));
							return;
						}
				}
				if (ti >= 20 * event.getForce())
					cancel();
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
		return false;
	}
}
