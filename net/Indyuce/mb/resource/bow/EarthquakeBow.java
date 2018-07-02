package net.Indyuce.mb.resource.bow;

import java.util.Random;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
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

import net.Indyuce.mb.ConfigData;
import net.Indyuce.mb.Eff;
import net.Indyuce.mb.Main;
import net.Indyuce.mb.resource.SpecialBow;
import net.Indyuce.mb.resource.TaskState;
import net.Indyuce.mb.util.VersionUtils;

public class EarthquakeBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		explode(a, p.getLocation());
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		explode(a, a.getLocation());
		return TaskState.CONTINUE;
	}

	private void explode(Arrow a, Location effLoc) {
		FileConfiguration config = ConfigData.getCD(Main.plugin, "", "bows");
		double radius = config.getDouble("EARTHQUAKE_BOW.radius");
		double knockup = config.getDouble("EARTHQUAKE_BOW.knockup");

		Location loc = a.getLocation();
		for (int j = 0; j < 20; j++)
			if (loc.add(0, -1, 0).getBlock().getType().isSolid()) {
				loc.setY(Math.floor(loc.getY()) + 1);
				Random r = new Random();
				for (int k = 0; k < 64; k++) {
					double rx = (r.nextDouble() - .5) * 6;
					double rz = (r.nextDouble() - .5) * 6;
					Eff.BLOCK_CRACK.display(new Eff.BlockData(Material.DIRT, (byte) 0), new Vector(0, 0, 0), 0, loc.clone().add(rx, .1, rz), 100);
				}
				break;
			}

		// needs a small delay because of the arrow knockback
		a.remove();
		VersionUtils.sound(a.getLocation(), "ENTITY_ZOMBIE_ATTACK_DOOR_WOOD", 2, 0);
		new BukkitRunnable() {
			public void run() {
				for (Entity ent : a.getNearbyEntities(radius, radius, radius))
					if (ent instanceof LivingEntity) {
						ent.playEffect(EntityEffect.HURT);
						ent.setVelocity(ent.getVelocity().setY(knockup));
					}
			}
		}.runTaskLater(Main.plugin, 1);
	}
}
