package net.Indyuce.moarbows.listener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;

public class HitEntity implements Listener {
	@EventHandler
	public void a(EntityDamageByEntityEvent event) {
		if (event.getDamager().getType() != EntityType.ARROW)
			return;

		Arrow arrow = (Arrow) event.getDamager();
		if (MoarBows.plugin.getArrowManager().isCustomArrow(arrow)) {
			ArrowData data = MoarBows.plugin.getArrowManager().getArrowData(arrow);
			data.getBow().whenHit(event, data, event.getEntity());
		}
	}
}
