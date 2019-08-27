package net.Indyuce.moarbows.bow;

import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.LinearValue;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;

public class Shocking_Bow extends MoarBow {
	public Shocking_Bow() {
		super(new String[] { "Fires enchanted arrows", "that shock your targets." }, 0,  "smoke_normal", new String[] { "FLINT,FLINT,FLINT", "FLINT,BOW,FLINT", "FLINT,FLINT,FLINT" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(5, -1, 1, 5)), new DoubleModifier("duration", new LinearValue(2, 1)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		return true;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
		int duration = (int) Math.min(300, data.getDouble("duration") * 10);
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				if (ti++ >= duration)
					cancel();
				target.playEffect(EntityEffect.HURT);
			}
		}.runTaskTimer(MoarBows.plugin, 0, 2);
	}

	@Override
	public void whenLand(ArrowData data) {
	}
}
