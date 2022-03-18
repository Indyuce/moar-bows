package net.Indyuce.moarbows.listener;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.bow.ArrowData;
import net.Indyuce.moarbows.bow.MoarBow;
import net.Indyuce.moarbows.player.PlayerData;
import net.Indyuce.moarbows.api.event.MoarBowShootEvent;
import net.Indyuce.moarbows.bow.particle.ArrowParticles;
import net.Indyuce.moarbows.comp.worldguard.CustomFlag;

public class ShootBow implements Listener {
	private static final DecimalFormat cooldownFormat = new DecimalFormat("0.#");

	@EventHandler
	public void a(EntityShootBowEvent event) {
		if (!(event.getProjectile() instanceof Arrow))
			return;

		if (event.getForce() < 1 && MoarBows.plugin.getConfig().getBoolean("full-pull-restriction"))
			return;

		/*
		 * check for bow
		 */
		ItemStack item = event.getBow();
		MoarBow bow = MoarBows.plugin.getBowManager().get(item);
		if (bow == null)
			return;

		/*
		 * permission check
		 */
		if (event.getEntity() instanceof Player)
			if (!event.getEntity().hasPermission("moarbows.use." + bow.getLowerCaseId())) {
				event.getEntity().sendMessage(MoarBows.plugin.getLanguage().formatMessage("not-enough-perms"));
				event.setCancelled(true);
				return;
			}

		/*
		 * worldguard flag check
		 */
		if (event.getEntity() instanceof Player ? !MoarBows.plugin.getWorldGuard().isFlagAllowed((Player) event.getEntity(), CustomFlag.MB_BOWS)
				: !MoarBows.plugin.getWorldGuard().isFlagAllowed(event.getEntity().getLocation(), CustomFlag.MB_BOWS)) {
			event.setCancelled(true);
			if (event.getEntity() instanceof Player)
				event.getEntity().sendMessage(MoarBows.plugin.getLanguage().formatMessage("disable-bows-flag"));
			return;
		}

		/*
		 * cooldown check
		 */
		ArrowData arrowData = event.getEntity() instanceof Player
				? new ArrowData(bow, PlayerData.get((Player) event.getEntity()), (Arrow) event.getProjectile(), item)
				: new ArrowData(bow, event.getEntity(), (Arrow) event.getProjectile(), item);
		if (arrowData.hasPlayer()) {
			Player player = (Player) event.getEntity();
			if (arrowData.getPlayerData().hasCooldown(arrowData.getBow(), arrowData.getLevel())) {
				player.sendMessage(MoarBows.plugin.getLanguage().formatMessage("on-cooldown", "left",
						cooldownFormat.format(arrowData.getPlayerData().getRemainingCooldown(bow, arrowData.getLevel()))));
				event.setCancelled(true);
				return;
			}
		}

		/*
		 * bukkit event
		 */
		MoarBowShootEvent bowEvent = new MoarBowShootEvent(arrowData);
		Bukkit.getPluginManager().callEvent(bowEvent);
		if (bowEvent.isCancelled())
			return;

		if (arrowData.hasPlayer())
			arrowData.getPlayerData().applyCooldown(bow);

		/*
		 * shoot effect
		 */
		if (!bow.canShoot(event, arrowData)) {
			event.setCancelled(true);
			return;
		}

		/*
		 * register arrow data
		 */
		MoarBows.plugin.getArrowManager().registerArrow(arrowData);

		/*
		 * arrow particles if enabled
		 */
		if (MoarBows.plugin.getConfig().getBoolean("arrow-particles"))
			new ArrowParticles(bow, arrowData.getArrow()).runTaskTimer(MoarBows.plugin, 0, 1);
	}
}