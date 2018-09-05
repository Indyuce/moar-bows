package net.Indyuce.moarbows.comp;

import org.bukkit.NamespacedKey;

import net.Indyuce.moarbows.MoarBows;

public class Version_1_12 {
	public static NamespacedKey key(String path) {
		return new NamespacedKey(MoarBows.plugin, "MoarBows_" + path);
	}
}
