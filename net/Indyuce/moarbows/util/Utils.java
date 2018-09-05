package net.Indyuce.moarbows.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.Indyuce.moarbows.MoarBows;

public class Utils implements Listener {
	public static Map<UUID, Map<String, Long>> cd = new HashMap<UUID, Map<String, Long>>();

	public static String msg(String path) {
		return ChatColor.translateAlternateColorCodes('&', MoarBows.messages.getString(path));
	}

	public static String caseOnWords(String s) {
		StringBuilder builder = new StringBuilder(s);
		boolean isLastSpace = true;
		for (int i = 0; i < builder.length(); i++) {
			char ch = builder.charAt(i);
			if (isLastSpace && ch >= 'a' && ch <= 'z') {
				builder.setCharAt(i, (char) (ch + ('A' - 'a')));
				isLastSpace = false;
			} else if (ch != ' ')
				isLastSpace = false;
			else
				isLastSpace = true;
		}
		return builder.toString();
	}

	public static boolean consumeAmmo(Player p, ItemStack i) {
		if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR)
			return true;
		if (!p.getInventory().containsAtLeast(i, 1))
			return false;
		p.getInventory().removeItem(i);
		return true;
	}

	public static boolean isPluginItem(ItemStack i, boolean lore) {
		if (i == null)
			return false;
		if (i.getItemMeta() == null)
			return false;
		if (i.getItemMeta().getDisplayName() == null)
			return false;
		if (lore && i.getItemMeta().getLore() == null)
			return false;
		return true;
	}

	public static ItemStack removeDisplayName(ItemStack i) {
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(null);
		i.setItemMeta(meta);
		return i;
	}

	public static boolean canDmgEntity(Player p, Location loc, Entity t) {
		if (t.hasMetadata("NPC"))
			return false;
		double[] boundingBox = VersionUtils.getBoundingBox(t);
		if ((loc == null ? true : loc.getX() >= boundingBox[0] - .5 && loc.getY() >= boundingBox[1] - .5 && loc.getZ() >= boundingBox[2] - .5 && loc.getX() <= boundingBox[3] + .5 && loc.getY() <= boundingBox[4] + .5 && loc.getZ() <= boundingBox[5] + .5) && t != p)
			return true;
		return false;
	}
}
