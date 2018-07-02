package net.Indyuce.mb.resource.bow;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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

public class FireBow implements SpecialBow {
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
		int duration = config.getInt("FIRE_BOW.duration") * 20;
		int maxTicks = config.getInt("FIRE_BOW.maxBurningTime") * 20;
		a.remove();
		Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, a.getLocation(), 200);
		Eff.LAVA.display(0, 0, 0, 0, 12, a.getLocation(), 200);
		Eff.FLAME.display(0, 0, 0, .13f, 48, a.getLocation().add(0, .1, 0), 200);
		VersionUtils.sound(a.getLocation(), "ENTITY_FIREWORK_LARGE_BLAST", 3, 1);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				int ticks = ent.getFireTicks() + duration;
				ticks = (ticks > maxTicks ? maxTicks : ticks);
				ent.setFireTicks(ticks);
			}
	}
}
