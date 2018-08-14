package net.Indyuce.mb.listener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ArrowLand_v1_9 implements Listener {
	@EventHandler
	public void a(ProjectileHitEvent e) {
		if (e.getEntity().getType() != EntityType.ARROW)
			return;
	
		if (e.getHitEntity() != null)
			return;
		
		Arrow t = (Arrow) e.getEntity();
		if (ShootBow.type.get(t.getUniqueId()) == null)
			return;
		if (!(t.getShooter() instanceof Player))
			return;
		
		// land effect
		ShootBow.type.get(t.getUniqueId()).getBowClass().land(t);
		ShootBow.type.remove(t.getUniqueId());
	}
}
