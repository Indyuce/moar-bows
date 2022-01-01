package net.Indyuce.moarbows.listener;

import java.util.Optional;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;

public class ArrowLand implements Listener {

	@EventHandler()
	public void a(ProjectileHitEvent event) {
		if (event.getEntity().getType() != EntityType.ARROW || event.getHitEntity() != null)
			return;

		Arrow arrow = (Arrow) event.getEntity();
		Optional<ArrowData> opt = MoarBows.plugin.getArrowManager().getArrowData(arrow);
		if (!opt.isPresent())
			return;

		// land effect
		ArrowData arrowData = opt.get();
		arrowData.getBow().whenLand(arrowData);
		MoarBows.plugin.getArrowManager().unregisterArrow(arrow);
	}
}
