package me.Indyuce.mb.resource.bow;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import me.Indyuce.mb.Main;
import me.Indyuce.mb.resource.SpecialBow;
import me.Indyuce.mb.resource.TaskState;
import me.Indyuce.mb.util.Utils;
import me.Indyuce.mb.util.VersionUtils;

public class TripppleBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		e.setCancelled(true);
		for (int j = -1; j < 2; j++) {
			if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
				return TaskState.BREAK;

			if (p.getGameMode() != GameMode.CREATIVE)
				p.getInventory().removeItem(new ItemStack(Material.ARROW));
			VersionUtils.sound(p.getLocation(), "ENTITY_ARROW_SHOOT", 2, 1);
			Location loc = p.getLocation().add(0, 1.2, 0);
			loc.setYaw(p.getLocation().getYaw() + j);
			Arrow a1 = p.launchProjectile(Arrow.class);
			EntityShootBowEvent event = new EntityShootBowEvent(p, Utils.removeDisplayName(i), a1, e.getForce());
			Main.plugin.getServer().getPluginManager().callEvent(event);
			a1.setVelocity(loc.getDirection().multiply(e.getForce() * 3.3));
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
