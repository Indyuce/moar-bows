package net.Indyuce.mb.comp.worldguard;

public enum CustomFlag {
	MB_BOWS;

	public String getPath() {
		return name().toLowerCase().replace("_", "-");
	}
}
