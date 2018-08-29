package net.Indyuce.mb.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.MoarBows;
import net.Indyuce.mb.api.BowModifier;
import net.Indyuce.mb.api.MoarBow;
import net.Indyuce.mb.util.VersionUtils;

public class Explosive_Bow extends MoarBow {
	public Explosive_Bow() {
		super(new String[] { "Arrows explode when landing, deal", "8 damage to nearby entities." }, 0, 0, "explosion_normal", new String[] { "TNT,TNT,TNT", "TNT,BOW,TNT", "TNT,TNT,TNT" });

		addModifier(new BowModifier("damage", 8));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(t, a);
	}

	@Override
	public void land(Player p, Arrow a) {
		double dmg = MoarBows.bows.getDouble("EXPLOSIVE_BOW.damage");
		a.remove();
		Eff.EXPLOSION_LARGE.display(2, 2, 2, 0, 8, a.getLocation(), 200);
		VersionUtils.sound(a.getLocation(), "ENTITY_GENERIC_EXPLODE", 3, 1);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity)
				MoarBows.damage.damageEntity(p, (LivingEntity) ent, dmg);
	}
}
