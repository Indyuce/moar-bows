package net.Indyuce.mb.resource;

public enum CustomFlag {
	MB_BOWS,
	;
	
	public String getPath() {
		return name().toLowerCase().replace("_", "-");
	}
}
