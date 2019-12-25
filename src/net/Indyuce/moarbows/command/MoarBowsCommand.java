package net.Indyuce.moarbows.command;

import java.util.Optional;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.util.Message;
import net.Indyuce.moarbows.gui.BowList;

public class MoarBowsCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 1) {
			if (!sender.hasPermission("moarbows.admin")) {
				sender.sendMessage(Message.NOT_ENOUGH_PERMS.translate());
				return true;
			}

			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "-----------------[" + ChatColor.LIGHT_PURPLE + " MoarBows Help " + ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "]-----------------");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "<>" + ChatColor.GRAY + " = required");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "()" + ChatColor.GRAY + " = optional");
			sender.sendMessage("");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb " + ChatColor.WHITE + "shows the help page.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb get <bow> (player) " + ChatColor.WHITE + "gives a bow to a player.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb getall " + ChatColor.WHITE + "gives you all the available bows.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb gui " + ChatColor.WHITE + "shows all available bows (GUI).");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb list " + ChatColor.WHITE + "shows all available bows.");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "/mb reload " + ChatColor.WHITE + "reloads the config file.");
			return true;
		}

		if (args[0].equalsIgnoreCase("gui")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This command is for players only.");
				return true;
			}

			if (!sender.hasPermission("moarbows.gui")) {
				sender.sendMessage(Message.NOT_ENOUGH_PERMS.translate());
				return true;
			}

			new BowList((Player) sender).open();
		}

		// perm for op commands
		if (!sender.hasPermission("moarbows.admin")) {
			sender.sendMessage(Message.NOT_ENOUGH_PERMS.translate());
			return true;
		}

		if (args[0].equalsIgnoreCase("reload")) {
			MoarBows.plugin.reloadPlugin();
			sender.sendMessage(ChatColor.YELLOW + "Config files & bows reloaded.");
		}

		if (args[0].equalsIgnoreCase("list")) {
			sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "------------------------------------------------");
			sender.sendMessage(ChatColor.GREEN + "List of available bows:");
			if (!(sender instanceof Player)) {
				for (MoarBow bow : MoarBows.plugin.getBowManager().getBows())
					sender.sendMessage("* " + ChatColor.GREEN + " " + bow.getName());
				return true;
			}

			for (MoarBow bow : MoarBows.plugin.getBowManager().getBows())
				MoarBows.plugin.getNMS().sendJson((Player) sender, "{\"text\":\"* " + ChatColor.GREEN + bow.getName() + ChatColor.WHITE + ", use /mb get " + bow.getLowerCaseId() + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/mb get " + bow.getId() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Click to get the " + ChatColor.GREEN + bow.getName() + ChatColor.WHITE + ".\",\"color\":\"white\"}]}}}");
		}

		if (args[0].equalsIgnoreCase("equip")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This command is for players only.");
				return true;
			}

			Player player = (Player) sender;
			if (player.getEquipment().getItemInMainHand() == null || player.getEquipment().getItemInMainHand().getType() == Material.AIR) {
				sender.sendMessage(ChatColor.RED + "Hold something in your hands first.");
				return true;
			}

			Optional<Entity> found = player.getNearbyEntities(10, 10, 10).stream().filter(entity -> entity instanceof LivingEntity).findFirst();
			if (!found.isPresent()) {
				sender.sendMessage(ChatColor.RED + "Couldn't find an entity to equip.");
				return true;
			}

			LivingEntity target = (LivingEntity) found.get();
			ItemStack hand = target.getEquipment().getItemInMainHand();
			target.getEquipment().setItemInMainHand(player.getEquipment().getItemInMainHand());
			if (hand != null)
				target.getWorld().dropItem(target.getLocation(), hand);
		}

		if (args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("give")) {
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "Usage: /mb get <bow> (player) (level)");
				return true;
			}

			if (args.length < 3 && !(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Please specify a player.");
				return true;
			}

			// bow
			String bowFormat = args[1].toUpperCase().replace("-", "_");
			MoarBow bow = MoarBows.plugin.getBowManager().safeGet(bowFormat);
			if (bow == null) {
				sender.sendMessage(ChatColor.RED + "Couldn't find the bow called " + bowFormat + ".");
				return true;
			}

			// player
			Player target = args.length > 2 ? Bukkit.getPlayer(args[2]) : ((Player) sender);
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "Couldn't find the player called " + args[2] + ".");
				return true;
			}

			// level
			int level = 0;
			if (args.length > 3)
				try {
					level = Integer.parseInt(args[3]);
					Validate.isTrue(level > 0);
				} catch (IllegalArgumentException exception) {
					sender.sendMessage(ChatColor.RED + args[3] + " is not a valid number.");
					return true;
				}

			// give item
			ItemStack item = bow.getItem(level);
			for (ItemStack drop : target.getInventory().addItem(item).values())
				target.getWorld().dropItem(target.getLocation(), drop);
			sender.sendMessage(ChatColor.YELLOW + target.getName() + " was given " + ChatColor.WHITE + bow.getName() + ChatColor.YELLOW + ".");

			// message
			String message = Message.RECEIVE_BOW.translate();
			if (!message.equals("") && sender != target)
				target.sendMessage(ChatColor.YELLOW + message.replace("%bow%", bow.getName()));

		}
		if (args[0].equalsIgnoreCase("getall")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This command is for players only.");
				return true;
			}

			Player player = (Player) sender;
			for (MoarBow bow : MoarBows.plugin.getBowManager().getBows())
				for (ItemStack drop : player.getInventory().addItem(bow.getItem(1)).values())
					player.getWorld().dropItem(player.getLocation(), drop);
		}

		return true;
	}
}
