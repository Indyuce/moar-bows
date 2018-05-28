package me.Indyuce.mb.resource.bow;

import org.bukkit.Effect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import me.Indyuce.mb.ConfigData;
import me.Indyuce.mb.Main;
import me.Indyuce.mb.resource.SpecialBow;
import me.Indyuce.mb.resource.TaskState;

public class SilverBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		if (!(t instanceof LivingEntity))
			return TaskState.CONTINUE;
			
		FileConfiguration config = ConfigData.getCD(Main.plugin, "", "bows");
		int id = config.getInt("SILVER_BOW.blockEffectID");
		p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, id);
		p.getWorld().playEffect(p.getLocation().add(0, 1, 0), Effect.STEP_SOUND, id);
		e.setDamage(e.getDamage() * (1. + config.getDouble("SILVER_BOW.damagePercent") / 100.));
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		return TaskState.CONTINUE;
	}
}
