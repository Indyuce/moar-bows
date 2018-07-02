package net.Indyuce.mb.resource.bow;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.mb.ConfigData;
import net.Indyuce.mb.FEP;
import net.Indyuce.mb.Main;
import net.Indyuce.mb.resource.SpecialBow;
import net.Indyuce.mb.resource.TaskState;

public class FlareBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		new BukkitRunnable() {
			public void run() {
				if (!a.isDead())
					explode(a);
			}
		}.runTaskLater(Main.plugin, 40);
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		explode(a);
		return TaskState.CONTINUE;
	}

	private static void explode(Arrow a) {
		a.remove();
		for (int x = -2; x < 3; x += 2) {
			for (int y = -2; y < 3; y += 2) {
				for (int z = -2; z < 3; z += 2) {
					Location loc = a.getLocation().clone();
					loc.add(x * 3 + ((new Random().nextDouble() - .5) * 2), y * 3 + ((new Random().nextDouble() - .5) * 2), z * 3 + ((new Random().nextDouble() - .5) * 2));
					int r = 0;
					int g = 255;
					int b = 0;
					FileConfiguration config = ConfigData.getCD(Main.plugin, "", "bows");
					if (config.getBoolean("Flare Bow.multicolor")) {
						r = new Random().nextInt(255);
						g = new Random().nextInt(255);
						b = new Random().nextInt(255);
					}
					FireworkEffect fe = FireworkEffect.builder().withColor(Color.fromBGR(r, g, b)).with(FireworkEffect.Type.BALL_LARGE).build();
					try {
						new FEP().playFirework(loc, fe);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
