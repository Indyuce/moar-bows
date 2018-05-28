package me.Indyuce.mb.resource.bow;

import org.bukkit.Effect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import me.Indyuce.mb.ConfigData;
import me.Indyuce.mb.Main;
import me.Indyuce.mb.resource.SpecialBow;
import me.Indyuce.mb.resource.TaskState;
import me.Indyuce.mb.util.Utils;

public class HunterBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		if (!Utils.isFriendly(p))
			return TaskState.CONTINUE;

		FileConfiguration config = ConfigData.getCD(Main.plugin, "", "bows");
		e.setDamage(e.getDamage() * (1 + config.getDouble("Hunter Bow.damage_percent") / 100));
		p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 55);
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		return TaskState.CONTINUE;
	}
}
