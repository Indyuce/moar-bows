package net.Indyuce.moarbows.bow;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Cupidons_Bow extends MoarBow {
	public Cupidons_Bow() {
		super("CUPIDONS_BOW", "&fCupidon's Bow", new String[] { "Arrows heal players for 3 hearts.", "Also unmarks (&nMarked Bow&7) players." }, 0, 0, "heart", new String[] { "GLISTERING_MELON_SLICE,GLISTERING_MELON_SLICE,GLISTERING_MELON_SLICE", "GLISTERING_MELON_SLICE,BOW,GLISTERING_MELON_SLICE", "GLISTERING_MELON_SLICE,GLISTERING_MELON_SLICE,GLISTERING_MELON_SLICE" });

		addModifier(new BowModifier("heal", 4));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		if (p.getType() != EntityType.PLAYER)
			return;

		e.setDamage(0);
		p.getWorld().spawnParticle(Particle.HEART, p.getLocation().add(0, 1, 0), 16, 1, 1, 1);
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2, 2);
		Marked_Bow.marked.remove(p.getUniqueId());
		double max = ((Player) p).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
		double health = ((Player) p).getHealth();
		double heal = getValue("heal");
		if (health + heal > max) {
			((Player) p).setHealth(max);
			return;
		}
		((Player) p).setHealth(health + heal);
	}
}
