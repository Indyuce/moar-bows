package net.Indyuce.mb.api;

public class BowModifier {
	private String p;
	private Object df;

	public BowModifier(String path, Object defaultValue) {
		this.p = path;
		this.df = defaultValue;
	}

	public String getPath() {
		return p;
	}

	public Object getDefaultValue() {
		return df;
	}
}
