package net.Indyuce.moarbows.bow;

import java.util.Random;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.ParticleEffect;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.version.VersionSound;

public class Pulsar_Bow extends MoarBow {
	public Pulsar_Bow() {
		super(new String[] { "Shoots arrows that summon a black", "hole that attracts nearby enemies." }, 0, 10.0, "smoke_normal", new String[] { "AIR,SKULL_ITEM:1,AIR", "SKULL_ITEM:1,BOW,SKULL_ITEM:1", "AIR,SKULL_ITEM:1,AIR" });

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
		a.getWorld().playSound(a.getLocation(), VersionSound.ENTITY_ENDERMEN_TELEPORT.getSound(), 3, 1);
		new BukkitRunnable() {
			int ti = 0;
			double r = 4;
			final Location loc = a.getLocation().clone();

			public void run() {
				ti++;
				loc.getWorld().playSound(loc, VersionSound.BLOCK_NOTE_HAT.getSound(), 2, 2);
				ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, loc, 100);
				for (int j = 0; j < 3; j++) {
					double ran = new Random().nextDouble() * Math.PI * 2;
					double ran_y = new Random().nextDouble() * 2. - 1.;
					double x = Math.cos(ran) * Math.sin(ran_y * Math.PI * 2);
					double z = Math.sin(ran) * Math.sin(ran_y * Math.PI * 2);
					Location loc1 = loc.clone().add(x * r, ran_y * r, z * r);
					Vector v = loc.toVector().subtract(loc1.toVector());
					ParticleEffect.SMOKE_LARGE.display(v, .1f, loc1, 100);
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
