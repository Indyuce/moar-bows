package me.Indyuce.mb.reflect;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.Indyuce.mb.resource.ItemTag;

public class NBTTags {
	public static ItemStack add(ItemStack i, ItemTag... tags) {
		try {
			Object nmsStack = RUt.obc("inventory.CraftItemStack").getDeclaredMethod("asNMSCopy", ItemStack.class).invoke(RUt.obc("inventory.CraftItemStack"), i);
			Object compound = ((boolean) nmsStack.getClass().getDeclaredMethod("hasTag").invoke(nmsStack) ? nmsStack.getClass().getDeclaredMethod("getTag").invoke(nmsStack) : RUt.nms("NBTTagCompound").getDeclaredConstructor().newInstance());

			for (ItemTag tag : tags) {
				if (tag.value instanceof Boolean) {
					compound.getClass().getDeclaredMethod("setBoolean", String.class, Boolean.TYPE).invoke(compound, tag.path, (boolean) tag.value);
					continue;
				}
				compound.getClass().getDeclaredMethod("setString", String.class, String.class).invoke(compound, tag.path, (String) tag.value);
			}

			nmsStack.getClass().getDeclaredMethod("setTag", compound.getClass()).invoke(nmsStack, compound);
			i = (ItemStack) RUt.obc("inventory.CraftItemStack").getDeclaredMethod("asBukkitCopy", nmsStack.getClass()).invoke(RUt.obc("inventory.CraftItemStack"), nmsStack);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException e) {
			e.printStackTrace();
		}
		return i;
	}

	@SuppressWarnings("unchecked")
	public static List<String> getTags(ItemStack i) {
		if (i == null)
			return new ArrayList<String>();
		if (i.getType() == Material.AIR)
			return new ArrayList<String>();
		Object tag = null;
		try {
			Object nmsStack = RUt.obc("inventory.CraftItemStack").getDeclaredMethod("asNMSCopy", ItemStack.class).invoke(RUt.obc("inventory.CraftItemStack"), i);
			Object compound = ((boolean) nmsStack.getClass().getDeclaredMethod("hasTag").invoke(nmsStack) ? nmsStack.getClass().getDeclaredMethod("getTag").invoke(nmsStack) : RUt.nms("NBTTagCompound").getDeclaredConstructor().newInstance());
			tag = new ArrayList<String>((Set<String>) compound.getClass().getDeclaredMethod("c").invoke(compound));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException e) {
			e.printStackTrace();
		}
		return (List<String>) tag;
	}

	public static boolean getBooleanTag(ItemStack i, String path) {
		if (i == null)
			return false;
		if (i.getType() == Material.AIR)
			return false;
		Object tag = null;
		try {
			Object nmsStack = RUt.obc("inventory.CraftItemStack").getDeclaredMethod("asNMSCopy", ItemStack.class).invoke(RUt.obc("inventory.CraftItemStack"), i);
			Object compound = ((boolean) nmsStack.getClass().getDeclaredMethod("hasTag").invoke(nmsStack) ? nmsStack.getClass().getDeclaredMethod("getTag").invoke(nmsStack) : RUt.nms("NBTTagCompound").getDeclaredConstructor().newInstance());
			tag = compound.getClass().getDeclaredMethod("getBoolean", String.class).invoke(compound, path);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException e) {
			e.printStackTrace();
		}
		return (boolean) tag;
	}

	public static String getPluginTag(ItemStack i, String path) {
		if (i == null)
			return "";
		if (i.getType() == Material.AIR)
			return "";
		Object tag = null;
		try {
			Object nmsStack = RUt.obc("inventory.CraftItemStack").getDeclaredMethod("asNMSCopy", ItemStack.class).invoke(RUt.obc("inventory.CraftItemStack"), i);
			Object compound = ((boolean) nmsStack.getClass().getDeclaredMethod("hasTag").invoke(nmsStack) ? nmsStack.getClass().getDeclaredMethod("getTag").invoke(nmsStack) : RUt.nms("NBTTagCompound").getDeclaredConstructor().newInstance());
			tag = compound.getClass().getDeclaredMethod("getString", String.class).invoke(compound, path);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException e) {
			e.printStackTrace();
		}
		return (String) tag;
	}
}
