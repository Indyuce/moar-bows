package net.Indyuce.moarbows.listener;

import java.util.logging.Level;

import org.bukkit.Color;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.ParticleEffect;
import net.Indyuce.moarbows.api.ArrowManager;
import net.Indyuce.moarbows.api.Message;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.comp.worldguard.CustomFlag;

public class ShootBow implements Listener {
	@EventHandler
	public void a(EntityShootBowEvent event) {
		if (event.getForce() < 1.0 && MoarBows.plugin.getConfig().getBoolean("full-pull-restriction"))
			return;

		ItemStack item = event.getBow();
		if (!(event.getProjectile() instanceof Arrow) || !(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();
		if (!BowUtils.isPluginItem(item, false))
			return;

		// check for bow
		MoarBow bow = MoarBows.getFromItem(item);
		if (bow == null)
			return;

		// permission
		if (!player.hasPermission("moarbows.use." + bow.getLowerCaseID())) {
			player.sendMessage(Message.NOT_ENOUGH_PERMS.translate());
			event.setCancelled(true);
		}

		// worldguard flag
		if (!MoarBows.getWorldGuard().isFlagAllowed(player, CustomFlag.MB_BOWS)) {
			event.setCancelled(true);
			player.sendMessage(Message.DISABLE_BOWS_FLAG.translate());
			return;
		}

		// cooldown
		if (!bow.canUse(player, event)) {
			event.setCancelled(true);
			return;
		}

		// shoot effect
		Arrow arrow = (Arrow) event.getProjectile();
		if (!bow.shoot(event, arrow, player, item)) {
			event.setCancelled(true);
			return;
		}

		// register arrow
		ArrowManager.registerArrow(arrow, bow, player);

		// arrow particles
		if (MoarBows.plugin.getConfig().getBoolean("arrow-particles")) {
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
					ParticleEffect effName = null;
					try {
						effName = ParticleEffect.valueOf(s[0].toUpperCase());
					} catch (Exception event) {
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
						effName.display(new ParticleEffect.OrdinaryColor(c), arrow.getLocation().clone().add(0, .25, 0), 200);
					}
				}
			}.runTaskTimer(MoarBows.plugin, 0, 1);
		}
	}
}