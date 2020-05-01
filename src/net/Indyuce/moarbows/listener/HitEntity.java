package net.Indyuce.moarbows.listener;

import java.util.Optional;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;

public class HitEntity implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void a(EntityDamageByEntityEvent event) {
		if (event.getDamager().getType() != EntityType.ARROW)
			return;

		Arrow arrow = (Arrow) event.getDamager();
		Optional<ArrowData> opt = MoarBows.plugin.getArrowManager().getArrowData(arrow);
		if (!opt.isPresent())
			return;

		ArrowData data = opt.get();
		data.getBow().whenHit(event, data, event.getEntity());
		MoarBows.plugin.getArrowManager().unregisterArrow(arrow);
	}
}
