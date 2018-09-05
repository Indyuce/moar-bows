package net.Indyuce.moarbows.bow;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.util.Utils;
import net.Indyuce.moarbows.util.VersionUtils;

public class Wither_Bow extends MoarBow {
	public Wither_Bow() {
		super(new String[] { "Shoots an exploding wither skull." }, 0, 4.0, "redstone:0,0,0", new String[] { "SKULL_ITEM:1,SKULL_ITEM:1,SKULL_ITEM:1", "SKULL_ITEM:1,BOW,SKULL_ITEM:1", "SKULL_ITEM:1,SKULL_ITEM:1,SKULL_ITEM:1" });
	}

	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		e.setCancelled(true);
		if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
			return false;

		VersionUtils.sound(p.getLocation(), "ENTITY_WITHER_SHOOT", 1, 1);
		WitherSkull ws = p.launchProjectile(WitherSkull.class);
		ws.setVelocity(p.getEyeLocation().getDirection().multiply(3.3 * e.getForce()));
		return false;
	}
}
