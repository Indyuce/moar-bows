package net.Indyuce.moarbows.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.Indyuce.moarbows.Eff;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.util.VersionUtils;

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
		int duration = MoarBows.bows.getInt("CORROSIVE_BOW.duration") * 20;
		a.remove();
		Eff.SLIME.display(2, 2, 2, 0, 48, a.getLocation(), 200);
		Eff.VILLAGER_HAPPY.display(2, 2, 2, 0, 32, a.getLocation(), 200);
		VersionUtils.sound(a.getLocation(), "BLOCK_BREWING_STAND_BREW", 3, 0);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				((LivingEntity) ent).removePotionEffect(PotionEffectType.POISON);
				((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, 1));
			}
	}
}
