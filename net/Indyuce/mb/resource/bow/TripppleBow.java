package net.Indyuce.mb.resource.bow;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.api.SpecialBow;
import net.Indyuce.mb.util.Utils;
import net.Indyuce.mb.util.VersionUtils;

public class TripppleBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		e.setCancelled(true);
		VersionUtils.sound(p.getLocation(), "ENTITY_ARROW_SHOOT", 2, 1);
		Location loc = p.getLocation().add(0, 1.2, 0);
		for (int j = -1; j < 2; j++) {
			if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
				return false;

			if (p.getGameMode() != GameMode.CREATIVE)
				p.getInventory().removeItem(new ItemStack(Material.ARROW));
			loc.setYaw(p.getLocation().getYaw() + j);
			p.launchProjectile(Arrow.class).setVelocity(loc.getDirection().multiply(e.getForce() * 3.3));
		}
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
	}

	@Override
	public void land(Arrow a) {
	}
}
