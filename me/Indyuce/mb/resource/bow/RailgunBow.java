package me.Indyuce.mb.resource.bow;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import me.Indyuce.mb.ConfigData;
import me.Indyuce.mb.Main;
import me.Indyuce.mb.resource.SpecialBow;
import me.Indyuce.mb.resource.TaskState;

public class RailgunBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		if (p.getVehicle() == null)
			return TaskState.BREAK;
		if (p.getVehicle().getType() != EntityType.MINECART)
			return TaskState.BREAK;
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		FileConfiguration config = ConfigData.getCD(Main.plugin, "", "bows");
		int radius = config.getInt("RAILGUN_BOW.radius");
		a.remove();
		a.getWorld().createExplosion(a.getLocation(), radius);
		return TaskState.CONTINUE;
	}
}
