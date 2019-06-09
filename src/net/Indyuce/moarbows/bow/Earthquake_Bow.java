package net.Indyuce.moarbows.bow;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Earthquake_Bow extends MoarBow {
	public Earthquake_Bow() {
		super(new String[] { "Summons a shockwave when hitting", "anything, powerfully knocking up", "all enemies within 5 blocks." }, 0, 10.0, "redstone:128,0,0", new String[] { "DIRT,DIRT,DIRT", "DIRT,BOW,DIRT", "DIRT,DIRT,DIRT" });

		addModifier(new BowModifier("knockup", 1), new BowModifier("radius", 5));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(t, a);
	}

	@Override
	public void land(Player p, Arrow a) {
		double radius = getValue("radius");
		double knockup = getValue("knockup");

		Location loc = a.getLocation();
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
		a.remove();
		a.getWorld().playSound(a.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 2, 0);
		new BukkitRunnable() {
			public void run() {
				for (Entity ent : a.getNearbyEntities(radius, radius, radius))
					if (ent instanceof LivingEntity) {
						ent.playEffect(EntityEffect.HURT);
						ent.setVelocity(ent.getVelocity().setY(knockup));
					}
			}
		}.runTaskLater(MoarBows.plugin, 1);

	}
}
