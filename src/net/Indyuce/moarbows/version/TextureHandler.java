package net.Indyuce.moarbows.version;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.version.nms.ItemTag;
import net.Indyuce.moarbows.version.nms.NBTItem;

public class TextureHandler {

	/*
	 * the tag is either "Damage" for 1.13 users or "CustomModelData" for 1.14
	 * users. texture by durability is also doable via item meta but the
	 * "Damage" tag lets us you NBTItems in both versions
	 */
	private final String tag;

	public TextureHandler(String tag) {
		this.tag = tag;
	}

	public NBTItem copyTexture(NBTItem item) {
		return MoarBows.plugin.getNMS().getNBTItem(new ItemStack(item.getItem().getType())).addTag(new ItemTag(tag, item.getInteger(tag)));
	}

	public ItemStack textureItem(Material material, int model) {
		return MoarBows.plugin.getNMS().getNBTItem(new ItemStack(material)).addTag(new ItemTag(tag, model)).toItem();
	}
}
