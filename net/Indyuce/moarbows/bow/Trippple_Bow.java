package net.Indyuce.moarbows.bow;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.version.VersionSound;

public class Trippple_Bow extends MoarBow {
	public Trippple_Bow() {
		super(new String[] { "Shoots 3 arrows at a time." }, 0, 2.5, "redstone:255,255,255", new String[] { "AIR,AIR,AIR", "BOW,BOW,BOW", "AIR,AIR,AIR" });
	}

	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		e.setCancelled(true);
		p.getWorld().playSound(p.getLocation(), VersionSound.ENTITY_ARROW_SHOOT.getSound(), 2, 1);
		Location loc = p.getLocation().add(0, 1.2, 0);
		for (int j = -1; j < 2; j++) {
			if (!BowUtils.consumeAmmo(p, new ItemStack(Material.ARROW)))
				return false;

			loc.setYaw(p.getLocation().getYaw() + j);
			p.launchProjectile(Arrow.class).setVelocity(loc.getDirection().multiply(e.getForce() * 3.3));
		}
		return true;
	}
}
