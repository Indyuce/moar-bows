package net.Indyuce.moarbows.bow;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.Eff;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.MoarBow;

public class Snow_Bow extends MoarBow {
	public Snow_Bow() {
		super(new String[] { "Shoots a few snowballs.", "The number depends on", "the bow pull force." }, 0, 2.0, "snow_shovel", new String[] { "SNOW_BLOCK,SNOW_BLOCK,SNOW_BLOCK", "SNOW_BLOCK,BOW,SNOW_BLOCK", "SNOW_BLOCK,SNOW_BLOCK,SNOW_BLOCK" });
	}

	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		e.setCancelled(true);
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				ti++;
				if (ti > 20 * e.getForce())
					cancel();

				Location loc = p.getEyeLocation().clone();
				Eff.SNOWBALL.display(.2f, .2f, .2f, 0, 6, loc, 100);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1, 1.5f);
				Snowball s = p.launchProjectile(Snowball.class);
				loc.setPitch(loc.getPitch() + new Random().nextInt(3) - 1);
				loc.setYaw(loc.getYaw() + new Random().nextInt(3) - 1);
				Vector v = loc.getDirection().multiply(3.3 * e.getForce());
				s.setVelocity(v);
			}
		}.runTaskTimer(MoarBows.plugin, 0, 2);
		return false;
	}
}
