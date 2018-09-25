package net.Indyuce.moarbows.comp;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.ArrowManager;

public class ArrowLand_v1_8 implements Listener {
	@EventHandler
	private void a(ProjectileHitEvent e) {
		if (e.getEntityType() != EntityType.ARROW)
			return;

		Arrow arrow = (Arrow) e.getEntity();
		if (!ArrowManager.isCustomArrow(arrow))
			return;

		new BukkitRunnable() {
			ArrowData arrowData = ArrowManager.getArrowData(arrow);

			public void run() {
				try {
					Object entityArrow = arrow.getClass().getDeclaredMethod("getHandle").invoke(arrow);

					Field fieldX = entityArrow.getClass().getDeclaredField("d");
					Field fieldY = entityArrow.getClass().getDeclaredField("e");
					Field fieldZ = entityArrow.getClass().getDeclaredField("f");

					fieldX.setAccessible(true);
					fieldY.setAccessible(true);
					fieldZ.setAccessible(true);

					int x = fieldX.getInt(entityArrow);
					int y = fieldY.getInt(entityArrow);
					int z = fieldZ.getInt(entityArrow);
					if (x != -1 && y != -1 && z != -1) {

						// land effect
						arrowData.getBow().land(arrowData.getSender(), arrow);
						ArrowManager.unregisterArrow(arrow);
					}
				} catch (NoSuchFieldException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e1) {
					e1.printStackTrace();
				}
			}
		}.runTaskLater(MoarBows.plugin, 0);
	}
}
