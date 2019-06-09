package net.Indyuce.moarbows;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class BowUtils implements Listener {
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

		// does not consume ammo if the
		// player is in creative mode
		if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR)
			return true;

		// returns false if the
		// player does not have the item
		if (!p.getInventory().containsAtLeast(i, 1))
			return false;

		// consume the ammo
		// and return true
		p.getInventory().removeItem(i);
		return true;
	}

	public static boolean isPluginItem(ItemStack i, boolean lore) {
		if (i != null)
			if (i.hasItemMeta())
				if (i.getItemMeta().hasDisplayName())
					return !lore || i.getItemMeta().getLore() != null;
		return false;
	}

	public static ItemStack removeDisplayName(ItemStack i) {
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(null);
		i.setItemMeta(meta);
		return i;
	}

	public static boolean canDmgEntity(Player player, Location loc, Entity target) {
		if (target.hasMetadata("NPC"))
			return false;

		BoundingBox box = target.getBoundingBox();
		return (loc == null ? true : loc.getX() >= box.getMinX() - .5 && loc.getY() >= box.getMinY() - .5 && loc.getZ() >= box.getMinZ() - .5 && loc.getX() <= box.getMaxX() + .5 && loc.getY() <= box.getMaxY() + .5 && loc.getZ() <= box.getMaxZ() + .5) && !target.equals(player);
	}

	public static double truncation(double x, int n) {
		double pow = Math.pow(10.0, n);
		return Math.floor(x * pow) / pow;
	}

	public static Vector rotAxisX(Vector v, double a) {
		double y = v.getY() * Math.cos(a) - v.getZ() * Math.sin(a);
		double z = v.getY() * Math.sin(a) + v.getZ() * Math.cos(a);
		return v.setY(y).setZ(z);
	}

	public static Vector rotAxisY(Vector v, double b) {
		double x = v.getX() * Math.cos(b) + v.getZ() * Math.sin(b);
		double z = v.getX() * -Math.sin(b) + v.getZ() * Math.cos(b);
		return v.setX(x).setZ(z);
	}

	public static Vector rotAxisZ(Vector v, double c) {
		double x = v.getX() * Math.cos(c) - v.getY() * Math.sin(c);
		double y = v.getX() * Math.sin(c) + v.getY() * Math.cos(c);
		return v.setX(x).setY(y);
	}

	public static Vector rotateFunc(Vector v, Location loc) {
		double yaw = loc.getYaw() / 180 * Math.PI;
		double pitch = loc.getPitch() / 180 * Math.PI;
		v = rotAxisX(v, pitch);
		v = rotAxisY(v, -yaw);
		return v;
	}
}
