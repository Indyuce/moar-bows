package net.Indyuce.mb.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.MoarBow;
import net.Indyuce.mb.comp.CUtils;
import net.Indyuce.mb.resource.CustomFlag;
import net.Indyuce.mb.util.Utils;

public class ShootBow implements Listener {
	public static HashMap<UUID, MoarBow> type = new HashMap<UUID, MoarBow>();

	@EventHandler
	public void a(EntityShootBowEvent e) {
		if (e.getForce() < 1.0 && Main.plugin.getConfig().getBoolean("full-pull-restriction"))
			return;

		ItemStack i = e.getBow();
		Entity t = e.getProjectile();
		if (!(t instanceof Arrow) || !(e.getEntity() instanceof Player))
			return;
		Player p = (Player) e.getEntity();

		// check for bow
		if (!Utils.isPluginItem(i, false))
			return;
		MoarBow b = MoarBow.get(i);
		if (b == null)
			return;

		// permission
		if (!p.hasPermission("moarbows.use." + b.getID().toLowerCase().replace("'", "")))
			return;

		// worldguard flag
		if (!CUtils.boolFlag(p, CustomFlag.MB_BOWS)) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.YELLOW + Utils.msg("disable-bows-flag"));
			return;
		}

		// cooldown
		if (!Utils.canUseBow(p, b, e)) {
			e.setCancelled(true);
			return;
		}

		// add to hashmap + shoot effect
		if (!b.getBowClass().shoot(e, (Arrow) t, p, i))
			return;
		type.put(t.getUniqueId(), b);

		// arrow particles
		if (Main.plugin.getConfig().getBoolean("bows-arrow-particles")) {
			new BukkitRunnable() {
				public void run() {
					if (t == null || t.isDead() || t.isOnGround()) {
						cancel();
						return;
					}

					String eff = b.getParticleEffect();
					if (eff == null || eff.equals("")) {
						cancel();
						return;
					}

					String[] s = eff.split(":");
					Eff effName = null;
					try {
						effName = Eff.valueOf(s[0].toUpperCase());
					} catch (Exception e) {
						type.remove(t.getUniqueId());
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error with " + b.getName() + ". Effect was not recognized.");
						cancel();
						return;
					}
					if (s.length == 1)
						effName.display(0, 0, 0, 0, 1, t.getLocation().clone().add(0, .25, 0), 200);
					if (s.length == 2) {
						String[] rgb = s[1].split(",");
						Color c = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
						effName.display(new Eff.OrdinaryColor(c), t.getLocation().clone().add(0, .25, 0), 200);
					}
				}
			}.runTaskTimer(Main.plugin, 0, 1);
		}
	}
}