package net.Indyuce.mb.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.ConfigData;
import net.Indyuce.mb.GUI;
import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.MoarBow;
import net.Indyuce.mb.reflect.Json;
import net.Indyuce.mb.util.Utils;

public class MoarBowsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------[" + ChatColor.LIGHT_PURPLE + " MoarBows Help " + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "]-----------------");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "<>" + ChatColor.GRAY + " = required");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "()" + ChatColor.GRAY + " = optional");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb " + ChatColor.WHITE + "shows the help page.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb get <bow> (player) " + ChatColor.WHITE + "gives a player a bow.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb getall " + ChatColor.WHITE + "gives you all the available bows.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb gui " + ChatColor.WHITE + "shows all available bows in a GUI.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb list " + ChatColor.WHITE + "shows all available bows.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb reload " + ChatColor.WHITE + "reloads the config file.");
			return true;
		}

		if (args[0].equalsIgnoreCase("gui")) {
			if (!Utils.checkPl(sender, true))
				return false;
			if (sender.hasPermission("moarbows.gui"))
				GUI.openInv((Player) sender);
		}

		// perm for op commands
		if (!sender.hasPermission("moarbows.cmd")) {
			sender.sendMessage(ChatColor.RED + Utils.msg("not-enough-perms"));
			return true;
		}

		if (args[0].equalsIgnoreCase("reload")) {
			
			// reload config files
			Main.plugin.reloadConfig();
			Main.bows = ConfigData.getCD(Main.plugin, "", "bows");
			Main.messages = ConfigData.getCD(Main.plugin, "", "messages");
			
			// reload bows
			for (MoarBow b : Main.map.values())
				b.update(Main.bows);
			
			sender.sendMessage(ChatColor.YELLOW + "Config files & bows reloaded.");
		}

		if (args[0].equalsIgnoreCase("list")) {
			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------------------");
			sender.sendMessage(ChatColor.GREEN + "List of available bows:");
			if (!Utils.checkPl(sender, false)) {
				for (MoarBow b : Main.map.values())
					sender.sendMessage("* " + ChatColor.GREEN + " " + ChatColor.translateAlternateColorCodes('&', b.getName()));
				return true;
			}
			for (MoarBow b : Main.map.values())
				Json.json((Player) sender, "{\"text\":\"* " + ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', b.getName()) + ChatColor.WHITE + ", use /mb get " + b.getID() + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/mb get " + b.getID() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Click to get the " + ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', b.getName()) + ChatColor.WHITE + ".\",\"color\":\"white\"}]}}}");
		}

		if (args[0].equalsIgnoreCase("get")) {
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "Usage: /mb get <bow> (player)");
				return false;
			}
			if (args.length < 3 && !(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This command is for players only.");
				return false;
			}

			// trap
			String bowFormat = args[1].toUpperCase().replace("-", "_");
			MoarBow bow = Main.map.containsKey(bowFormat) ? Main.map.get(bowFormat) : null;
			if (bow == null) {
				sender.sendMessage(ChatColor.RED + "Couldn't find the bow called " + bowFormat + ".");
				return false;
			}

			// player
			Player t = (args.length > 2 ? null : ((Player) sender));
			if (args.length > 2)
				t = Bukkit.getPlayer(args[2]);
			if (t == null) {
				sender.sendMessage(ChatColor.RED + "Couldn't find the player called " + args[2] + ".");
				return false;
			}

			// void
			ItemStack i = bow.a();
			if (t.getInventory().firstEmpty() == -1)
				t.getWorld().dropItem(t.getLocation(), i);
			else
				t.getInventory().addItem(i);
			sender.sendMessage(ChatColor.YELLOW + t.getName() + " was given " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', bow.getName()) + ChatColor.YELLOW + ".");

			// message
			String message = Utils.msg("receive-bow");
			if (!ChatColor.stripColor(message).equals("") && sender != t)
				t.sendMessage(ChatColor.YELLOW + message.replace("%bow%", ChatColor.translateAlternateColorCodes('&', bow.getName())));

		}
		if (args[0].equalsIgnoreCase("getall")) {
			if (!Utils.checkPl(sender, true))
				return false;
			for (MoarBow b : Main.map.values()) {
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
