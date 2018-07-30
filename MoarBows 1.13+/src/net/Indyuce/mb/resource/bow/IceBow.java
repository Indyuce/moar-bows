package net.Indyuce.mb.resource.bow;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;

public class IceBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(a);
	}

	@Override
	public void land(Arrow a) {
		int duration = Main.bows.getInt("ICE_BOW.duration") * 20;
		int amplifier = Main.bows.getInt("ICE_BOW.amplifier");
		a.remove();
		a.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, a.getLocation(), 0);
		a.getWorld().spawnParticle(Particle.SNOW_SHOVEL, a.getLocation(), 48, 0, 0, 0, .2);
		a.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, a.getLocation(), 24, 0, 0, 0, .2);
		a.getWorld().playSound(a.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 1);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				((LivingEntity) ent).removePotionEffect(PotionEffectType.SLOW);
				((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, amplifier));
			}
	}
}
