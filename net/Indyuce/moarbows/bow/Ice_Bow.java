package net.Indyuce.moarbows.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Indyuce.moarbows.ParticleEffect;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.version.VersionSound;

public class Ice_Bow extends MoarBow {
	public Ice_Bow() {
		super(new String[] { "Shoots ice arrows that cause an ice", "explosion upon landing, temporarily", "slowing every nearby entity." }, 0, 0, "snow_shovel", new String[] { "ICE,ICE,ICE", "ICE,BOW,ICE", "ICE,ICE,ICE" });

		addModifier(new BowModifier("amplifier", 2), new BowModifier("duration", 5));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(t, a);
	}

	@Override
	public void land(Player p, Arrow a) {
		int duration = (int) (getValue("duration") * 20);
		int amplifier = (int) getValue("amplifier");
		a.remove();
		ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, a.getLocation(), 200);
		ParticleEffect.SNOW_SHOVEL.display(0, 0, 0, .2f, 48, a.getLocation().add(0, .1, 0), 200);
		ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, .2f, 24, a.getLocation().add(0, .1, 0), 200);
		a.getWorld().playSound(p.getLocation(), VersionSound.ENTITY_FIREWORK_LARGE_BLAST.getSound(), 2, 1);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				((LivingEntity) ent).removePotionEffect(PotionEffectType.SLOW);
				((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, amplifier));
			}
	}
}
