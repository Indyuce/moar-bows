package net.Indyuce.mb.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import net.Indyuce.mb.Eff;

public class MathUtils {
	public static double tronc(double x, int n) {
		double pow = Math.pow(10.0, n);
		return Math.floor(x * pow) / pow;
	}

	public static void drawVec(Vector v, Location loc, Color color) {
		for (double j = 0; j < 1; j += .1)
			Eff.REDSTONE.display(new Eff.OrdinaryColor(color), loc.clone().add(v.clone().multiply(j)), 100);
	}

	public static Vector rotAxisX(Vector v, double a) {
		double y = v.getY() * Math.cos(a) - v.getZ() * Math.sin(a);
		double z = v.getY() * Math.sin(a) + v.getZ() * Math.cos(a);
		return v.setY(y).setZ(z);
	}

	public static Vector rotAxisY(Vector v, double b) {
		double x = v.getX() * Math.cos(b) + v.getZ() * Math.sin(b);
		double z = v.getX() * -Math.sin(b) + v.getZ() * Math.cos(b);
		return v.setX(x).setZ(z);
	}

	public static Vector rotAxisZ(Vector v, double c) {
		double x = v.getX() * Math.cos(c) - v.getY() * Math.sin(c);
		double y = v.getX() * Math.sin(c) + v.getY() * Math.cos(c);
		return v.setX(x).setY(y);
	}

	public static Vector rotateFunc(Vector v, Location loc) {
		double yaw = loc.getYaw() / 180 * Math.PI;
		double pitch = loc.getPitch() / 180 * Math.PI;
		v = rotAxisX(v, pitch);
		v = rotAxisY(v, -yaw);
		return v;
	}
}
