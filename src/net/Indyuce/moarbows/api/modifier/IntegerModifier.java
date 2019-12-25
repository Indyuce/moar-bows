package net.Indyuce.moarbows.api.modifier;

import net.Indyuce.moarbows.api.util.LinearValue;

public class IntegerModifier extends DoubleModifier {
	public IntegerModifier(String path, LinearValue value) {
		super(path, value);
	}

	@Override
	public double calculate(int x) {
		return (int) super.calculate(x);
	}

	@Override
	public String getDisplay(int x) {
		return "" + calculate(x);
	}
}
