package net.Indyuce.moarbows.bow;

import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Hunter_Bow extends MoarBow {
	public Hunter_Bow() {
		super(new String[] { "Arrows deal 75% additional", "damage to friendly mobs." }, 0, 0, "redstone", new String[] { "CHICKEN,BEEF,CHICKEN", "BEEF,BOW,BEEF", "CHICKEN,BEEF,CHICKEN" });

		addModifier(new BowModifier("damage-percent", 75));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		if (p instanceof Monster || !(p instanceof LivingEntity))
			return;

		e.setDamage(e.getDamage() * (1 + getValue("damage-percent") / 100));
		p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 55);
	}
}
