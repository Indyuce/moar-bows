package net.Indyuce.moarbows.bow;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.moarbows.Eff;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.util.VersionUtils;

public class Cupidons_Bow extends MoarBow {
	public Cupidons_Bow() {
		super("CUPIDONS_BOW", "Cupidon's Bow", new String[] { "Arrows heal players for 3 hearts.", "Also unmarks (&nMarked Bow&7) players." }, 0, 0, "heart", new String[] { "SPECKLED_MELON,SPECKLED_MELON,SPECKLED_MELON", "SPECKLED_MELON,BOW,SPECKLED_MELON", "SPECKLED_MELON,SPECKLED_MELON,SPECKLED_MELON" });

		addModifier(new BowModifier("heal", 4));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		if (p.getType() != EntityType.PLAYER)
			return;

		e.setDamage(0);
		Eff.HEART.display(1, 1, 1, 0, 16, p.getLocation().add(0, 1, 0), 200);
		VersionUtils.sound(p.getLocation(), "ENTITY_BLAZE_AMBIENT", 2, 2);
		Marked_Bow.marked.remove(p.getUniqueId());
		double max = ((Player) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
		double health = ((Player) p).getHealth();
		double heal = MoarBows.bows.getDouble("CUPIDONS_BOW.heal");
		if (health + heal > max) {
			((Player) p).setHealth(max);
			return;
		}
		((Player) p).setHealth(health + heal);
	}
}
