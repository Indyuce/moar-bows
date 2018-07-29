package net.Indyuce.mb.comp;

import org.bukkit.NamespacedKey;

import net.Indyuce.mb.Main;

public class Version_1_12 {
	public static NamespacedKey key(String path) {
		return new NamespacedKey(Main.plugin, "MoarBows_" + path);
	}
}
