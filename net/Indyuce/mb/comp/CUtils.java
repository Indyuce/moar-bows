package net.Indyuce.mb.comp;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.Indyuce.mb.resource.CustomFlag;

public class CUtils implements Listener {
	public static HashMap<String, Object> flags = new HashMap<String, Object>();

	public static boolean isActive(String plugin) {
		if (plugin.equalsIgnoreCase("worldguard"))
			return !flags.keySet().isEmpty();
		return false;
	}

	public static boolean boolFlag(Player p, CustomFlag cf) {
		if (isActive("worldguard"))
			return WorldGuardUtils.boolFlag(p, cf);
		return true;
	}
}
