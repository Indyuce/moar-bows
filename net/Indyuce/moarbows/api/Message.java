package net.Indyuce.moarbows.api;

import net.Indyuce.moarbows.MoarBows;

public enum Message {
	ON_COOLDOWN("This bow is on cooldown! Please wait another %left%s."),
	RECEIVE_BOW("You were given &f%bow%&e."),
	BOW_DROPPED("The bow you received was dropped as your inventory is full."),
	GUI_NAME("Bows"),
	NOT_ENOUGH_PERMS("You don't have enough permissions."),
	DISABLE_BOWS_FLAG("Bows are disabled here."),;

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
		return MoarBows.getLanguage().getTranslation(path);
	}
}