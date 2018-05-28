package me.Indyuce.mb.resource.bow;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Indyuce.mb.ConfigData;
import me.Indyuce.mb.Eff;
import me.Indyuce.mb.Main;
import me.Indyuce.mb.resource.SpecialBow;
import me.Indyuce.mb.resource.TaskState;
import me.Indyuce.mb.util.VersionUtils;

public class CorrosiveBow implements SpecialBow {
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
		int duration = config.getInt("CORROSIVE_BOW.duration") * 20;
		a.remove();
		Eff.SLIME.display(2, 2, 2, 0, 48, a.getLocation(), 200);
		Eff.VILLAGER_HAPPY.display(2, 2, 2, 0, 32, a.getLocation(), 200);
		VersionUtils.sound(a.getLocation(), "BLOCK_BREWING_STAND_BREW", 3, 0);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				((LivingEntity) ent).removePotionEffect(PotionEffectType.POISON);
				((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, 1));
			}
	}
}
