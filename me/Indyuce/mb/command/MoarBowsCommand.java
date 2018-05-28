package me.Indyuce.mb.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Indyuce.mb.GUI;
import me.Indyuce.mb.Main;
import me.Indyuce.mb.reflect.Json;
import me.Indyuce.mb.resource.Bow;
import me.Indyuce.mb.util.Utils;

public class MoarBowsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 1) {
			sender.sendMessage("§e-----------------------------------------------------");
			sender.sendMessage("§a§l" + Main.plugin.getName() + " " + Main.plugin.getDescription().getVersion());
			sender.sendMessage("");
			sender.sendMessage("§a/mb get <bow> (player) §fgives you or the player a specific bow.");
			sender.sendMessage("§a/mb getall §fgives you all the bows.");
			sender.sendMessage("§a/mb gui §fopens the Bows GUI.");
			sender.sendMessage("§a/mb list §fshows the list of all bows.");
			sender.sendMessage("§a/mb reload §freloads the config file.");
			return false;
		}
		if (args[0].equalsIgnoreCase("gui")) {
			if (!Utils.checkPl(sender, true))
				return false;
			if (!sender.hasPermission("moarbows.gui"))
				return false;
			GUI.openInv((Player) sender);
		}
		if (!sender.hasPermission("moarbows.cmd"))
			return false;
		if (args[0].equalsIgnoreCase("reload")) {
			Main.plugin.reloadConfig();
			sender.sendMessage(ChatColor.YELLOW + Utils.msg("config-reload"));
		}
		if (args[0].equalsIgnoreCase("list")) {
			sender.sendMessage("§e-----------------------------------------------------");
			sender.sendMessage(ChatColor.GREEN + Utils.msg("bow-list"));
			if (!Utils.checkPl(sender, false)) {
				for (Bow b : Bow.values())
					sender.sendMessage("* §a " + b.name);
				return false;
			}
			for (Bow b : Bow.values())
				Json.json((Player) sender, "{\"text\":\"* §a" + b.name + "§f, " + Utils.msg("use") + " /mb get " + b.name() + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/mb get " + b.name() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Click to get the §a" + b.name + "§f.\",\"color\":\"white\"}]}}}");
		}
		if (args[0].equalsIgnoreCase("get")) {
			if (args.length < 2) {
				if (!Main.plugin.getConfig().getBoolean("disable-give-cmd-msg"))
					sender.sendMessage(ChatColor.RED + "Usage: /mb get <bow> (player).");
				return false;
			}
			for (Bow b1 : Bow.values()) {
				String name = b1.name();
				if (name.equals(args[1].toUpperCase().replace("-", "_"))) {
					if (args.length > 2) {
						String pl = args[2];
						Player t = Bukkit.getPlayer(pl);
						if (t != null) {
							if (t.getInventory().firstEmpty() == -1) {
								t.getWorld().dropItem(t.getLocation(), b1.a());
								if (!Main.plugin.getConfig().getBoolean("disable-give-cmd-msg")) {
									sender.sendMessage(ChatColor.YELLOW + Utils.msg("give-bow-to-other-player").replace("%BOW%", args[1].toUpperCase()).replace("%PLAYER%", t.getName()));
									sender.sendMessage(ChatColor.YELLOW + Utils.msg("bow-dropped-on-ground"));
								}
								return false;
							}
							if (!Main.plugin.getConfig().getBoolean("disable-give-cmd-msg"))
								sender.sendMessage(ChatColor.YELLOW + Utils.msg("give-bow-to-other-player").replace("%BOW%", args[1].toUpperCase()).replace("%PLAYER%", t.getName()));
							t.getInventory().addItem(b1.a());
							return false;
						}
						if (!Main.plugin.getConfig().getBoolean("disable-give-cmd-msg"))
							sender.sendMessage(ChatColor.RED + Utils.msg("couldnt-find-player").replace("%PLAYER%", args[2]));
						return false;
					}
					if (!Utils.checkPl(sender, true))
						return false;
					if (((Player) sender).getInventory().firstEmpty() == -1) {
						if (!Main.plugin.getConfig().getBoolean("disable-give-cmd-msg")) {
							sender.sendMessage(ChatColor.YELLOW + Utils.msg("give-bow-to-yourself").replace("%BOW%", args[1].toUpperCase()));
							sender.sendMessage(ChatColor.YELLOW + Utils.msg("bow-dropped-on-ground"));
						}
						((Player) sender).getWorld().dropItem(((Player) sender).getLocation(), b1.a());
						return false;
					}
					if (!Main.plugin.getConfig().getBoolean("disable-give-cmd-msg"))
						sender.sendMessage(ChatColor.YELLOW + Utils.msg("give-bow-to-yourself").replace("%BOW%", args[1].toUpperCase()));
					((Player) sender).getInventory().addItem(b1.a());
					return false;
				}
			}
			sender.sendMessage(ChatColor.RED + Utils.msg("couldnt-find-bow").replace("%BOW%", args[1].toUpperCase()));
		}
		if (args[0].equalsIgnoreCase("getall")) {
			if (!Utils.checkPl(sender, true))
				return false;
			for (Bow b : Bow.values()) {
				if (((Player) sender).getInventory().firstEmpty() == -1) {
					((Player) sender).getWorld().dropItem(((Player) sender).getLocation(), b.a());
					continue;
				}
				((Player) sender).getInventory().addItem(b.a());
			}
		}

		return true;
	}
}
