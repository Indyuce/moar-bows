package net.Indyuce.mb.bow;

import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.mb.MoarBows;
import net.Indyuce.mb.api.BowModifier;
import net.Indyuce.mb.api.MoarBow;
import net.Indyuce.mb.util.Utils;

public class Hunter_Bow extends MoarBow {
	public Hunter_Bow() {
		super(new String[] { "Arrows deal 75% additional", "damage to friendly mobs." }, 0, 0, "redstone", new String[] { "RAW_CHICKEN,RAW_BEEF,RAW_CHICKEN", "RAW_BEEF,BOW,RAW_BEEF", "RAW_CHICKEN,RAW_BEEF,RAW_CHICKEN" });

		addModifier(new BowModifier("damage-percent", 75));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		if (!Utils.isFriendly(p))
			return;

		e.setDamage(e.getDamage() * (1 + MoarBows.bows.getDouble("HUNTER_BOW.damage-percent") / 100));
		p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 55);
	}
}
