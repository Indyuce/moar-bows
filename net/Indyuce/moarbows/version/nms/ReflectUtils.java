package net.Indyuce.moarbows.version.nms;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

import net.Indyuce.moarbows.util.VersionUtils;

public class ReflectUtils {
	public static void sendPacket(Player p, Object packet) {
		try {
			Object handle = p.getClass().getMethod("getHandle").invoke(p);
			Object connection = handle.getClass().getDeclaredField("playerConnection").get(handle);
			connection.getClass().getMethod("sendPacket", nms("Packet")).invoke(connection, packet);
		} catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public static Class<?> nms(String str) throws ClassNotFoundException {
		return Class.forName("net.minecraft.server." + VersionUtils.version + "." + str);
	}

	public static Class<?> obc(String str) throws ClassNotFoundException {
		return Class.forName("org.bukkit.craftbukkit." + VersionUtils.version + "." + str);
	}

	// works above 1.13
	public static Class<?> enumTitleAction() throws SecurityException, ClassNotFoundException {
		return ReflectUtils.nms("PacketPlayOutTitle").getDeclaredClasses()[0];
	}

	// public static Class<?> enumTitleAction() throws SecurityException,
	// ClassNotFoundException {
	// return ReflectUtils.nms("PacketPlayOutTitle").getDeclaredClasses().length
	// > 0 ? ReflectUtils.nms("PacketPlayOutTitle").getDeclaredClasses()[0] :
	// ReflectUtils.nms("EnumTitleAction");
	// }

	public static Class<?> chatSerializer() throws SecurityException, ClassNotFoundException {
		return ReflectUtils.nms("IChatBaseComponent").getDeclaredClasses()[0];
	}

	// public static Class<?> chatSerializer() throws SecurityException,
	// ClassNotFoundException {
	// return ReflectUtils.nms("IChatBaseComponent").getDeclaredClasses().length
	// > 0 ? ReflectUtils.nms("IChatBaseComponent").getDeclaredClasses()[0] :
	// ReflectUtils.nms("ChatSerializer");
	// }
}
