package net.Indyuce.mb.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.MoarBows;
import net.Indyuce.mb.api.ArrowManager;
import net.Indyuce.mb.api.MoarBow;
import net.Indyuce.mb.comp.worldguard.CustomFlag;
import net.Indyuce.mb.util.Utils;

public class ShootBow implements Listener {
	@EventHandler
	public void a(EntityShootBowEvent e) {
		if (e.getForce() < 1.0 && MoarBows.plugin.getConfig().getBoolean("full-pull-restriction"))
			return;

		ItemStack i = e.getBow();
		if (!(e.getProjectile() instanceof Arrow) || !(e.getEntity() instanceof Player))
			return;

		Player p = (Player) e.getEntity();
		if (!Utils.isPluginItem(i, false))
			return;
		
		// check for bow
		MoarBow bow = MoarBow.get(i);
		if (bow == null)
			return;

		// permission
		if (!p.hasPermission("moarbows.use." + bow.getLowerCaseID()))
			return;

		// worldguard flag
		if (!MoarBows.wgPlugin.isFlagAllowed(p, CustomFlag.MB_BOWS)) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.YELLOW + Utils.msg("disable-bows-flag"));
			return;
		}

		// cooldown
		if (!Utils.canUseBow(p, bow, e)) {
			e.setCancelled(true);
			return;
		}

		// shoot effect
		Arrow arrow = (Arrow) e.getProjectile();
		if (!bow.shoot(e, arrow, p, i)) {
			e.setCancelled(true);
			return;
		}
		
		// register arrow
		ArrowManager.registerArrow(arrow, bow, p);

		// arrow particles
		if (MoarBows.plugin.getConfig().getBoolean("bows-arrow-particles")) {
			new BukkitRunnable() {
				public void run() {
					if (arrow == null || arrow.isDead() || arrow.isOnGround()) {
						cancel();
						return;
					}

					String eff = bow.getParticleEffect();
					if (eff == null || eff.equals("")) {
						cancel();
						return;
					}

					String[] s = eff.split(":");
					Eff effName = null;
					try {
						effName = Eff.valueOf(s[0].toUpperCase());
					} catch (Exception e) {
						ArrowManager.unregisterArrow(arrow);
						Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error with " + bow.getID() + ". Effect was not recognized.");
						cancel();
						return;
					}
					if (s.length == 1)
						effName.display(0, 0, 0, 0, 1, arrow.getLocation().clone().add(0, .25, 0), 200);
					if (s.length == 2) {
						String[] rgb = s[1].split(",");
						Color c = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
						effName.display(new Eff.OrdinaryColor(c), arrow.getLocation().clone().add(0, .25, 0), 200);
					}
				}
			}.runTaskTimer(MoarBows.plugin, 0, 1);
		}
	}
}