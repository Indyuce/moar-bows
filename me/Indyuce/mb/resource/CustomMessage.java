package me.Indyuce.mb.resource;

public enum CustomMessage {
	ON_COOLDOWN("This bow is on cooldown!"),
	CONFIG_RELOAD("Configuration file reloaded."),
	BOW_LIST("§aList of all bows:"),
	USE("use"),
	GIVE_BOW_TO_YOURSELF("Gave you the bow %BOW%."),
	GIVE_BOW_TO_OTHER_PLAYER("Gave %PLAYER% the bow %BOW%."),
	BOW_DROPPED_ON_GROUND("It was dropped on the ground due to full inventory."),
	COULDNT_FIND_PLAYER("Couldn't find the player %PLAYER%."),
	COULDNT_FIND_BOW("Couldn't find the bow %BOW%."),
	GUI_NAME("Bows"),
	NOT_ENOUGH_PERMISSIONS("You don't have enough permissions."),
	DISABLE_BOWS_FLAG("Bows are disabled here."),;

	public Object value;

	private CustomMessage(Object value) {
		this.value = value;
	}
}