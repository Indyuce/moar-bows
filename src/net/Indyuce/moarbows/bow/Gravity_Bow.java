package net.Indyuce.moarbows.bow;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.util.LinearValue;

public class Gravity_Bow extends MoarBow {
	public Gravity_Bow() {
		super(new String[] { "Shoots arrows that attract", "your target to yourself." }, 0, "spell_instant", new String[] { "AIR,FISHING_ROD,AIR", "AIR,BOW,AIR", "AIR,AIR,AIR" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(0, 0)), new DoubleModifier("force", new LinearValue(2.5, .5)), new DoubleModifier("y-static", new LinearValue(.3, .05)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		return true;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
		double force = data.getDouble("force");
		double ystatic = data.getDouble("y-static");
		new BukkitRunnable() {
			public void run() {
				Vector v = data.getShooter().getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
				v.setX(v.getX() * force);
				v.setY(ystatic);
				v.setZ(v.getZ() * force);

				target.setVelocity(v);
			}
		}.runTaskLater(MoarBows.plugin, 1);
	}

	@Override
	public void whenLand(ArrowData data) {
	}
}
