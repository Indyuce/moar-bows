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

public class FireBow implements SpecialBow {
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
		int duration = Main.bows.getInt("FIRE_BOW.duration") * 20;
		int maxTicks = Main.bows.getInt("FIRE_BOW.max-burning-time") * 20;
		a.remove();
		Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, a.getLocation(), 200);
		Eff.LAVA.display(0, 0, 0, 0, 12, a.getLocation(), 200);
		Eff.FLAME.display(0, 0, 0, .13f, 48, a.getLocation().add(0, .1, 0), 200);
		VersionUtils.sound(a.getLocation(), "ENTITY_FIREWORK_LARGE_BLAST", 3, 1);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				int ticks = ent.getFireTicks() + duration;
				ticks = (ticks > maxTicks ? maxTicks : ticks);
				ent.setFireTicks(ticks);
			}
	}
}
