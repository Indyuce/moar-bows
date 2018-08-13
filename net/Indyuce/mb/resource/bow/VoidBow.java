package net.Indyuce.mb.resource.bow;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.api.SpecialBow;
import net.Indyuce.mb.util.VersionUtils;

public class VoidBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		explode(a);
	}

	@Override
	public void land(Arrow a) {
		explode(a);
	}
	
	private void explode(Arrow a) {
		a.remove();
		Location loc = a.getLocation();
		loc.setPitch(((Player) a.getShooter()).getLocation().getPitch());
		loc.setYaw(((Player) a.getShooter()).getLocation().getYaw());
		((Player) a.getShooter()).teleport(loc);
		Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, loc, 200);
		VersionUtils.sound(a.getLocation(), "ENTITY_ENDERMEN_TELEPORT", 3, 1);
	}
}
