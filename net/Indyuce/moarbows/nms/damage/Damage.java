package net.Indyuce.mb.nms.damage;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface Damage {
	public void damageEntity(Player p, LivingEntity t, double value);
}
