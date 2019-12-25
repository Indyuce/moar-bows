package net.Indyuce.moarbows.api.util;

import org.bukkit.ChatColor;

import net.Indyuce.moarbows.MoarBows;

public enum Message {
	ON_COOLDOWN("&cThis bow is on cooldown! Please wait another %left%s."),
	RECEIVE_BOW("&eYou were given &f%bow%&e."),
	GUI_NAME("Bows"),
	NOT_ENOUGH_PERMS("&cYou don't have enough permissions."),
	DISABLE_BOWS_FLAG("&cBows are disabled here."),;

	private Object value;
	private String path;

	private Message(Object value) {
		this.value = value;
		this.path = name().toLowerCase().replace("_", "-");
	}

	public Object getDefaultValue() {
		return value;
	}

	public String translate() {
		return ChatColor.translateAlternateColorCodes('&', MoarBows.plugin.getLanguage().getTranslation(path));
	}
}