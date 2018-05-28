package me.Indyuce.mb.resource.bow;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import me.Indyuce.mb.resource.SpecialBow;
import me.Indyuce.mb.resource.TaskState;
import me.Indyuce.mb.util.Utils;
import me.Indyuce.mb.util.VersionUtils;

public class WitherBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		e.setCancelled(true);
		if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
			return TaskState.BREAK;

		VersionUtils.sound(p.getLocation(), "ENTITY_WITHER_SHOOT", 1, 1);
		WitherSkull ws = p.launchProjectile(WitherSkull.class);
		ws.setVelocity(p.getEyeLocation().getDirection().multiply(3.3 * e.getForce()));
		return TaskState.BREAK;
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
