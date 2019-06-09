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

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Composite_Bow extends MoarBow {
	public Composite_Bow() {
		super(new String[] { "Fires enchanted arrows that", "follow a linear trajectory." }, 0, 2.5, "redstone:91,60,17", new String[] { "AIR,AIR,AIR", "BOW,NETHER_STAR,BOW", "AIR,AIR,AIR" });

		addModifier(new BowModifier("damage", 8));
	}

	@Override
	public boolean shoot(EntityShootBowEvent event, Arrow arrow, Player player, ItemStack item) {
		final double dmg = getValue("damage") * getPowerDamageMultiplier(item);
		event.setCancelled(true);
		if (!BowUtils.consumeAmmo(player, new ItemStack(Material.ARROW)))
			return false;

		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 2, 0);
		new BukkitRunnable() {
			Location loc = player.getEyeLocation();
			double ti = 0;
			double max = 20 * event.getForce();
			Vector v = player.getEyeLocation().getDirection().multiply(1.25);

			public void run() {
				for (double j = 0; j < 3; j++) {
					ti += .5;
					loc.add(v);
					loc.getWorld().spawnParticle(Particle.CRIT, loc, 8, .1, .1, .1, .1);
					loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HAT, 3, 2);
					for (LivingEntity t : loc.getWorld().getEntitiesByClass(LivingEntity.class))
						if (BowUtils.canDmgEntity(player, loc, t) && t != player) {
							t.getWorld().playSound(t.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
							loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 0);
							cancel();
							t.damage(dmg, player);
							return;
						}
				}
				if (ti >= max)
					cancel();
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
		return false;
	}
}
