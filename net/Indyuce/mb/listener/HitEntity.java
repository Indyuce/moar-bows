package net.Indyuce.mb.listener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.mb.api.ArrowManager;

public class HitEntity implements Listener {
	@EventHandler
	public void a(EntityDamageByEntityEvent e) {
		if (e.getDamager().getType() != EntityType.ARROW)
			return;

		Arrow arrow = (Arrow) e.getDamager();
		if (!ArrowManager.isCustomArrow(arrow))
			return;

		ArrowManager.getArrowData(arrow).getBow().hit(e, arrow, e.getEntity(), (Player) arrow.getShooter());
	}
}
