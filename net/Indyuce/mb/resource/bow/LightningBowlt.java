package net.Indyuce.mb.resource.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.resource.SpecialBow;
import net.Indyuce.mb.resource.TaskState;

public class LightningBowlt implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		a.remove();
		a.getWorld().strikeLightning(a.getLocation());
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		a.remove();
		a.getWorld().strikeLightning(a.getLocation());
		return TaskState.CONTINUE;
	}
}
