package net.Indyuce.mb.resource.bow;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;
import net.Indyuce.mb.util.Utils;

public class LinearBow implements SpecialBow {
	@Override
	
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		double dmg = Main.bows.getDouble("LINEAR_BOW.damage");
		e.setCancelled(true);
		if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
			return false;

		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 2, 0);
		int range = (int) (56 * e.getForce());
		Location loc = p.getEyeLocation();
		for (double j = 0; j < range; j++) {
			loc.add(p.getEyeLocation().getDirection());
			loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, new Particle.DustOptions(Color.GRAY, 2));
			if (loc.getBlock().getType().isSolid())
				break;
			
			for (Entity t : p.getNearbyEntities(30, 30, 30))
				if (Utils.canDmgEntity(p, loc, t) && t instanceof LivingEntity) {
					e.setCancelled(true);
					((LivingEntity) t).damage(dmg);
					loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 0);
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
