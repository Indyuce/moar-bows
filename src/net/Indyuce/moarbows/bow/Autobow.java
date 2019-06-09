package net.Indyuce.moarbows.bow;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.MoarBow;

public class Autobow extends MoarBow {
	public Autobow() {
		super(new String[] { "Shoots a flurry of arrows.", "The number depends on the", "bow pull force." }, 0, 8.0, "crit", new String[] { "BOW,BOW,BOW", "BOW,NETHER_STAR,BOW", "BOW,BOW,BOW" });
	}

	@Override
	public boolean shoot(EntityShootBowEvent event, Arrow arrow, Player player, ItemStack item) {
		event.setCancelled(true);
		new BukkitRunnable() {
			double ti = 0;

			public void run() {
				ti++;
				if (ti > 20 * event.getForce())
					cancel();

				if (!BowUtils.consumeAmmo(player, new ItemStack(Material.ARROW)))
					return;

				Location loc = player.getEyeLocation().clone();
				loc.getWorld().spawnParticle(Particle.CRIT, loc, 6, .2, .2, .2, 0);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1.5f);
				loc.setPitch(loc.getPitch() + random.nextInt(3) - 1);
				loc.setYaw(loc.getYaw() + random.nextInt(3) - 1);
				player.launchProjectile(Arrow.class).setVelocity(loc.getDirection().multiply(3.3 * event.getForce()));
			}
		}.runTaskTimer(MoarBows.plugin, 0, 2);
		return false;
	}
}
