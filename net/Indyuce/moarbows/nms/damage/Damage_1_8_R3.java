package net.Indyuce.mb.nms.damage;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.DamageSource;

public class Damage_1_8_R3 implements Damage {
	@Override
	public void damageEntity(Player p, LivingEntity t, double value) {
		((CraftLivingEntity) t).getHandle().damageEntity(DamageSource.playerAttack(((CraftPlayer) p).getHandle()), (float) value);
	}
}
