package net.Indyuce.moarbows.bow;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.util.LinearValue;

public class Laser_Bow extends MoarBow {
	public Laser_Bow() {
		super(new String[] { "Fires instant laser arrows", "that deals &c{damage} &7damage to", "every entity it hits." }, 0, "redstone:255,0,0", new String[] { "REDSTONE_BLOCK,REDSTONE_BLOCK,REDSTONE_BLOCK", "REDSTONE_BLOCK,BOW,REDSTONE_BLOCK", "REDSTONE_BLOCK,REDSTONE_BLOCK,REDSTONE_BLOCK" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(0, 0)), new DoubleModifier("damage", new LinearValue(5, 3)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		double damage = data.getDouble("damage") * BowUtils.getPowerDamageMultiplier(data.getSource().getItem());
		if (!BowUtils.consumeAmmo(data.getShooter(), new ItemStack(Material.ARROW)))
			return false;

		data.getShooter().getWorld().playSound(data.getShooter().getLocation(), Sound.ENTITY_ARROW_SHOOT, 2, 0);
		int range = (int) (56 * event.getForce());
		Location loc = data.getShooter().getEyeLocation();
		List<Integer> hit = new ArrayList<>();
		for (int j = 0; j < range; j++) {
			loc.add(data.getShooter().getEyeLocation().getDirection());
			loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, new Particle.DustOptions(Color.RED, 1.2f));
			if (loc.getBlock().getType().isSolid())
				break;

			for (Entity target : data.getShooter().getNearbyEntities(100, 100, 100))
				if (!hit.contains(target.getEntityId()) && BowUtils.canTarget(data.getShooter(), loc, target) && target instanceof LivingEntity) {
					hit.add(target.getEntityId());
					((LivingEntity) target).damage(damage, data.getShooter());
				}
		}
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
