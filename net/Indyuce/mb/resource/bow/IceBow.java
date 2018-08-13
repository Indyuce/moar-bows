package net.Indyuce.mb.resource.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;
import net.Indyuce.mb.util.VersionUtils;

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
		Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, a.getLocation(), 200);
		Eff.SNOW_SHOVEL.display(0, 0, 0, .2f, 48, a.getLocation().add(0, .1, 0), 200);
		Eff.FIREWORKS_SPARK.display(0, 0, 0, .2f, 24, a.getLocation().add(0, .1, 0), 200);
		VersionUtils.sound(a.getLocation(), "ENTITY_FIREWORK_LARGE_BLAST", 3, 1);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				((LivingEntity) ent).removePotionEffect(PotionEffectType.SLOW);
				((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, amplifier));
			}
	}
}
