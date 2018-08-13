package net.Indyuce.mb.resource.bow;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;

public class SpartanBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		explode(a);
	}

	@Override
	public void land(Arrow a) {
		explode(a);
	}
	
	private void explode(Arrow a) {
		a.remove();
		final Player p = a.getShooter() != null ? (Player) a.getShooter() : null;
		Random r = new Random();
		final Location loc1 = a.getLocation().clone();
		double random = Math.PI * 4 * (r.nextDouble() - .5);
		Location sky = a.getLocation().clone().add(Math.cos(random) * 6, 13, Math.sin(random) * 6);
		final double duration = Main.bows.getDouble("SPARTAN_BOW.duration");
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				ti += 3d / 20d;
				Eff.SMOKE_LARGE.display(0, 0, 0, 0, 1, sky, 100);
				Location loc2 = loc1.clone().add(8 * (r.nextDouble() - .5), 0, 8 * (r.nextDouble() - .5));
				Vector v = loc2.toVector().subtract(sky.toVector()).normalize();
				Arrow a1 = (Arrow) sky.getWorld().spawnEntity(sky, EntityType.ARROW);
				if (p != null) {
					EntityShootBowEvent event = new EntityShootBowEvent(p, new ItemStack(Material.BOW), a, 1);
					Main.plugin.getServer().getPluginManager().callEvent(event);
				}
				a1.setVelocity(v);

				if (ti > duration)
					cancel();
			}
		}.runTaskTimer(Main.plugin, 0, 3);
	}
}
