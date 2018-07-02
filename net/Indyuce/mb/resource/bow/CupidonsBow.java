package net.Indyuce.mb.resource.bow;

import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.ConfigData;
import net.Indyuce.mb.Eff;
import net.Indyuce.mb.Main;
import net.Indyuce.mb.resource.SpecialBow;
import net.Indyuce.mb.resource.TaskState;
import net.Indyuce.mb.util.VersionUtils;

public class CupidonsBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		if (p.getType() != EntityType.PLAYER)
			return TaskState.CONTINUE;

		FileConfiguration config = ConfigData.getCD(Main.plugin, "", "bows");
		e.setDamage(0);
		Eff.HEART.display(1, 1, 1, 0, 16, p.getLocation().add(0, 1, 0), 200);
		VersionUtils.sound(p.getLocation(), "ENTITY_BLAZE_AMBIENT", 2, 2);
		MarkedBow.marked.remove(p.getUniqueId());
		double max = ((Player) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
		double health = ((Player) p).getHealth();
		double heal = config.getDouble("CUPIDONS_BOW.heal");
		if (health + heal > max) {
			((Player) p).setHealth(max);
			return TaskState.CONTINUE;
		}
		((Player) p).setHealth(health + heal);
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		return TaskState.CONTINUE;
	}
}
