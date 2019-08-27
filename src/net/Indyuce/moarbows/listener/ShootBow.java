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
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.Message;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.PlayerData;
import net.Indyuce.moarbows.api.event.MoarBowShootEvent;
import net.Indyuce.moarbows.api.runnable.ArrowParticles;
import net.Indyuce.moarbows.comp.worldguard.CustomFlag;

public class ShootBow implements Listener {
	private final DecimalFormat cooldownFormat = new DecimalFormat("0.#");

	@EventHandler
	public void a(EntityShootBowEvent event) {
		if (!(event.getProjectile() instanceof Arrow) || !(event.getEntity() instanceof Player))
			return;

		if (event.getForce() < 1 && MoarBows.plugin.getConfig().getBoolean("full-pull-restriction"))
			return;

		// check for bow
		ItemStack item = event.getBow();
		MoarBow bow = MoarBows.plugin.getBowManager().get(item);
		if (bow == null)
			return;

		// permission
		Player player = (Player) event.getEntity();
		if (!player.hasPermission("moarbows.use." + bow.getLowerCaseId())) {
			player.sendMessage(Message.NOT_ENOUGH_PERMS.translate());
			event.setCancelled(true);
			return;
		}

		// worldguard flag
		if (!MoarBows.plugin.getWorldGuard().isFlagAllowed(player, CustomFlag.MB_BOWS)) {
			event.setCancelled(true);
			player.sendMessage(Message.DISABLE_BOWS_FLAG.translate());
			return;
		}

		// cooldown
		PlayerData playerData = PlayerData.get(player);
		ArrowData arrowData = new ArrowData(bow, playerData, (Arrow) event.getProjectile(), item);
		if (playerData.hasCooldown(arrowData.getBow(), arrowData.getLevel())) {
			player.sendMessage(Message.ON_COOLDOWN.translate().replace("%left%", cooldownFormat.format(playerData.getRemainingCooldown(bow, arrowData.getLevel()))));
			event.setCancelled(true);
			return;
		}

		MoarBowShootEvent bowEvent = new MoarBowShootEvent(playerData, arrowData);
		Bukkit.getPluginManager().callEvent(bowEvent);
		if (bowEvent.isCancelled())
			return;

		playerData.applyCooldown(bow);

		// shoot effect
		if (!bow.canShoot(event, arrowData)) {
			event.setCancelled(true);
			return;
		}

		// register arrow
		MoarBows.plugin.getArrowManager().registerArrow(arrowData);

		// arrow particles
		if (MoarBows.plugin.getConfig().getBoolean("arrow-particles"))
			new ArrowParticles(bow, arrowData.getArrow()).runTaskTimer(MoarBows.plugin, 0, 1);
	}
}