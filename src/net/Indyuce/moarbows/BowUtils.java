package net.Indyuce.moarbows;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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

	public static boolean consumeAmmo(LivingEntity entity, ItemStack i) {

		/*
		 * if sender is not a player, then do not consume anyammo
		 */
		if (!(entity instanceof Player))
			return true;

		/*
		 * does not consume ammo if the player is in creative mode
		 */
		Player player = (Player) entity;
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
			return true;

		/*
		 * returns false if the player has no item
		 */
		if (!player.getInventory().containsAtLeast(i, 1))
			return false;

		/*
		 * returns true and consumes the ammo if the player has enough
		 */
		player.getInventory().removeItem(i);
		return true;
	}

	public static boolean isPluginItem(ItemStack item, boolean lore) {
		return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && (!lore || item.getItemMeta().hasLore());
	}

	public static ItemStack removeDisplayName(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(null);

		ItemStack clone = item.clone();
		clone.setItemMeta(meta);
		return clone;
	}

	public static double getPowerDamageMultiplier(ItemStack item) {
		return item == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) ? 1
				: 1 + .25 * (item.getItemMeta().getEnchantLevel(Enchantment.ARROW_DAMAGE) + 1);
	}

	public static boolean canTarget(LivingEntity shooter, Location loc, Entity target) {
		if (target.hasMetadata("NPC"))
			return false;

		BoundingBox box = target.getBoundingBox();
		return (loc == null || (loc.getX() >= box.getMinX() - .5 && loc.getY() >= box.getMinY() - .5 && loc.getZ() >= box.getMinZ() - .5
				&& loc.getX() <= box.getMaxX() + .5 && loc.getY() <= box.getMaxY() + .5 && loc.getZ() <= box.getMaxZ() + .5))
				&& !target.equals(shooter);
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

	/*
	 * method to get all entities surrounding a location. this method does not
	 * take every entity in the world but rather takes all the entities from the
	 * 9 chunks around the entity, so even if the location is at the border of a
	 * chunk (worst case border of 4 chunks), the entity will still be included
	 */
	public static List<Entity> getNearbyChunkEntities(Location loc) {

		/*
		 * another method to save performance is if an entity bounding box
		 * calculation is made twice in the same tick then the method does not
		 * need to be called twice, it can utilize the same entity list since
		 * the entities have not moved (e.g fireball which does 2+ calculations
		 * per tick)
		 */
		List<Entity> entities = new ArrayList<>();

		int cx = loc.getChunk().getX();
		int cz = loc.getChunk().getZ();

		for (int x = -1; x < 2; x++)
			for (int z = -1; z < 2; z++)
				for (Entity entity : loc.getWorld().getChunkAt(cx + x, cz + z).getEntities())
					entities.add(entity);

		return entities;
	}
}
