package net.Indyuce.moarbows.api.util;

import org.bukkit.configuration.ConfigurationSection;

public class LinearFormula {
	private final double base, scale, min, max;
	private final boolean hasmin, hasmax;

	/*
	 * a number which depends on the player level. it can be used as a skill
	 * modifier to make the ability better depending on the player level or as
	 * an attrribute value to make attributes increase with the player level
	 */
	public LinearFormula(double base, double scale) {
		this.base = base;
		this.scale = scale;
		min = max = 0;
		hasmin = hasmax = false;
	}

	public LinearFormula(double base, double scale, double min, double max) {
		this.base = base;
		this.scale = scale;
		this.min = min;
		this.max = max;
		hasmin = true;
		hasmax = true;
	}

	public LinearFormula(ConfigurationSection config) {
		base = config.getDouble("base");
		scale = config.getDouble("per-level");
		hasmin = config.contains("min");
		min = hasmin ? config.getDouble("min") : 0;
		hasmax = config.contains("max");
		max = hasmax ? config.getDouble("max") : 0;
	}

	public double getBase() {
		return base;
	}

	public double getScale() {
		return scale;
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

	public boolean hasMax() {
		return hasmax;
	}

	public boolean hasMin() {
		return hasmin;
	}

	public double calculate(int level) {
		double value = base + scale * (level - 1);

		if (hasmin)
			value = Math.max(min, value);

		if (hasmax)
			value = Math.min(max, value);

		return value;
	}
}
