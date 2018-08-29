package net.Indyuce.mb.bow;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.api.MoarBow;
import net.Indyuce.mb.util.VersionUtils;

public class Void_Bow extends MoarBow {
	public Void_Bow() {
		super(new String[] { "Its arrows teleport you", "to where they land." }, 0, 5, "redstone:128,0,128", new String[] { "AIR,ENDER_PEARL,AIR", "ENDER_PEARL,BOW,ENDER_PEARL", "AIR,ENDER_PEARL,AIR" });
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(t, a);
	}

	@Override
	public void land(Player p, Arrow a) {
		a.remove();
		Location loc = a.getLocation();
		loc.setPitch(((Player) a.getShooter()).getLocation().getPitch());
		loc.setYaw(((Player) a.getShooter()).getLocation().getYaw());
		((Player) a.getShooter()).teleport(loc);
		Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, loc, 200);
		VersionUtils.sound(a.getLocation(), "ENTITY_ENDERMEN_TELEPORT", 3, 1);
	}
}
