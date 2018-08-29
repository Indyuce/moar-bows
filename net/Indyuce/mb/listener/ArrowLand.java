package net.Indyuce.mb.listener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import net.Indyuce.mb.api.ArrowData;
import net.Indyuce.mb.api.ArrowManager;

public class ArrowLand implements Listener {
	@EventHandler
	public void a(ProjectileHitEvent e) {
		if (e.getEntity().getType() != EntityType.ARROW || e.getHitEntity() != null)
			return;

		Arrow arrow = (Arrow) e.getEntity();
		if (!ArrowManager.isCustomArrow(arrow))
			return;

		// land effect
		ArrowData arrowData = ArrowManager.getArrowData(arrow);
		arrowData.getBow().land(arrowData.getSender(), arrow);
		ArrowManager.unregisterArrow(arrow);
	}
}
