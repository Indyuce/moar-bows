package net.Indyuce.moarbows.bow;

import org.bukkit.EntityEffect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Shocking_Bow extends MoarBow {
	public Shocking_Bow() {
		super(new String[] { "Fires enchanted arrows", "that shock your targets." }, 0, 0, "smoke_normal", new String[] { "FLINT,FLINT,FLINT", "FLINT,BOW,FLINT", "FLINT,FLINT,FLINT" });

		addModifier(new BowModifier("duration", 2));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		int duration = (int) Math.min(300, getValue("duration") * 10);
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				ti++;
				if (ti >= duration)
					cancel();
				p.playEffect(EntityEffect.HURT);
			}
		}.runTaskTimer(MoarBows.plugin, 0, 2);
	}
}
