package net.Indyuce.moarbows.bow;

import org.bukkit.Color;
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

public class Shadow_Bow extends MoarBow {
	public Shadow_Bow() {
		super(new String[] { "Shoots a long ranged linear", "cursed arrow that deals 8", "damage tothe first entity it hits." }, 0, 10.0, "redstone:128,0,128", new String[] { "ENDER_EYE,ENDER_EYE,ENDER_EYE", "ENDER_EYE,BOW,ENDER_EYE", "ENDER_EYE,ENDER_EYE,ENDER_EYE" });

		addModifier(new BowModifier("damage", 8));
	}

	@Override
	public boolean shoot(EntityShootBowEvent event, Arrow arrow, Player player, ItemStack item) {
		event.setCancelled(true);
		final double dmg = getValue("damage") * getPowerDamageMultiplier(item);
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
					loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 8, .1, .1, .1, 0);
					loc.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_HURT, 3, 2);
					for (LivingEntity entity : loc.getWorld().getEntitiesByClass(LivingEntity.class))
						if (BowUtils.canDmgEntity(player, loc, entity)) {
							new BukkitRunnable() {
								final Location loc2 = entity.getLocation();
								double y = 0;

								public void run() {
									for (int item = 0; item < 2; item++) {
										y += .05;
										for (int j = 0; j < 2; j++) {
											double xz = y * Math.PI * .8 + (j * Math.PI);
											loc.getWorld().spawnParticle(Particle.REDSTONE, loc2.clone().add(Math.cos(xz) * 1.3, y, Math.sin(xz) * 1.3), 0, new Particle.DustOptions(Color.PURPLE, 1));
										}
									}
									if (y >= 2.5) {
										cancel();
									}
								}
							}.runTaskTimer(MoarBows.plugin, 0, 1);
							loc.getWorld().playSound(entity.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
							loc.getWorld().spawnParticle(Particle.SPELL_WITCH, entity.getLocation().add(0, 1, 0), 0);
							cancel();
							entity.damage(dmg, entity);
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
