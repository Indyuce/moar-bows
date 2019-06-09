package net.Indyuce.moarbows.command.completion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.MoarBow;

public class MoarBowsCompletion implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("moarbows.admin"))
			return null;

		List<String> list = new ArrayList<>();

		if (args.length == 1) {
			list.add("get");
			list.add("getall");
			list.add("gui");
			list.add("list");
			list.add("reload");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("get"))
				for (MoarBow bow : MoarBows.getBowManager().getBows())
					list.add(bow.getID());
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("get"))
				for (Player t : Bukkit.getOnlinePlayers())
					list.add(t.getName());
		}

		return args[args.length - 1].isEmpty() ? list : list.stream().filter(string -> string.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
	}
}
