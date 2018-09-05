package net.Indyuce.moarbows.bow;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.Eff;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.util.Utils;
import net.Indyuce.moarbows.util.VersionUtils;

public class Linear_Bow extends MoarBow {
	public Linear_Bow() {
		super(new String[] { "Fires instant linear arrows", "that deals 8 damage to", "the first entity it hits." }, 0, 0, "redstone:90,90,255", new String[] { "FLINT,STICK,FLINT", "STICK,BOW,STICK", "FLINT,STICK,FLINT" });
	}

	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		double dmg = MoarBows.bows.getDouble("LINEAR_BOW.damage");
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
					MoarBows.getNMS().damageEntity(p, (LivingEntity) t, dmg);
					Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, t.getLocation().add(0, 1, 0), 100);
					break;
				}
		}
		return false;
	}
}
