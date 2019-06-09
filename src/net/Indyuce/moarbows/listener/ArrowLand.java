package net.Indyuce.moarbows.listener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;

public class ArrowLand implements Listener {
	@EventHandler
	public void a(ProjectileHitEvent event) {
		if (event.getEntity().getType() != EntityType.ARROW || event.getHitEntity() != null)
			return;

		Arrow arrow = (Arrow) event.getEntity();
		if (!MoarBows.getArrowManager().isCustomArrow(arrow))
			return;

		// land effect
		ArrowData arrowData = MoarBows.getArrowManager().getArrowData(arrow);
		arrowData.getBow().land(arrowData.getSender(), arrow);
		MoarBows.getArrowManager().unregisterArrow(arrow);
	}
}
