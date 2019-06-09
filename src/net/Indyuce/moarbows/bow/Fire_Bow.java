package net.Indyuce.moarbows.bow;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

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
		int duration = (int) (getValue("duration") * 20);
		int maxTicks = (int) (getValue("max-burning-time") * 20);
		a.remove();
		a.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, a.getLocation(), 0);
		a.getWorld().spawnParticle(Particle.LAVA, a.getLocation(), 12, 0, 0, 0);
		a.getWorld().spawnParticle(Particle.FLAME, a.getLocation(), 48, 0, 0, 0, .13);
		a.getWorld().playSound(a.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3, 1);
		for (Entity ent : a.getNearbyEntities(5, 5, 5))
			if (ent instanceof LivingEntity)
				ent.setFireTicks(Math.min(ent.getFireTicks() + duration, maxTicks));
	}
}
