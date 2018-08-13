package net.Indyuce.mb.resource.bow;

import org.bukkit.Effect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.SpecialBow;

public class SilverBow implements SpecialBow {
	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		if (!(t instanceof LivingEntity))
			return;
			
		int id = Main.bows.getInt("SILVER_BOW.block-effect-id");
		p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, id);
		p.getWorld().playEffect(p.getLocation().add(0, 1, 0), Effect.STEP_SOUND, id);
		e.setDamage(e.getDamage() * (1. + Main.bows.getDouble("SILVER_BOW.damagePercent") / 100.));
	}

	@Override
	public void land(Arrow a) {
	}
}
