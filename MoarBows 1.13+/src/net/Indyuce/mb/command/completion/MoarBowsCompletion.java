package net.Indyuce.mb.command.completion;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.Indyuce.mb.Main;

public class MoarBowsCompletion implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (!p.hasPermission("moarbows.cmd"))
			return null;

		List<String> list = new ArrayList<String>();
		if (args.length == 1) {
			list.add("get");
			list.add("getall");
			list.add("gui");
			list.add("list");
			list.add("reload");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("get"))
					list.addAll(Main.map.keySet());
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("get"))
				for (Player t : Bukkit.getOnlinePlayers())
					list.add(t.getName());
		}

		// if last arg is not empty, only complete word
		String lastArg = args[args.length - 1];
		if (!lastArg.isEmpty()) {
			List<String> newList = new ArrayList<String>();
			for (String s : list)
				if (s.toLowerCase().startsWith(lastArg.toLowerCase()))
					newList.add(s);
			list = newList;
		}

		return list;
	}
}
