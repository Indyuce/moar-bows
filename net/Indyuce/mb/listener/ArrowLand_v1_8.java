package net.Indyuce.mb.listener;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.mb.Main;
import net.minecraft.server.v1_8_R3.EntityArrow;

public class ArrowLand_v1_8 implements Listener {
	@EventHandler
	private void onProjectileHit(final ProjectileHitEvent e) {
		if (e.getEntityType() != EntityType.ARROW)
			return;

		Arrow t = (Arrow) e.getEntity();
		if (ShootBow.type.get(t.getUniqueId()) == null)
			return;
		if (!(t.getShooter() instanceof Player))
			return;
		
		new BukkitRunnable() {
			public void run() {
				try {
					EntityArrow entityArrow = ((CraftArrow) t).getHandle();
					Field fieldX = EntityArrow.class.getDeclaredField("d");
					Field fieldY = EntityArrow.class.getDeclaredField("e");
					Field fieldZ = EntityArrow.class.getDeclaredField("f");

					fieldX.setAccessible(true);
					fieldY.setAccessible(true);
					fieldZ.setAccessible(true);

					int x = fieldX.getInt(entityArrow);
					int y = fieldY.getInt(entityArrow);
					int z = fieldZ.getInt(entityArrow);
					if (x != -1 && y != -1 && z != -1) {
						
						// land effect
						ShootBow.type.get(t.getUniqueId()).getBowClass().land(t);
						ShootBow.type.remove(t.getUniqueId());
					}
				} catch (NoSuchFieldException | IllegalAccessException | SecurityException | IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
		}.runTaskLater(Main.plugin, 0);
	}
}
