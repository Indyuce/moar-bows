package net.Indyuce.mb.resource.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;

public class GravityBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		double force = Main.bows.getDouble("GRAVITY_BOW.force");
		double ystatic = Main.bows.getDouble("GRAVITY_BOW.y-static");
		new BukkitRunnable() {
			public void run() {
				Vector v = t.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
				v.setX(v.getX() * force);
				v.setY(ystatic);
				v.setZ(v.getZ() * force);
				p.setVelocity(v);
			}
		}.runTaskLater(Main.plugin, 1);
	}

	@Override
	public void land(Arrow a) {
	}
}
