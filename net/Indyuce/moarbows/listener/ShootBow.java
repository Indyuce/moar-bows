package net.Indyuce.moarbows.listener;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.Eff;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowManager;
import net.Indyuce.moarbows.api.Message;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.comp.worldguard.CustomFlag;
import net.Indyuce.moarbows.util.Utils;

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
			p.sendMessage(ChatColor.YELLOW + Message.DISABLE_BOWS_FLAG.translate());
			return;
		}

		// cooldown
		if (!bow.canUse(p, e)) {
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
						MoarBows.plugin.getLogger().log(Level.WARNING, "Couldn't recognize effect of " + bow.getID());
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