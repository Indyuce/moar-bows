package net.Indyuce.mb.resource.bow;

import org.bukkit.EntityEffect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;

public class ShockingBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		int duration = Main.bows.getInt("SHOCKING_BOW.duration");
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				ti++;
				if (ti >= (duration * 10 > 300 ? 300 : duration * 10))
					cancel();
				p.playEffect(EntityEffect.HURT);
			}
		}.runTaskTimer(Main.plugin, 0, 2);
	}

	@Override
	public void land(Arrow a) {
	}
}
