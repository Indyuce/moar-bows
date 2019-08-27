package net.Indyuce.moarbows.api.modifier;

import java.text.DecimalFormat;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

import net.Indyuce.moarbows.api.LinearValue;

public class DoubleModifier extends Modifier {
	private LinearValue value;
	
	private static final DecimalFormat modifierFormat = new DecimalFormat("0.#");

	public DoubleModifier(String path, LinearValue value) {
		super(path);

		this.value = value;
	}

	public LinearValue getValue() {
		return value;
	}

	public double calculate(int x) {
		return value.calculate(x);
	}

	@Override
	public void setup(ConfigurationSection config) {
		config.set(getPath() + ".base", value.getBaseValue());
		config.set(getPath() + ".per-level", value.getPerLevel());
		if (value.hasMax())
			config.set(getPath() + ".max", value.getMax());
		if (value.hasMin())
			config.set(getPath() + ".min", value.getMin());
	}

	@Override
	public void load(Object object) {
		Validate.isTrue(object instanceof ConfigurationSection, "Modifier requires a config section");
		value = new LinearValue((ConfigurationSection) object);
	}

	public String getDisplay(int x) {
		return modifierFormat.format(calculate(x));
	}
}
