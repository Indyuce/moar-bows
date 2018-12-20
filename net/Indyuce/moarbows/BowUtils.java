package net.Indyuce.moarbows;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
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

	public static String statsInLore(FileConfiguration config, String lore1, String mainName) {
		if (lore1.contains("#")) {
			String stat = lore1.split("#")[1];
			double mod = config.getDouble(mainName + "." + stat);

			lore1 = lore1.replace("#" + stat + "#", ChatColor.WHITE + "" + mod + "" + ChatColor.GRAY);
			lore1 = statsInLore(config, lore1, mainName);
		}
		return lore1;
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

	public static boolean canDmgEntity(Player p, Location loc, Entity t) {
		if (t.hasMetadata("NPC"))
			return false;

		double[] boundingBox = MoarBows.getNMS().getBoundingBox(t);
		return (loc == null ? true : loc.getX() >= boundingBox[0] - .5 && loc.getY() >= boundingBox[1] - .5 && loc.getZ() >= boundingBox[2] - .5 && loc.getX() <= boundingBox[3] + .5 && loc.getY() <= boundingBox[4] + .5 && loc.getZ() <= boundingBox[5] + .5) && t != p;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack[] getHandItems(Player p) {
		return MoarBows.getVersion().isBelowOrEqual(1, 8) ? new ItemStack[] { p.getItemInHand() } : new ItemStack[] { p.getInventory().getItemInMainHand(), p.getInventory().getItemInOffHand() };
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

	public static Vector rotateFunc(Vector v, double yaw, double pitch) {
		yaw *= Math.PI / 180;
		pitch *= Math.PI / 180;
		v = rotAxisX(v, pitch);
		v = rotAxisY(v, -yaw);
		return v;
	}

	@SuppressWarnings("deprecation")
	private static String parse(MaterialData data) {
		return data.getItemType().name() + (data.getData() != 0 ? ":" + data.getData() : "");
	}

	// the array must be 9 values long
	public static String[] formatCraftingRecipe(MaterialData... craftingRecipe) {
		if (craftingRecipe.length != 9)
			return null;

		List<String> line1 = Arrays.asList(new String[] { parse(craftingRecipe[0]), parse(craftingRecipe[1]), parse(craftingRecipe[2]) });
		List<String> line2 = Arrays.asList(new String[] { parse(craftingRecipe[3]), parse(craftingRecipe[4]), parse(craftingRecipe[5]) });
		List<String> line3 = Arrays.asList(new String[] { parse(craftingRecipe[6]), parse(craftingRecipe[7]), parse(craftingRecipe[8]) });

		return new String[] { Strings.join(line1, ','), Strings.join(line2, ','), Strings.join(line3, ',') };
	}
}
