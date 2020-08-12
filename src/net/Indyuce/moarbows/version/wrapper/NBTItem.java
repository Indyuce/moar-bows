package net.Indyuce.moarbows.version.wrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.MoarBows;

public abstract class NBTItem {
	protected final ItemStack item;

	public NBTItem(ItemStack item) {
		this.item = item;
	}

	public ItemStack getItem() {
		return item;
	}

	public abstract String getString(String path);

	public abstract boolean hasTag(String path);

	public abstract boolean getBoolean(String path);

	public abstract double getDouble(String path);

	public abstract int getInteger(String path);

	public abstract NBTItem addTag(List<ItemTag> tags);

	public abstract NBTItem removeTag(String... paths);

	public abstract Set<String> getTags();

	public abstract ItemStack toItem();

	// TODO add NBTMeta for display name and lore for less calculations.

	public NBTItem addTag(ItemTag... tags) {
		return addTag(Arrays.asList(tags));
	}

	public static NBTItem get(ItemStack item) {
		return MoarBows.plugin.getVersionWrapper().getNBTItem(item);
	}
}
