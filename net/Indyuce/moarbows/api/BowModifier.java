package net.Indyuce.moarbows.api;

public class BowModifier {
	private String path;
	private Object defaultValue;

	public BowModifier(String path, Object defaultValue) {
		this.path = path;
		this.defaultValue = defaultValue;
	}

	public String getPath() {
		return path;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}
}
