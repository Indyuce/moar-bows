package net.Indyuce.moarbows.comp;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.manager.ArrowManager;

public class ArrowLand_v1_8 implements Listener {
	@EventHandler
	private void a(ProjectileHitEvent event) {
		if (event.getEntityType() != EntityType.ARROW)
			return;

		Arrow arrow = (Arrow) event.getEntity();
		if (!ArrowManager.isCustomArrow(arrow))
			return;

		new BukkitRunnable() {
			ArrowData arrowData = ArrowManager.getArrowData(arrow);

			public void run() {
				try {
					Object entityArrow = arrow.getClass().getDeclaredMethod("getHandle").invoke(arrow);
					if (entityArrow.getClass().getField("inGround").getBoolean(entityArrow)) {
						arrowData.getBow().land(arrowData.getSender(), arrow);
						ArrowManager.unregisterArrow(arrow);
					}
				} catch (NoSuchFieldException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}.runTaskLater(MoarBows.plugin, 0);
	}
}
