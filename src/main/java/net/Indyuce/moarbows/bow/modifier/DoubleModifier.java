package net.Indyuce.moarbows.bow.modifier;

import java.text.DecimalFormat;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.ConfigurationSection;

import net.Indyuce.moarbows.util.LinearFormula;

public class DoubleModifier extends Modifier {
	private LinearFormula value;
	
	private static final DecimalFormat modifierFormat = new DecimalFormat("0.#");

	public DoubleModifier(String path, LinearFormula value) {
		super(path);

		this.value = value;
	}

	public LinearFormula getValue() {
		return value;
	}

	public double calculate(int x) {
		return value.calculate(x);
	}

	@Override
	public void setup(ConfigurationSection config) {
		config.set(getPath() + ".base", value.getBase());
		config.set(getPath() + ".per-level", value.getScale());
		if (value.hasMax())
			config.set(getPath() + ".max", value.getMax());
		if (value.hasMin())
			config.set(getPath() + ".min", value.getMin());
	}

	@Override
	public void load(Object object) {
		Validate.isTrue(object instanceof ConfigurationSection, "Modifier requires a config section");
		value = new LinearFormula((ConfigurationSection) object);
	}

	public String getDisplay(int x) {
		return modifierFormat.format(calculate(x));
	}
}
