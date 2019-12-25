package net.Indyuce.moarbows.bow;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.util.LinearValue;

public class Earthquake_Bow extends MoarBow {
	public Earthquake_Bow() {
		super(new String[] { "Summons a shockwave when hitting", "anything, powerfully knocking up", "all enemies within &c{radius} &7blocks.", "Knock-up Force: &c{knockup}" }, 0, "redstone:128,0,0", new String[] { "DIRT,DIRT,DIRT", "DIRT,BOW,DIRT", "DIRT,DIRT,DIRT" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(10, -1, 3, 10)), new DoubleModifier("knockup", new LinearValue(1, .5)), new DoubleModifier("radius", new LinearValue(5, .2)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		return true;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
		whenLand(data);
	}

	@Override
	public void whenLand(ArrowData data) {
		double radius = data.getDouble("radius");
		double knockup = data.getDouble("knockup");

		Location loc = data.getArrow().getLocation();
		for (int j = 0; j < 20; j++)
			if (loc.add(0, -1, 0).getBlock().getType().isSolid()) {
				loc.setY(Math.floor(loc.getY()) + 1);
				for (int k = 0; k < 64; k++) {
					double rx = (random.nextDouble() - .5) * 6;
					double rz = (random.nextDouble() - .5) * 6;
					loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc.clone().add(rx, 1, rz), 0, Material.DIRT.createBlockData());
				}
				break;
			}

		// needs a small delay because of the arrow knockback
		data.getArrow().remove();
		data.getArrow().getWorld().playSound(data.getArrow().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 2, 0);
		new BukkitRunnable() {
			public void run() {
				for (Entity ent : data.getArrow().getNearbyEntities(radius, radius, radius))
					if (ent instanceof LivingEntity) {
						ent.playEffect(EntityEffect.HURT);
						ent.setVelocity(ent.getVelocity().setY(knockup));
					}
			}
		}.runTaskLater(MoarBows.plugin, 1);
	}
}
