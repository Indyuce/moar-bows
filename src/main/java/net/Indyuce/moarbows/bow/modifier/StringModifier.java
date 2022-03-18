package net.Indyuce.moarbows.bow.modifier;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

public class StringModifier extends Modifier {
	private String value;

	public StringModifier(String path, String value) {
		super(path);

		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public void setup(ConfigurationSection config) {
		config.set(getPath(), value);
	}

	@Override
	public void load(Object object) {
		Validate.isTrue(object instanceof String, "Modifier requires a string");
		value = new String((String) object);
	}
}
