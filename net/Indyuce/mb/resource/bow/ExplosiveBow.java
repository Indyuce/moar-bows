package net.Indyuce.mb.resource.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;
import net.Indyuce.mb.util.VersionUtils;

public class ExplosiveBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		explode(a);
	}

	@Override
	public void land(Arrow a) {
		explode(a);
	}

	private static void explode(Arrow a) {
		double dmg = Main.bows.getDouble("EXPLOSIVE_BOW.damage");
		a.remove();
		Eff.EXPLOSION_LARGE.display(2, 2, 2, 0, 8, a.getLocation(), 200);
		VersionUtils.sound(a.getLocation(), "ENTITY_GENERIC_EXPLODE", 3, 1);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity)
				((LivingEntity) ent).damage(dmg);
	}
}
