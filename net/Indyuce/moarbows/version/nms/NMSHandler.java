package net.Indyuce.moarbows.version.nms;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSHandler {
	public ItemStack addTag(ItemStack i, ItemTag... tags);

	public String getStringTag(ItemStack i, String path);

	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int ticks, int fadeOut);

	public void sendJson(Player player, String message);

	public void damageEntity(Player player, LivingEntity entity, double value);

	public double[] getBoundingBox(Entity p);

	public default boolean isInBoundingBox(Entity p, Location loc) {
		double[] bb = getBoundingBox(p);
		return loc.getX() > bb[0] && loc.getX() < bb[3] && loc.getY() > bb[1] && loc.getY() < bb[4] && loc.getZ() > bb[2] && loc.getZ() < bb[5];
	}
}
