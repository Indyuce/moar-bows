package net.Indyuce.moarbows.bow;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Explosive_Bow extends MoarBow {
	public Explosive_Bow() {
		super(new String[] { "Arrows explode when landing, deal", "8 damage to nearby entities." }, 0, 0, "explosion_normal", new String[] { "TNT,TNT,TNT", "TNT,BOW,TNT", "TNT,TNT,TNT" });

		addModifier(new BowModifier("damage", 8));
	}

	@Override
	public void hit(EntityDamageByEntityEvent event, Arrow arrow, Entity player, Player target) {
		land(target, arrow);
	}

	@Override
	public void land(Player player, Arrow arrow) {
		double dmg = getValue("damage");
		arrow.remove();
		arrow.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, arrow.getLocation(), 16, 1.5, 1.5, 1.5);
		arrow.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, arrow.getLocation(), 48, 0, 0, 0, .4);
		arrow.getWorld().playSound(arrow.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 3, 1);
		for (Entity ent : arrow.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity)
				((LivingEntity) ent).damage(dmg, player);
	}
}
