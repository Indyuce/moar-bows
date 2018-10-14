package net.Indyuce.moarbows.listener;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.ParticleEffect;
import net.Indyuce.moarbows.api.MoarBow;

public class HandParticles {
	private static int amount = MoarBows.plugin.getConfig().getInt("hands-particles.amount");
	private static long delay = MoarBows.plugin.getConfig().getLong("hand-particles.delay");

	public HandParticles() {
		if (!MoarBows.plugin.getConfig().getBoolean("hand-particles"))
			return;

		new BukkitRunnable() {
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers())
					loopHandParticles(player);
			}
		}.runTaskTimer(MoarBows.plugin, 0, delay);
	}

	private void loopHandParticles(Player player) {
		ItemStack[] items = BowUtils.getHandItems(player);
		for (int j = 0; j < items.length; j++) {
			ItemStack item = items[j];
			if (!BowUtils.isPluginItem(item, false))
				continue;

			MoarBow bow = MoarBows.getFromItem(item);
			if (bow == null)
				continue;

			String eff = bow.getParticleEffect();
			if (eff == null || eff.equals(""))
				continue;

			Location loc = player.getLocation().clone().add(0, .8, 0);
			loc.setYaw(player.getLocation().getYaw());
			loc.add(loc.getDirection().multiply(.2));
			loc.setPitch(0);
			loc.setYaw(player.getLocation().getYaw() + 90 - (j * 180));
			loc.add(loc.getDirection().multiply(.3));
			String[] s = eff.split(":");
			ParticleEffect effName = null;
			try {
				effName = ParticleEffect.valueOf(s[0].toUpperCase());
			} catch (Exception e) {
				continue;
			}

			if (s.length == 1)
				effName.display(.1f, .1f, .1f, 0, amount, loc, 200);

			if (s.length == 2)
				for (int j1 = 0; j1 < amount; j1++) {
					loc.add(new Random().nextDouble() / 2.5 - .2, new Random().nextDouble() / 2.5 - .2, new Random().nextDouble() / 2.5 - .2);
					String[] rgb = s[1].split(",");
					Color c = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
					effName.display(new ParticleEffect.OrdinaryColor(c), loc, 200);
				}
		}
	}
}
