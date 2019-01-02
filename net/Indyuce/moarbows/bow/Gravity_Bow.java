package net.Indyuce.moarbows.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Gravity_Bow extends MoarBow {
	public Gravity_Bow() {
		super(new String[] { "Shoots arrows that attract", "your target to yourself." }, 0, 0, "spell_instant", new String[] { "AIR,FISHING_ROD,AIR", "AIR,BOW,AIR", "AIR,AIR,AIR" });

		addModifier(new BowModifier("force", 2.5), new BowModifier("y-static", .3));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		double force = getValue("force");
		double ystatic = getValue("y-static");
		new BukkitRunnable() {
			public void run() {
				Vector v = t.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
				v.setX(v.getX() * force);
				v.setY(ystatic);
				v.setZ(v.getZ() * force);
				p.setVelocity(v);
			}
		}.runTaskLater(MoarBows.plugin, 1);
	}
}
