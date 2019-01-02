package net.Indyuce.moarbows.listener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.moarbows.manager.ArrowManager;

public class HitEntity implements Listener {
	@EventHandler
	public void a(EntityDamageByEntityEvent event) {
		if (event.getDamager().getType() != EntityType.ARROW)
			return;

		Arrow arrow = (Arrow) event.getDamager();
		if (ArrowManager.isCustomArrow(arrow))
			ArrowManager.getArrowData(arrow).getBow().hit(event, arrow, event.getEntity(), (Player) arrow.getShooter());
	}
}
