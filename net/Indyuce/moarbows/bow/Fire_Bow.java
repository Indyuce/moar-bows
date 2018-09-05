package net.Indyuce.moarbows.bow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.moarbows.Eff;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.comp.version.VersionSound;

public class Fire_Bow extends MoarBow {

	public Fire_Bow() {
		super(new String[] { "Shoots burning arrows that cause a", "first burst upon landing, igniting", "any entity within a few blocks." }, 0, 0, "flame", new String[] { "BLAZE_ROD,BLAZE_ROD,BLAZE_ROD", "BLAZE_ROD,BOW,BLAZE_ROD", "BLAZE_ROD,BLAZE_ROD,BLAZE_ROD" });

		addModifier(new BowModifier("duration", 4), new BowModifier("max-burning-time", 8));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(t, a);
	}

	@Override
	public void land(Player p, Arrow a) {
		int duration = MoarBows.getLanguage().getBows().getInt("FIRE_BOW.duration") * 20;
		int maxTicks = MoarBows.getLanguage().getBows().getInt("FIRE_BOW.max-burning-time") * 20;
		a.remove();
		Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, a.getLocation(), 200);
		Eff.LAVA.display(0, 0, 0, 0, 12, a.getLocation(), 200);
		Eff.FLAME.display(0, 0, 0, .13f, 48, a.getLocation().add(0, .1, 0), 200);
		a.getWorld().playSound(p.getLocation(), VersionSound.ENTITY_FIREWORK_LARGE_BLAST.getSound(), 2, 1);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity) {
				int ticks = ent.getFireTicks() + duration;
				ticks = ticks > maxTicks ? maxTicks : ticks;
				ent.setFireTicks(ticks);
			}
	}
}
