package me.Indyuce.mb.resource.bow;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Indyuce.mb.Eff;
import me.Indyuce.mb.Main;
import me.Indyuce.mb.resource.SpecialBow;
import me.Indyuce.mb.resource.TaskState;
import me.Indyuce.mb.util.VersionUtils;

public class SnowBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		e.setCancelled(true);
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				ti++;
				if (ti > 20 * e.getForce())
					cancel();

				Location loc = p.getEyeLocation().clone();
				Eff.SNOWBALL.display(.2f, .2f, .2f, 0, 6, loc, 100);
				VersionUtils.sound(p.getLocation(), "ENTITY_SNOWBALL_THROW", 1, 1.5f);
				Snowball s = p.launchProjectile(Snowball.class);
				loc.setPitch(loc.getPitch() + new Random().nextInt(3) - 1);
				loc.setYaw(loc.getYaw() + new Random().nextInt(3) - 1);
				Vector v = loc.getDirection().multiply(3.3 * e.getForce());
				s.setVelocity(v);
			}
		}.runTaskTimer(Main.plugin, 0, 2);
		return TaskState.BREAK;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		return TaskState.CONTINUE;
	}
}
