package net.Indyuce.moarbows.bow;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.Eff;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.comp.version.VersionSound;
import net.Indyuce.moarbows.util.Utils;

public class Chicken_Bow extends MoarBow {
	public Chicken_Bow() {
		super(new String[] { "Shoots a few eggs. The number", "depends on the bow pull force." }, 0, 3.0, "redstone:240,230,140", new String[] { "EGG,EGG,EGG", "EGG,BOW,EGG", "EGG,EGG,EGG" });
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

				if (!Utils.consumeAmmo(p, new ItemStack(Material.EGG)))
					return;

				Location loc = p.getEyeLocation().clone();
				Eff.CRIT.display(.2f, .2f, .2f, 0, 6, loc, 100);
				loc.getWorld().playSound(loc, VersionSound.ENTITY_CHICKEN_EGG.getSound(), 1, 1.5f);
				Egg egg = p.launchProjectile(Egg.class);
				loc.setPitch(loc.getPitch() + new Random().nextInt(3) - 1);
				loc.setYaw(loc.getYaw() + new Random().nextInt(3) - 1);
				Vector v = loc.getDirection().multiply(3.3 * e.getForce());
				egg.setVelocity(v);
			}
		}.runTaskTimer(MoarBows.plugin, 0, 2);
		return false;
	}
}
