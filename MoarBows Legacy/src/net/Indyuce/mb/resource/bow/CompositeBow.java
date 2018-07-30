package net.Indyuce.mb.resource.bow;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;
import net.Indyuce.mb.util.Utils;
import net.Indyuce.mb.util.VersionUtils;

public class CompositeBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		final double dmg = Main.bows.getDouble("COMPOSITE_BOW.damage");
		e.setCancelled(true);
		if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
			return false;

		VersionUtils.sound(p.getLocation(), "ENTITY_ARROW_SHOOT", 2, 0);
		new BukkitRunnable() {
			Location loc = p.getEyeLocation();
			double ti = 0;
			double max = 20 * e.getForce();
			Vector v = p.getEyeLocation().getDirection().multiply(1.25);

			public void run() {
				for (double j = 0; j < 3; j++) {
					ti += .5;
					loc.add(v);
					Eff.CRIT.display(.1f, .1f, .1f, .1f, 8, loc, 100);
					VersionUtils.sound(loc, "BLOCK_NOTE_HAT", 3, 2);
					for (LivingEntity t : loc.getWorld().getEntitiesByClass(LivingEntity.class))
						if (Utils.canDmgEntity(p, loc, t) && t != p) {
							VersionUtils.sound(t.getLocation(), "ENTITY_FIREWORK_BLAST", 3, 0);
							Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, t.getLocation().add(0, 1, 0), 100);
							cancel();
							t.damage(dmg);
							return;
						}
				}
				if (ti >= max)
					cancel();
			}
		}.runTaskTimer(Main.plugin, 0, 1);
		return false;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
	}

	@Override
	public void land(Arrow a) {
	}
}
