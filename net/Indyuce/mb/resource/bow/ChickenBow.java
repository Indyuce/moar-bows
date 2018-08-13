package net.Indyuce.mb.resource.bow;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
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

public class ChickenBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		e.setCancelled(true);
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				ti++;
				if (ti > 20 * e.getForce())
					cancel();
				
				if (!Utils.consumeAmmo(p, new ItemStack(Material.EGG)))
					return;

				Location loc = p.getEyeLocation().clone();
				Eff.CRIT.display(.2f, .2f, .2f, 0, 6, loc, 100);
				VersionUtils.sound(p.getLocation(), "ENTITY_EGG_THROW", 1, 1.5f);
				Egg egg = p.launchProjectile(Egg.class);
				loc.setPitch(loc.getPitch() + new Random().nextInt(3) - 1);
				loc.setYaw(loc.getYaw() + new Random().nextInt(3) - 1);
				Vector v = loc.getDirection().multiply(3.3 * e.getForce());
				egg.setVelocity(v);
			}
		}.runTaskTimer(Main.plugin, 0, 2);
		return false;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
	}

	@Override
	public void land(Arrow a) {
	}
}
