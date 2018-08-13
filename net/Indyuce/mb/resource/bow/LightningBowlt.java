package net.Indyuce.mb.resource.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.api.SpecialBow;

public class LightningBowlt implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		a.remove();
		a.getWorld().strikeLightning(a.getLocation());
	}

	@Override
	public void land(Arrow a) {
		a.remove();
		a.getWorld().strikeLightning(a.getLocation());
	}
}
