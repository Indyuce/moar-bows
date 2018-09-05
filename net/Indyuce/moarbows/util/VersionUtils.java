package net.Indyuce.moarbows.util;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.MoarBows;

@SuppressWarnings("deprecation")
public class VersionUtils implements Listener {
	public static String version;
	public static String[] splitVersion;

	public static boolean isBelow(int... ver) {
		return Integer.parseInt(splitVersion[0].replace("v", "")) <= ver[0] && Integer.parseInt(splitVersion[1]) <= ver[1];
	}

	public static double[] getBoundingBox(Entity p) {
		Object pHandle;
		Object boundingBox;
		try {
			pHandle = p.getClass().getMethod("getHandle").invoke(p);
			boundingBox = pHandle.getClass().getMethod("getBoundingBox").invoke(pHandle);
			for (String c : new String[] { "a", "b", "c", "d", "e", "f" })
				boundingBox.getClass().getField(c).setAccessible(true);
			return new double[] { boundingBox.getClass().getDeclaredField("a").getDouble(boundingBox), boundingBox.getClass().getDeclaredField("b").getDouble(boundingBox), boundingBox.getClass().getDeclaredField("c").getDouble(boundingBox), boundingBox.getClass().getDeclaredField("d").getDouble(boundingBox), boundingBox.getClass().getDeclaredField("e").getDouble(boundingBox), boundingBox.getClass().getDeclaredField("f").getDouble(boundingBox) };
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ItemStack[] getStatEquipment(Player p) {
		if (version.startsWith("v1_8")) {
			return new ItemStack[] { p.getInventory().getItemInHand(), p.getInventory().getHelmet(), p.getInventory().getChestplate(), p.getInventory().getLeggings(), p.getInventory().getBoots() };
		}
		return new ItemStack[] { p.getInventory().getItemInMainHand(), p.getInventory().getItemInOffHand(), p.getInventory().getHelmet(), p.getInventory().getChestplate(), p.getInventory().getLeggings(), p.getInventory().getBoots() };
	}

	public static ItemStack[] getItemsInHand(Player p) {
		if (isBelow(1, 8))
			return new ItemStack[] { p.getItemInHand() };
		return new ItemStack[] { p.getInventory().getItemInMainHand(), p.getInventory().getItemInOffHand() };
	}

	public static ItemStack getMainItem(Player p) {
		ItemStack i = p.getItemInHand();
		if (!version.startsWith("v1_8"))
			i = p.getInventory().getItemInMainHand();
		return i;
	}

	static String getModifiedSound(String path) {
		String sound = path;
		if (version.startsWith("v1_8")) {
			sound = sound.replace("BLOCK_FIRE_EXTINGUISH", "FIZZ");
			sound = sound.replace("BLOCK_NOTE_HAT", "NOTE_STICKS");
			sound = sound.replace("ENTITY_SHEEP_DEATH", "SHEEP_IDLE");
			sound = sound.replace("ENTITY_LLAMA_ANGRY", "HORSE_HIT");
			sound = sound.replace("BLOCK_BREWING_STAND_BREW", "CREEPER_HISS");
			sound = sound.replace("ENTITY_SHULKER_TELEPORT", "ENDERMAN_TELEPORT");
			sound = sound.replace("ENTITY_ZOMBIE_ATTACK_IRON_DOOR", "ZOMBIE_METAL");
			sound = sound.replace("BLOCK_GRAVEL_BREAK", "DIG_GRAVEL");
			sound = sound.replace("BLOCK_SNOW_BREAK", "DIG_SNOW");
			sound = sound.replace("BLOCK_GRAVEL_BREAK", "DIG_GRAVEL");
			sound = sound.replace("ENTITY_PLAYER_LEVELUP", "LEVEL_UP");
			sound = sound.replace("ENTITY_SNOWBALL_THROW", "SHOOT_ARROW");
			sound = sound.replace("ENTITY_EGG_THROW", "SHOOT_ARROW");
			sound = sound.replace("ENTITY_ZOMBIE_ATTACK_DOOR_WOOD", "ZOMBIE_WOOD_BREAK");
			
			sound = sound.replace("ENTITY_", "");
			sound = sound.replace("GENERIC_", "");
			sound = sound.replace("BLOCK_", "");
			sound = sound.replace("BLAZE_AMBIENT", "BLAZE_BREATH");
			sound = sound.replace("_AMBIENT", "");
			sound = sound.replace("_BREAK", "");
			sound = sound.replace("PLAYER_ATTACK_CRIT", "ITEM_BREAK");
			sound = sound.replace("ENDERMEN", "ENDERMAN");
			sound = sound.replace("ARROW_SHOOT", "SHOOT_ARROW");
			sound = sound.replace("UI_BUTTON_", "");
			sound = sound.replace("ENDERMAN_HURT", "ENDERMAN_HIT");
			sound = sound.replace("BLAZE_HURT", "BLAZE_HIT");
			sound = sound.replace("_FLAP", "_WINGS");
			sound = sound.replace("EXPERIENCE_", "");
		}
		return sound;
	}

	public static void sound(Location loc, String sound, float vol, float pitch) {
		String path = getModifiedSound(sound);
		try {
			loc.getWorld().playSound(loc, Sound.valueOf(path), vol, pitch);
		} catch (Exception e) {
			MoarBows.plugin.getLogger().log(Level.WARNING, "No sound called " + sound + "found");
		}
	}

	public static void sound(Player p, String sound, float vol, float pitch) {
		String path = getModifiedSound(sound);
		try {
			p.playSound(p.getLocation(), Sound.valueOf(path), vol, pitch);
		} catch (Exception e) {
			MoarBows.plugin.getLogger().log(Level.WARNING, "No sound called " + sound + "found");
		}
	}
}
