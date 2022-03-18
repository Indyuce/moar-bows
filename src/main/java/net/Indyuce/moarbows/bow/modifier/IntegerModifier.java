package net.Indyuce.moarbows.bow.modifier;

import net.Indyuce.moarbows.util.LinearFormula;

public class IntegerModifier extends DoubleModifier {
	public IntegerModifier(String path, LinearFormula value) {
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
