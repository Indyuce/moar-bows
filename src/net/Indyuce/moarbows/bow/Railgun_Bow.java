package net.Indyuce.moarbows.bow;

import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.particle.ParticleData;
import net.Indyuce.moarbows.api.util.LinearValue;

public class Railgun_Bow extends MoarBow {
	public Railgun_Bow() {
		super(new String[] { "Only works in minecarts. Arrows ", "explode upon landing, causing", "a powerful explosion." },
				new ParticleData(Particle.VILLAGER_ANGRY), new String[] { "TNT,RAIL,TNT", "RAIL,BOW,RAIL", "TNT,RAIL,TNT" });

		addModifier(new DoubleModifier("cooldown", new LinearValue(8, -1, 3, 8)), new DoubleModifier("radius", new LinearValue(5, 1)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		return data.getShooter().getVehicle() != null && data.getShooter().getVehicle().getType() == EntityType.MINECART;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
		whenLand(data);
	}

	@Override
	public void whenLand(ArrowData data) {
		data.getArrow().remove();
		data.getArrow().getWorld().createExplosion(data.getArrow().getLocation(), (float) data.getDouble("radius"));
	}
}
