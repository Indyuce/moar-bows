package net.Indyuce.moarbows.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.moarbows.api.MoarBow;

public class Lightning_Bowlt extends MoarBow {
	public Lightning_Bowlt() {
		super("LIGHTNING_BOWLT", "Lightning Bow'lt", new String[] { "Shoots arrows that summon", "lightning upon landing." }, 0, 0, "fireworks_spark", new String[] { "AIR,BEACON,AIR", "AIR,BOW,AIR", "AIR,AIR,AIR" });
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(t, a);
	}

	@Override
	public void land(Player p, Arrow a) {
		a.remove();
		a.getWorld().strikeLightning(a.getLocation());
	}
}
