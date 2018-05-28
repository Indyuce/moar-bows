package me.Indyuce.mb.resource.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import me.Indyuce.mb.resource.SpecialBow;
import me.Indyuce.mb.resource.TaskState;

public class GravityBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		Player pl = (Player) a.getShooter();
		p.setVelocity(pl.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().setY(.6).multiply(5));
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		return TaskState.CONTINUE;
	}
}
