package net.Indyuce.mb.bow;

import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.mb.MoarBows;
import net.Indyuce.mb.api.BowModifier;
import net.Indyuce.mb.api.MoarBow;

public class Silver_Bow extends MoarBow {
	public Silver_Bow() {
		super(new String[] { "Arrows deal 40% additional damage." }, 0, 0, "crit", new String[] { "IRON_INGOT,IRON_INGOT,IRON_INGOT", "IRON_INGOT,BOW,IRON_INGOT", "IRON_INGOT,IRON_INGOT,IRON_INGOT" });

		addModifier(new BowModifier("damage-percent", 40), new BowModifier("block-effect-iD", 12));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		if (!(t instanceof LivingEntity))
			return;

		int id = MoarBows.bows.getInt("SILVER_BOW.block-effect-id");
		p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, id);
		p.getWorld().playEffect(p.getLocation().add(0, 1, 0), Effect.STEP_SOUND, id);
		e.setDamage(e.getDamage() * (1. + MoarBows.bows.getDouble("SILVER_BOW.damagePercent") / 100.));
	}
}
