package net.Indyuce.mb.resource.bow;

import org.bukkit.EntityEffect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.mb.ConfigData;
import net.Indyuce.mb.Main;
import net.Indyuce.mb.resource.SpecialBow;
import net.Indyuce.mb.resource.TaskState;

public class ShockingBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		FileConfiguration config = ConfigData.getCD(Main.plugin, "", "bows");
		int duration = config.getInt("SHOCKING_BOW.duration");
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				ti++;
				if (ti >= (duration * 10 > 300 ? 300 : duration * 10))
					cancel();
				p.playEffect(EntityEffect.HURT);
			}
		}.runTaskTimer(Main.plugin, 0, 2);
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		return TaskState.CONTINUE;
	}
}
