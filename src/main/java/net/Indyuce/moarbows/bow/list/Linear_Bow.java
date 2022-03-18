package net.Indyuce.moarbows.bow.list;

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
import net.Indyuce.moarbows.bow.ArrowData;
import net.Indyuce.moarbows.bow.MoarBow;
import net.Indyuce.moarbows.bow.modifier.DoubleModifier;
import net.Indyuce.moarbows.bow.particle.ParticleData;
import net.Indyuce.moarbows.util.LinearFormula;

public class Linear_Bow extends MoarBow {
	public Linear_Bow() {
		super(new String[] { "Fires instant linear arrows", "that deals &c{damage} &7damage to", "the first entity it hits." },
				new ParticleData(Particle.REDSTONE, Color.fromRGB(90, 90, 255), 2),
				new String[] { "FLINT,STICK,FLINT", "STICK,BOW,STICK", "FLINT,STICK,FLINT" });

		addModifier(new DoubleModifier("cooldown", new LinearFormula(0, 0)), new DoubleModifier("damage", new LinearFormula(8, 3)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		double dmg = data.getDouble("damage") * BowUtils.getPowerDamageMultiplier(data.getSource());
		if (!BowUtils.consumeAmmo(data.getShooter(), new ItemStack(Material.ARROW)))
			return false;

		data.getShooter().getWorld().playSound(data.getShooter().getLocation(), Sound.ENTITY_ARROW_SHOOT, 2, 0);
		int range = (int) (56 * event.getForce());
		Location loc = data.getShooter().getEyeLocation();
		for (double j = 0; j < range; j++) {
			loc.add(data.getShooter().getEyeLocation().getDirection());
			loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, new Particle.DustOptions(Color.GRAY, 2));
			if (loc.getBlock().getType().isSolid())
				break;

			for (Entity entity : data.getShooter().getNearbyEntities(30, 30, 30))
				if (BowUtils.canTarget(data.getShooter(), loc, entity) && entity instanceof LivingEntity) {
					event.setCancelled(true);
					((LivingEntity) entity).damage(dmg, data.getShooter());
					loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 0);
					break;
				}
		}
		return false;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
	}

	@Override
	public void whenLand(ArrowData data) {
	}
}
