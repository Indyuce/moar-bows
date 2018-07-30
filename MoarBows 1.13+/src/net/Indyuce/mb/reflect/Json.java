package net.Indyuce.mb.reflect;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

public class Json {
	public static void json(Player p, String msg) {
		try {
			Object chatMsg = RUt.nms("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, msg);
			Object titlePacket = RUt.nms("PacketPlayOutChat").getConstructor(RUt.nms("IChatBaseComponent")).newInstance(chatMsg);
			RUt.sendPacket(p, titlePacket);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
