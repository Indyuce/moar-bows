package net.Indyuce.moarbows.bow;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.particle.ParticleData;
import net.Indyuce.moarbows.api.util.LinearValue;

public class Trippple_Bow extends MoarBow {
	public Trippple_Bow() {
		super(new String[] { "Shoots 3 arrows at a time." }, new ParticleData(Particle.REDSTONE, Color.fromRGB(255, 255, 255)),
				new String[] { "AIR,AIR,AIR", "BOW,BOW,BOW", "AIR,AIR,AIR" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(2.5, 0)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		event.setCancelled(true);
		data.getShooter().getWorld().playSound(data.getShooter().getLocation(), Sound.ENTITY_ARROW_SHOOT, 2, 1);
		Location loc = data.getShooter().getLocation().add(0, 1.2, 0);
		for (int j = -1; j < 2; j++) {
			if (!BowUtils.consumeAmmo(data.getShooter(), new ItemStack(Material.ARROW)))
				return false;

			loc.setYaw(data.getShooter().getLocation().getYaw() + j);
			data.getShooter().launchProjectile(Arrow.class).setVelocity(loc.getDirection().multiply(event.getForce() * 3.3));
		}
		return true;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
	}

	@Override
	public void whenLand(ArrowData data) {
	}
}
