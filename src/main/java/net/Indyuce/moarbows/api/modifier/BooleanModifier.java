package net.Indyuce.moarbows.api.modifier;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

public class BooleanModifier extends Modifier {
	private boolean value;

	public BooleanModifier(String path, boolean value) {
		super(path);

		this.value = value;
	}

	public Boolean getValue() {
		return value;
	}

	@Override
	public void setup(ConfigurationSection config) {
		config.set(getPath(), value);
	}

	@Override
	public void load(Object object) {
		Validate.isTrue(object instanceof Boolean, "Modifier requires a boolean");
		value = (boolean) object;
	}
}
