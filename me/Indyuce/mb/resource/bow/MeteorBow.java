package me.Indyuce.mb.resource.bow;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Indyuce.mb.ConfigData;
import me.Indyuce.mb.Eff;
import me.Indyuce.mb.Main;
import me.Indyuce.mb.resource.SpecialBow;
import me.Indyuce.mb.resource.TaskState;
import me.Indyuce.mb.util.VersionUtils;

public class MeteorBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		explode(a);
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		explode(a);
		return TaskState.CONTINUE;
	}

	private void explode(Arrow a) {
		a.remove();
		FileConfiguration config = ConfigData.getCD(Main.plugin, "", "bows");
		double damage = config.getInt("METEOR_BOW.damage");
		double knockback = config.getInt("METEOR_BOW.knockback");
		VersionUtils.sound(a.getLocation(), "ENTITY_ENDERMEN_TELEPORT", 3, 1);
		new BukkitRunnable() {
			Location loc = a.getLocation().clone();
			Location source = a.getLocation().clone().add(0, 20, 0);
			Vector v = loc.toVector().subtract(source.toVector()).multiply(.06);
			double ti = 0;

			public void run() {
				ti += .06;
				source.add(v);
				Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, source, 150);
				Eff.FLAME.display(.2f, .2f, .2f, 0, 4, source, 100);
				if (ti >= 1) {
					VersionUtils.sound(loc, "ENTITY_GENERIC_EXPLODE", 3, 1);
					Eff.EXPLOSION_LARGE.display(2, 2, 2, 0, 16, loc, 1000);
					Eff.FLAME.display(0, 0, 0, .25f, 32, loc.add(0, .1, 0), 100);
					Eff.EXPLOSION_NORMAL.display(0, 0, 0, .25f, 32, loc, 100);
					for (LivingEntity t : a.getWorld().getEntitiesByClass(LivingEntity.class)) {
						if (t.getLocation().add(0, 1, 0).distanceSquared(loc) < 25) {
							t.damage(damage);
							t.setVelocity(t.getLocation().toVector().subtract(loc.toVector()).setY(.75).normalize().multiply(knockback));
						}
					}
					cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 0, 1);
	}
}
