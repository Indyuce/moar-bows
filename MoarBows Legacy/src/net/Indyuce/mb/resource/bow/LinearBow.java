package net.Indyuce.mb.resource.bow;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
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
import net.Indyuce.mb.util.Utils;
import net.Indyuce.mb.util.VersionUtils;

public class LinearBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		double dmg = Main.bows.getDouble("LINEAR_BOW.damage");
		e.setCancelled(true);
		if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
			return false;

		VersionUtils.sound(p.getLocation(), "ENTITY_ARROW_SHOOT", 2, 0);
		int range = (int) (56 * e.getForce());
		Location loc = p.getEyeLocation();
		for (double j = 0; j < range; j++) {
			loc.add(p.getEyeLocation().getDirection());
			Eff.REDSTONE.display(new Eff.OrdinaryColor(Color.SILVER), loc, 200);
			if (loc.getBlock().getType().isSolid())
				break;
			for (Entity t : p.getNearbyEntities(30, 30, 30))
				if (Utils.canDmgEntity(p, loc, t) && t instanceof LivingEntity) {
					e.setCancelled(true);
					((LivingEntity) t).damage(dmg);
					Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, t.getLocation().add(0, 1, 0), 100);
					break;
				}
		}
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
	}

	@Override
	public void land(Arrow a) {
	}
}
