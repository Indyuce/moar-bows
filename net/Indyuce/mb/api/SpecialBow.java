package net.Indyuce.mb.api;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public interface SpecialBow {
	// when shooting an arrow with the bow
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i);

	// when hitting an entity
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t);
	
	// when landing on the ground
	public void land(Arrow a);
}
