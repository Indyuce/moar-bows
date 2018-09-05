package net.Indyuce.moarbows.version.nms;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSHandler {
	public ItemStack addTag(ItemStack i, ItemTag... tags);

	public List<String> getTags(ItemStack i);

	public double getDoubleTag(ItemStack i, String path);

	public String getStringTag(ItemStack i, String path);

	public boolean getBooleanTag(ItemStack i, String path);

	public HashMap<String, Double> requestDoubleTags(ItemStack i, HashMap<String, Double> map);

	public ItemStack addAttribute(ItemStack i, Attribute... attributes);

	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int ticks, int fadeOut);

	public void sendActionBar(Player p, String msg);

	public void sendJson(Player player, String message);

	public void damageEntity(Player player, LivingEntity entity, double value);

	public double[] getBoundingBox(Entity p);

	public default boolean isInBoundingBox(Entity p, Location loc) {
		double[] bb = getBoundingBox(p);
		return loc.getX() > bb[0] && loc.getX() < bb[3] && loc.getY() > bb[1] && loc.getY() < bb[4] && loc.getZ() > bb[2] && loc.getZ() < bb[5];
	}

	public default double distanceFromBoundingBox(Entity p, Location loc) {
		return Math.sqrt(distanceSquaredFromBoundingBox(p, loc));
	}

	public default double distanceSquaredFromBoundingBox(Entity p, Location loc) {
		double[] bb = getBoundingBox(p);

		double dx = loc.getX() > bb[0] && loc.getX() < bb[3] ? 0 : Math.min(Math.abs(bb[0] - loc.getX()), Math.abs(bb[3] - loc.getX()));
		double dy = loc.getY() > bb[1] && loc.getY() < bb[4] ? 0 : Math.min(Math.abs(bb[1] - loc.getY()), Math.abs(bb[4] - loc.getY()));
		double dz = loc.getZ() > bb[2] && loc.getZ() < bb[5] ? 0 : Math.min(Math.abs(bb[2] - loc.getZ()), Math.abs(bb[5] - loc.getZ()));

		return Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2);
	}
}
