package me.Indyuce.mb.resource.bow;

import org.bukkit.Color;
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

import me.Indyuce.mb.ConfigData;
import me.Indyuce.mb.Eff;
import me.Indyuce.mb.Main;
import me.Indyuce.mb.resource.SpecialBow;
import me.Indyuce.mb.resource.TaskState;
import me.Indyuce.mb.util.Utils;
import me.Indyuce.mb.util.VersionUtils;

public class LinearBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		FileConfiguration config = ConfigData.getCD(Main.plugin, "", "bows");
		double dmg = config.getDouble("LINEAR_BOW.damage");
		e.setCancelled(true);
		if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
			return TaskState.BREAK;

		VersionUtils.sound(p.getLocation(), "ENTITY_ARROW_SHOOT", 2, 0);
		int range = (int) (56 * e.getForce());
		Location loc = p.getEyeLocation();
		for (double j = 0; j < range; j++) {
			loc.add(p.getEyeLocation().getDirection());
			Eff.REDSTONE.display(new Eff.OrdinaryColor(Color.SILVER), loc, 200);
			if (loc.getBlock().getType().isSolid())
				break;
			for (Entity t : p.getNearbyEntities(30, 30, 30)) {
				if (Utils.canDmgEntity(p, loc, t) && t instanceof LivingEntity) {
					e.setCancelled(true);
					((LivingEntity) t).damage(dmg);
					Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, t.getLocation().add(0, 1, 0), 100);
					break;
				}
			}
		}
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		return TaskState.CONTINUE;
	}
}
