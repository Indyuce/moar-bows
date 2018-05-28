package me.Indyuce.mb.resource.bow;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import me.Indyuce.mb.ConfigData;
import me.Indyuce.mb.Eff;
import me.Indyuce.mb.Main;
import me.Indyuce.mb.resource.SpecialBow;
import me.Indyuce.mb.resource.TaskState;
import me.Indyuce.mb.util.VersionUtils;

public class ExplosiveBow implements SpecialBow {
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

	private static void explode(Arrow a) {
		FileConfiguration config = ConfigData.getCD(Main.plugin, "", "bows");
		double dmg = config.getDouble("EXPLOSIVE_BOW.damage");
		a.remove();
		Eff.EXPLOSION_LARGE.display(2, 2, 2, 0, 8, a.getLocation(), 200);
		VersionUtils.sound(a.getLocation(), "ENTITY_GENERIC_EXPLODE", 3, 1);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity)
				((LivingEntity) ent).damage(dmg);
	}
}
