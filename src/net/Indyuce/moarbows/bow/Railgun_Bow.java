package net.Indyuce.moarbows.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Railgun_Bow extends MoarBow {
	public Railgun_Bow() {
		super(new String[] { "Only works in minecarts. Arrows ", "explode upon landing, causing", "a powerful explosion." }, 0, 7.5, "villager_angry", new String[] { "TNT,RAIL,TNT", "RAIL,BOW,RAIL", "TNT,RAIL,TNT" });

		addModifier(new BowModifier("radius", 5));
	}

	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return p.getVehicle() != null ? p.getVehicle().getType() == EntityType.MINECART : false;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(t, a);
	}

	@Override
	public void land(Player p, Arrow a) {
		a.remove();
		a.getWorld().createExplosion(a.getLocation(), (float) getValue("radius"));
	}
}
