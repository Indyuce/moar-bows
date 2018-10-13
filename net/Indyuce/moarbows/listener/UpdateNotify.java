package net.Indyuce.moarbows.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.Indyuce.moarbows.MoarBows;

public class UpdateNotify implements Listener {
	@EventHandler
	public void a(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Bukkit.broadcastMessage("test " + p.hasPermission("moarbows.update-notify"));
		if (!p.hasPermission("moarbows.update-notify"))
			return;

		if (MoarBows.getSpigotPlugin().isOutOfDate())
			for (String s : MoarBows.getSpigotPlugin().getOutOfDateMessage())
				p.sendMessage(ChatColor.GREEN + "(MoarBows) " + s);
	}
}
