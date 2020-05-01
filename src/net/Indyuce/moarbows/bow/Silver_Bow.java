package net.Indyuce.moarbows.bow;

import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.particle.ParticleData;
import net.Indyuce.moarbows.api.util.LinearFormula;

public class Silver_Bow extends MoarBow {
	public Silver_Bow() {
		super(new String[] { "Arrows deal &c{extra}% &7additional damage." }, new ParticleData(Particle.CRIT),
				new String[] { "IRON_INGOT,IRON_INGOT,IRON_INGOT", "IRON_INGOT,BOW,IRON_INGOT", "IRON_INGOT,IRON_INGOT,IRON_INGOT" });

		addModifier(new DoubleModifier("cooldown", new LinearFormula(0, 0)), new DoubleModifier("extra", new LinearFormula(40, 30)),
				new DoubleModifier("block-effect-id", new LinearFormula(12, 0)));
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		return true;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {
		if (!(target instanceof LivingEntity))
			return;

		int id = (int) data.getDouble("block-effect-id");
		data.getShooter().getWorld().playEffect(data.getShooter().getLocation(), Effect.STEP_SOUND, id);
		data.getShooter().getWorld().playEffect(data.getShooter().getLocation().add(0, 1, 0), Effect.STEP_SOUND, id);
		event.setDamage(event.getDamage() * (1. + data.getDouble("damage-percent") / 100.));
	}

	@Override
	public void whenLand(ArrowData data) {
	}
}
