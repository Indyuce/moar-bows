package net.Indyuce.moarbows.bow.list;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Indyuce.moarbows.bow.ArrowData;
import net.Indyuce.moarbows.bow.MoarBow;
import net.Indyuce.moarbows.bow.modifier.DoubleModifier;
import net.Indyuce.moarbows.bow.particle.ParticleData;
import net.Indyuce.moarbows.util.LinearFormula;

public class Corrosive_Bow extends MoarBow {
	public Corrosive_Bow() {
		super(new String[] { "Arrows poison your targets and", "nearby entities for &c{duration} &7seconds." },
				new ParticleData(Particle.VILLAGER_HAPPY), new String[] { "AIR,SLIME_BALL,AIR", "SLIME_BALL,BOW,SLIME_BALL", "AIR,SLIME_BALL,AIR" });

		addModifier(new DoubleModifier("cooldown", new LinearFormula(0, 0)), new DoubleModifier("duration", new LinearFormula(4, .2)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		return true;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
		whenLand(data);
	}

	@Override
	public void whenLand(ArrowData data) {
		int duration = (int) (data.getDouble("duration") * 20);
		data.getArrow().remove();
		data.getArrow().getWorld().spawnParticle(Particle.SLIME, data.getArrow().getLocation(), 48, 2, 2, 2);
		data.getArrow().getWorld().spawnParticle(Particle.VILLAGER_HAPPY, data.getArrow().getLocation(), 32, 2, 2, 2);
		data.getArrow().getWorld().playSound(data.getArrow().getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 3, 0);
		for (Entity ent : data.getArrow().getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				((LivingEntity) ent).removePotionEffect(PotionEffectType.POISON);
				((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, 1));
			}

	}
}
