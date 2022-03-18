package net.Indyuce.moarbows.bow.modifier;

import org.bukkit.configuration.ConfigurationSection;

public abstract class Modifier {
	private final String path;

	public Modifier(String path) {
		this.path = path.replace("_", "-").toLowerCase().replace(" ", "-");
	}

	public String getPath() {
		return path;
	}

	public abstract Object getValue();

	public abstract void setup(ConfigurationSection config);

	public abstract void load(Object object);
}
