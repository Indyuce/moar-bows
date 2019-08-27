package net.Indyuce.moarbows.command.completion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import net.Indyuce.moarbows.MoarBows;

public class MoarBowsCompletion implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		List<String> list = new ArrayList<>();
		if (!sender.hasPermission("moarbows.admin"))
			return list;

		if (args.length == 1) {
			list.add("get");
			list.add("getall");
			list.add("gui");
			list.add("list");
			list.add("reload");

		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("get"))
				MoarBows.plugin.getBowManager().getBows().forEach(bow -> list.add(bow.getId()));

		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("get"))
				Bukkit.getOnlinePlayers().forEach(online -> list.add(online.getName()));
		}

		return args[args.length - 1].isEmpty() ? list : list.stream().filter(string -> string.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
	}
}
