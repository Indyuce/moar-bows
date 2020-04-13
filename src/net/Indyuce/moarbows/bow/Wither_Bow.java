package net.Indyuce.moarbows.bow;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.particle.ParticleData;
import net.Indyuce.moarbows.api.util.LinearValue;

public class Wither_Bow extends MoarBow {
	public Wither_Bow() {
		super(new String[] { "Shoots an exploding wither skull." }, new ParticleData(Particle.REDSTONE, Color.fromRGB(0, 0, 0)),
				new String[] { "WITHER_SKELETON_SKULL,WITHER_SKELETON_SKULL,WITHER_SKELETON_SKULL", "WITHER_SKELETON_SKULL,BOW,WITHER_SKELETON_SKULL",
						"WITHER_SKELETON_SKULL,WITHER_SKELETON_SKULL,WITHER_SKELETON_SKULL" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(4, -8., 2, 4)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		event.setCancelled(true);
		if (!BowUtils.consumeAmmo(data.getShooter(), new ItemStack(Material.ARROW)))
			return false;

		data.getShooter().getWorld().playSound(data.getShooter().getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, 1);
		WitherSkull skull = data.getShooter().launchProjectile(WitherSkull.class);
		skull.setVelocity(data.getShooter().getEyeLocation().getDirection().multiply(3.3 * event.getForce()));
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
