package net.Indyuce.mb.reflect;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Entity;

public class BoundingBox {
	public static double[] get(Entity p) {
		Object pHandle;
		Object boundingBox;
		try {
			pHandle = p.getClass().getMethod("getHandle").invoke(p);
			boundingBox = pHandle.getClass().getMethod("getBoundingBox").invoke(pHandle);
			for (String c : new String[] { "a", "b", "c", "d", "e", "f" })
				boundingBox.getClass().getField(c).setAccessible(true);
			return new double[] { boundingBox.getClass().getDeclaredField("a").getDouble(boundingBox), boundingBox.getClass().getDeclaredField("b").getDouble(boundingBox), boundingBox.getClass().getDeclaredField("c").getDouble(boundingBox), boundingBox.getClass().getDeclaredField("d").getDouble(boundingBox), boundingBox.getClass().getDeclaredField("e").getDouble(boundingBox), boundingBox.getClass().getDeclaredField("f").getDouble(boundingBox) };
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}
}
