package net.Indyuce.moarbows.bow;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.Eff;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.comp.version.VersionSound;

public class Meteor_Bow extends MoarBow {
	public Meteor_Bow() {
		super(new String[] { "Shoots arrows that summon a fire", "comet upon landing, dealing damage", "and knockback to nearby entities." }, 0, 10.0, "lava", new String[] { "FIREBALL,FIREBALL,FIREBALL", "FIREBALL,BOW,FIREBALL", "FIREBALL,FIREBALL,FIREBALL" });

		addModifier(new BowModifier("damage", 8), new BowModifier("knockback", 1));
	}

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		land(t, a);
	}

	@Override
	public void land(Player p, Arrow a) {
		a.remove();
		double dmg = MoarBows.getLanguage().getBows().getInt("METEOR_BOW.damage");
		double knockback = MoarBows.getLanguage().getBows().getInt("METEOR_BOW.knockback");
		a.getWorld().playSound(a.getLocation(), VersionSound.ENTITY_ENDERMEN_TELEPORT.getSound(), 3, 1);
		new BukkitRunnable() {
			Location loc = a.getLocation().clone();
			Location source = a.getLocation().clone().add(0, 20, 0);
			Vector v = loc.toVector().subtract(source.toVector()).multiply(.06);
			double ti = 0;

			public void run() {
				ti += .06;
				source.add(v);
				Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, source, 150);
				Eff.FLAME.display(.2f, .2f, .2f, 0, 4, source, 100);
				if (ti >= 1) {
					loc.getWorld().playSound(loc, VersionSound.ENTITY_GENERIC_EXPLODE.getSound(), 3, 1);
					Eff.EXPLOSION_LARGE.display(2, 2, 2, 0, 16, loc, 1000);
					Eff.FLAME.display(0, 0, 0, .25f, 32, loc.add(0, .1, 0), 100);
					Eff.EXPLOSION_NORMAL.display(0, 0, 0, .25f, 32, loc, 100);
					for (LivingEntity t : a.getWorld().getEntitiesByClass(LivingEntity.class))
						if (t.getLocation().add(0, 1, 0).distanceSquared(loc) < 25) {
							MoarBows.getNMS().damageEntity(p, t, dmg);
							t.setVelocity(t.getLocation().toVector().subtract(loc.toVector()).setY(.75).normalize().multiply(knockback));
						}
					cancel();
				}
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
	}
}
