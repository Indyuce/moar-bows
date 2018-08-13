package net.Indyuce.mb.nms.nbttag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.resource.ItemTag;
import net.minecraft.server.v1_10_R1.NBTTagCompound;

public class NBTTags_1_10_R1 implements NBTTags {
	@Override
	public ItemStack add(ItemStack i, ItemTag... tags) {
		net.minecraft.server.v1_10_R1.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();

		for (ItemTag tag : tags) {
			if (tag.value instanceof Boolean) {
				compound.setBoolean(tag.path, (boolean) tag.value);
				continue;
			}
			if (tag.value instanceof Double) {
				compound.setDouble(tag.path, (double) tag.value);
				continue;
			}
			compound.setString(tag.path, (String) tag.value);
		}

		nmsi.setTag(compound);
		i = CraftItemStack.asBukkitCopy(nmsi);
		return i;
	}

	@Override
	public List<String> getTags(ItemStack i) {
		if (i == null || i.getType() == Material.AIR)
			return new ArrayList<String>();

		net.minecraft.server.v1_10_R1.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return new ArrayList<String>(compound.c());
	}

	@Override
	public boolean getBooleanTag(ItemStack i, String path) {
		if (i == null || i.getType() == Material.AIR)
			return false;

		net.minecraft.server.v1_10_R1.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getBoolean(path);
	}

	@Override
	public double getDoubleTag(ItemStack i, String path) {
		if (i == null || i.getType() == Material.AIR)
			return 0;

		net.minecraft.server.v1_10_R1.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getDouble(path);
	}

	@Override
	public String getStringTag(ItemStack i, String path) {
		if (i == null || i.getType() == Material.AIR)
			return "";

		net.minecraft.server.v1_10_R1.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getString(path);
	}

	@Override
	public HashMap<String, Double> requestDoubleTags(ItemStack i, HashMap<String, Double> map) {
		if (i == null || i.getType() == Material.AIR)
			return map;

		net.minecraft.server.v1_10_R1.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		for (String s : map.keySet())
			map.put(s, map.get(s) + compound.getDouble("MMOITEMS_" + s));

		return map;
	}
}
