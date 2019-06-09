package net.Indyuce.moarbows.bow;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Corrosive_Bow extends MoarBow {
	public Corrosive_Bow() {
		super(new String[] { "Arrows poison your targets and", "nearby entities for 6 seconds." }, 0, 0, "villager_happy", new String[] { "AIR,SLIME_BALL,AIR", "SLIME_BALL,BOW,SLIME_BALL", "AIR,SLIME_BALL,AIR" });

		addModifier(new BowModifier("duration", 6));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(t, a);
	}

	@Override
	public void land(Player p, Arrow a) {
		int duration = (int) (getValue("duration") * 20);
		a.remove();
		a.getWorld().spawnParticle(Particle.SLIME, a.getLocation(), 48, 2, 2, 2);
		a.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, a.getLocation(), 32, 2, 2, 2);
		a.getWorld().playSound(a.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 3, 0);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				((LivingEntity) ent).removePotionEffect(PotionEffectType.POISON);
				((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, 1));
			}
	}
}
