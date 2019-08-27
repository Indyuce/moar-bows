package net.Indyuce.moarbows.api;

import org.bukkit.configuration.ConfigurationSection;

public class LinearValue {
	private final double base, perLevel;

	private double min, max;
	private boolean hasmin, hasmax;

	/*
	 * a number which depends on the player level. it can be used as a skill
	 * modifier to make the ability better depending on the player level or as
	 * an attrribute value to make attributes increase with the player level
	 */
	public LinearValue(double base, double perLevel) {
		this.base = base;
		this.perLevel = perLevel;
	}

	public LinearValue(double base, double perLevel, double min, double max) {
		this.base = base;
		this.perLevel = perLevel;
		this.min = min;
		this.max = max;
		hasmin = true;
		hasmax = true;
	}

	public LinearValue(ConfigurationSection config) {
		this.base = config.getDouble("base");
		this.perLevel = config.getDouble("per-level");
		if (config.contains("min")) {
			this.min = config.getDouble("min");
			this.hasmin = true;
		}

		if (config.contains("max")) {
			this.max = config.getDouble("max");
			this.hasmax = true;
		}
	}

	public double getBaseValue() {
		return base;
	}

	public double getPerLevel() {
		return perLevel;
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
		double value = base + perLevel * (level - 1);

		if (hasmin)
			value = Math.max(min, value);

		if (hasmax)
			value = Math.min(max, value);

		return value;
	}
}
