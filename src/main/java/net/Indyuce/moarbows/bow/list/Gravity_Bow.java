package net.Indyuce.moarbows.bow.list;

import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.bow.ArrowData;
import net.Indyuce.moarbows.bow.MoarBow;
import net.Indyuce.moarbows.bow.modifier.DoubleModifier;
import net.Indyuce.moarbows.bow.particle.ParticleData;
import net.Indyuce.moarbows.util.LinearFormula;

public class Gravity_Bow extends MoarBow {
	public Gravity_Bow() {
		super(new String[] { "Shoots arrows that attract", "your target to yourself." }, new ParticleData(Particle.SPELL_INSTANT),
				new String[] { "AIR,FISHING_ROD,AIR", "AIR,BOW,AIR", "AIR,AIR,AIR" });

		addModifier(new DoubleModifier("cooldown", new LinearFormula(0, 0)), new DoubleModifier("force", new LinearFormula(2.5, .5)),
				new DoubleModifier("y-static", new LinearFormula(.3, .05)));
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
