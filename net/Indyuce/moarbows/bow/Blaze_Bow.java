package net.Indyuce.moarbows.bow;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.ParticleEffect;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.version.VersionSound;

public class Blaze_Bow extends MoarBow {
	public Blaze_Bow() {
		super(new String[] { "Shoots a long ranged firebolt that", "deals 8 damage to the first entity it", "hits, igniting him for 4 seconds." }, 0, 10.0, "flame", new String[] { "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM", "MAGMA_CREAM,BOW,MAGMA_CREAM", "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM" });

		addModifier(new BowModifier("damage", 8), new BowModifier("duration", 4));
	}

	@Override
	public boolean shoot(EntityShootBowEvent event, Arrow arrow, Player player, ItemStack item) {
		event.setCancelled(true);
		final double dmg = MoarBows.getLanguage().getBows().getDouble("BLAZE_BOW.damage") * getPowerDamageMultiplier(item);
		final double duration = MoarBows.getLanguage().getBows().getDouble("BLAZE_BOW.duration");
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
					ParticleEffect.FLAME.display(.1f, .1f, .1f, 0, 8, loc, 100);
					ParticleEffect.SMOKE_NORMAL.display(0, 0, 0, 0, 1, loc, 150);
					loc.getWorld().playSound(loc, VersionSound.BLOCK_NOTE_HAT.getSound(), 2, 2);
					for (LivingEntity t : loc.getWorld().getEntitiesByClass(LivingEntity.class))
						if (BowUtils.canDmgEntity(player, loc, t) && t != player) {
							new BukkitRunnable() {
								final Location loc2 = t.getLocation();
								double y = 0;

								public void run() {
									for (int i = 0; i < 2; i++) {
										y += .05;
										for (int j = 0; j < 2; j++) {
											double xz = y * Math.PI * .8 + (j * Math.PI);
											ParticleEffect.FLAME.display(0, 0, 0, 0, 1, loc2.clone().add(Math.cos(xz) * 1.3, y, Math.sin(xz) * 1.3), 150);
										}
									}
									if (y >= 2.5)
										cancel();
								}
							}.runTaskTimer(MoarBows.plugin, 0, 1);
							loc.getWorld().playSound(loc, VersionSound.ENTITY_FIREWORK_BLAST.getSound(), 2, 0);
							ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, t.getLocation().add(0, 1, 0), 100);
							cancel();
							MoarBows.getNMS().damageEntity(player, t, dmg);
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
