package net.Indyuce.mb.resource.bow;

import java.util.Random;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;
import net.Indyuce.mb.util.VersionUtils;

public class PulsarBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		explode(a);
	}

	@Override
	public void land(Arrow a) {
		explode(a);
	}

	private void explode(Arrow a) {
		double duration = Main.bows.getDouble("PULSAR_BOW.duration") * 20;
		a.remove();
		VersionUtils.sound(a.getLocation(), "ENTITY_ENDERMEN_TELEPORT", 3, 1);
		new BukkitRunnable() {
			int ti = 0;
			double r = 4;
			final Location loc = a.getLocation().clone();

			public void run() {
				ti++;
				VersionUtils.sound(loc, "BLOCK_NOTE_HAT", 2, 2);
				Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, loc, 100);
				for (int j = 0; j < 3; j++) {
					double ran = new Random().nextDouble() * Math.PI * 2;
					double ran_y = new Random().nextDouble() * 2. - 1.;
					double x = Math.cos(ran) * Math.sin(ran_y * Math.PI * 2);
					double z = Math.sin(ran) * Math.sin(ran_y * Math.PI * 2);
					Location loc1 = loc.clone().add(x * r, ran_y * r, z * r);
					Vector v = loc.toVector().subtract(loc1.toVector());
					Eff.SMOKE_LARGE.display(v, .1f, loc1, 100);
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
		}.runTaskTimer(Main.plugin, 0, 1);
	}
}
