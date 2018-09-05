package net.Indyuce.moarbows.bow;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.Indyuce.moarbows.Eff;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.comp.version.VersionSound;
import net.Indyuce.moarbows.util.Utils;

public class Composite_Bow extends MoarBow {
	public Composite_Bow() {
		super(new String[] { "Fires enchanted arrows that", "follow a linear trajectory." }, 0, 2.5, "redstone:91,60,17", new String[] { "AIR,AIR,AIR", "BOW,NETHER_STAR,BOW", "AIR,AIR,AIR" });

		addModifier(new BowModifier("damage", 8));
	}

	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		final double dmg = MoarBows.getLanguage().getBows().getDouble("COMPOSITE_BOW.damage");
		e.setCancelled(true);
		if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
			return false;

		p.getWorld().playSound(p.getLocation(), VersionSound.ENTITY_ARROW_SHOOT.getSound(), 2, 0);
		new BukkitRunnable() {
			Location loc = p.getEyeLocation();
			double ti = 0;
			double max = 20 * e.getForce();
			Vector v = p.getEyeLocation().getDirection().multiply(1.25);

			public void run() {
				for (double j = 0; j < 3; j++) {
					ti += .5;
					loc.add(v);
					Eff.CRIT.display(.1f, .1f, .1f, .1f, 8, loc, 100);
					loc.getWorld().playSound(loc, VersionSound.BLOCK_NOTE_HAT.getSound(), 2, 2);
					for (LivingEntity t : loc.getWorld().getEntitiesByClass(LivingEntity.class))
						if (Utils.canDmgEntity(p, loc, t) && t != p) {
							t.getWorld().playSound(t.getLocation(), VersionSound.ENTITY_FIREWORK_BLAST.getSound(), 2, 0);
							Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, t.getLocation().add(0, 1, 0), 100);
							cancel();
							MoarBows.getNMS().damageEntity(p, t, dmg);
							return;
						}
				}
				if (ti >= max)
					cancel();
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
		return false;
	}
}
