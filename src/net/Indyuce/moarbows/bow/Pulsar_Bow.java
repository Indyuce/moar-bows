package net.Indyuce.moarbows.bow;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Pulsar_Bow extends MoarBow {
	public Pulsar_Bow() {
		super(new String[] { "Shoots arrows that summon a black", "hole that attracts nearby enemies." }, 0, 10.0, "smoke_normal", new String[] { "AIR,WITHER_SKELETON_SKULL,AIR", "WITHER_SKELETON_SKULL,BOW,WITHER_SKELETON_SKULL", "AIR,WITHER_SKELETON_SKULL,AIR" });

		addModifier(new BowModifier("duration", 3));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(t, a);
	}

	@Override
	public void land(Player p, Arrow a) {
		double duration = getValue("duration") * 20;
		a.remove();
		a.getWorld().playSound(a.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 1);
		new BukkitRunnable() {
			int ti = 0;
			double r = 4;
			final Location loc = a.getLocation().clone();

			public void run() {
				ti++;
				loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HAT, 2, 2);
				loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 0);
				for (int j = 0; j < 3; j++) {
					double ran = random.nextDouble() * Math.PI * 2;
					double ran_y = random.nextDouble() * 2. - 1.;
					double x = Math.cos(ran) * Math.sin(ran_y * Math.PI * 2);
					double z = Math.sin(ran) * Math.sin(ran_y * Math.PI * 2);
					Location loc1 = loc.clone().add(x * r, ran_y * r, z * r);
					Vector v = loc.toVector().subtract(loc1.toVector());
					loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc1, 0, v.getX(), v.getY(), v.getZ(), .1);
				}
				for (Entity t : a.getNearbyEntities(5, 5, 5))
					if (t instanceof LivingEntity) {
						t.playEffect(EntityEffect.HURT);
						Vector v = a.getLocation().toVector().subtract(t.getLocation().toVector()).normalize().multiply(.5);
						t.setVelocity(v);
					}
				if (ti > duration)
					cancel();
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
	}
}
