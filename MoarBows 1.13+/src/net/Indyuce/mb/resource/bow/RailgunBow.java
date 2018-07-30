package net.Indyuce.mb.resource.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;

public class RailgunBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		if (p.getVehicle() != null)
			if (p.getVehicle().getType() == EntityType.MINECART)
				return true;
		return false;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(a);
	}

	@Override
	public void land(Arrow a) {
		int radius = Main.bows.getInt("RAILGUN_BOW.radius");
		a.remove();
		a.getWorld().createExplosion(a.getLocation(), radius);
	}
}
