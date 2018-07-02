package net.Indyuce.mb.resource;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public interface SpecialBow {
	// when shooting an arrow with the bow
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i);

	// when hitting an entity
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj);
	
	// when landing on the ground
	public TaskState land(Arrow a);
}
