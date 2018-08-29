package net.Indyuce.mb.bow;

import org.bukkit.EntityEffect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.mb.MoarBows;
import net.Indyuce.mb.api.BowModifier;
import net.Indyuce.mb.api.MoarBow;

public class Shocking_Bow extends MoarBow {
	public Shocking_Bow() {
		super(new String[] { "Fires enchanted arrows", "that shock your targets." }, 0, 0, "smoke_normal", new String[] { "FLINT,FLINT,FLINT", "FLINT,BOW,FLINT", "FLINT,FLINT,FLINT" });

		addModifier(new BowModifier("duration", 2));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		int duration = MoarBows.bows.getInt("SHOCKING_BOW.duration");
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				ti++;
				if (ti >= (duration * 10 > 300 ? 300 : duration * 10))
					cancel();
				p.playEffect(EntityEffect.HURT);
			}
		}.runTaskTimer(MoarBows.plugin, 0, 2);
	}
}
