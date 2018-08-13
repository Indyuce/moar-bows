package net.Indyuce.mb.nms.nbttag;

import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.resource.ItemTag;

public interface NBTTags {
	public ItemStack add(ItemStack i, ItemTag... tags);

	public List<String> getTags(ItemStack i);

	public double getDoubleTag(ItemStack i, String path);

	public String getStringTag(ItemStack i, String path);

	public boolean getBooleanTag(ItemStack i, String path);

	public HashMap<String, Double> requestDoubleTags(ItemStack i, HashMap<String, Double> map);
}
