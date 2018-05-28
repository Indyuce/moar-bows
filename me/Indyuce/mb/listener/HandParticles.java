package me.Indyuce.mb.listener;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Indyuce.mb.Eff;
import me.Indyuce.mb.Main;
import me.Indyuce.mb.resource.Bow;
import me.Indyuce.mb.util.Utils;
import me.Indyuce.mb.util.VersionUtils;

public class HandParticles {
	int parsPerSec = 1;

	public HandParticles() {
		if (Main.plugin.getConfig().getBoolean("reduce-hand-particles"))
			parsPerSec = 8;
		if (Main.plugin.getConfig().getBoolean("bows-hand-particles")) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
				public void run() {
					for (Player p : Bukkit.getOnlinePlayers())
						loopHandParticles(p);
				}
			}, 0, (Main.plugin.getConfig().getBoolean("reduce-hand-particles") ? 20 : 2));
		}
	}

	public void loopHandParticles(Player p) {
		ItemStack[] items = VersionUtils.getItemsInHand(p);
		for (int j = 0; j < items.length; j++) {
			ItemStack i = items[j];
			if (!Utils.isPluginItem(i, false))
				continue;
			
			Bow b = Utils.getBow(i);
			if (b == null)
				return;

			Location loc = p.getLocation().clone().add(0, .8, 0);
			loc.setYaw(p.getLocation().getYaw());
			loc.add(loc.getDirection().multiply(.2));
			loc.setPitch(0);
			loc.setYaw(p.getLocation().getYaw() + 90 - (j * 180));
			loc.add(loc.getDirection().multiply(.3));
			String eff = b.eff;
			String[] s = eff.split(":");
			Eff eff_name = null;
			try {
				eff_name = Eff.valueOf(s[0].toUpperCase());
			} catch (Exception e) {
				Utils.bowInHandError(b, p);
				break;
			}
			if (s.length == 1)
				eff_name.display(.1f, .1f, .1f, 0, parsPerSec, loc, 200);
			if (s.length == 2)
				for (int j1 = 0; j1 < parsPerSec; j1++) {
					loc.add(new Random().nextDouble() / 2.5 - .2, new Random().nextDouble() / 2.5 - .2, new Random().nextDouble() / 2.5 - .2);
					String[] rgb = s[1].split(",");
					Color c = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
					eff_name.display(new Eff.OrdinaryColor(c), loc, 200);
				}
		}
	}
}
