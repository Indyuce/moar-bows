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
import net.Indyuce.moarbows.api.LinearValue;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;

public class Composite_Bow extends MoarBow {
	public Composite_Bow() {
		super(new String[] { "Fires enchanted arrows that", "follow a linear trajectory.", "Deals &c{damage} &7damage." }, 0, "redstone:91,60,17", new String[] { "AIR,AIR,AIR", "BOW,NETHER_STAR,BOW", "AIR,AIR,AIR" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(2, 0)), new DoubleModifier("damage", new LinearValue(8, 2)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		final double dmg = data.getDouble("damage") * getPowerDamageMultiplier(data.getSource().getItem());
		event.setCancelled(true);
		if (!BowUtils.consumeAmmo(data.getSender(), new ItemStack(Material.ARROW)))
			return false;

		data.getSender().getWorld().playSound(data.getSender().getLocation(), Sound.ENTITY_ARROW_SHOOT, 2, 0);
		new BukkitRunnable() {
			Location loc = data.getSender().getEyeLocation();
			double ti = 0;
			double max = 20 * event.getForce();
			Vector v = data.getSender().getEyeLocation().getDirection().multiply(1.25);

			public void run() {
				for (double j = 0; j < 3; j++) {
					ti += .5;
					loc.add(v);
					loc.getWorld().spawnParticle(Particle.CRIT, loc, 8, .1, .1, .1, .1);
					loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HAT, 3, 2);
					for (LivingEntity entity : loc.getWorld().getEntitiesByClass(LivingEntity.class))
						if (BowUtils.canDmgEntity(data.getSender(), loc, entity) && !entity.equals(data.getSender())) {
							entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 0);
							loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 0);
							cancel();
							entity.damage(dmg, data.getSender());
							return;
						}
				}
				if (ti >= max)
					cancel();
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
		return false;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
		// TODO Auto-generated method stub

	}

	@Override
	public void whenLand(ArrowData data) {
		// TODO Auto-generated method stub

	}
}
