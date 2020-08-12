package net.Indyuce.moarbows.version.wrapper;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class VersionWrapper {
	private final String modelTagPath;

	/**
	 * Used to handle version specific code
	 * 
	 * @param modelTagPath
	 *            NBTTag path used to path custom model data, ie 'Damage' in
	 *            1.13 or lower and 'CustomModelData' since 1.14
	 */
	public VersionWrapper(String modelTagPath) {
		this.modelTagPath = modelTagPath;
	}

	/**
	 * @param item
	 *            Item to extract NBTTags data from
	 * @return An instance of NBTItem to add, remove or edit nbt tags on an item
	 *         stack
	 */
	public abstract NBTItem getNBTItem(ItemStack item);

	public abstract void sendJson(Player player, String message);

	public NBTItem copyTexture(NBTItem item) {
		return getNBTItem(new ItemStack(item.getItem().getType())).addTag(new ItemTag(modelTagPath, item.getInteger(modelTagPath)));
	}

	public ItemStack textureItem(Material material, int model) {
		return getNBTItem(new ItemStack(material)).addTag(new ItemTag(modelTagPath, model)).toItem();
	}
}
