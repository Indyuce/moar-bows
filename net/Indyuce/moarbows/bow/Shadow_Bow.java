package net.Indyuce.moarbows.bow;

import org.bukkit.Color;
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
import net.Indyuce.moarbows.util.Utils;
import net.Indyuce.moarbows.util.VersionUtils;

public class Shadow_Bow extends MoarBow {
	public Shadow_Bow() {
		super(new String[] { "Shoots a long ranged linear", "cursed arrow that deals 8", "damage tothe first entity it hits." }, 0, 10.0, "redstone:128,0,128", new String[] { "EYE_OF_ENDER,EYE_OF_ENDER,EYE_OF_ENDER", "EYE_OF_ENDER,BOW,EYE_OF_ENDER", "EYE_OF_ENDER,EYE_OF_ENDER,EYE_OF_ENDER" });

		addModifier(new BowModifier("damage", 8));
	}

	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		e.setCancelled(true);
		final double dmg = MoarBows.getLanguage().getBows().getDouble("SHADOW_BOW.damage");
		if (!Utils.consumeAmmo(p, new ItemStack(Material.ARROW)))
			return false;

		new BukkitRunnable() {
			Location loc = p.getEyeLocation();
			double ti = 0;
			Vector v = p.getEyeLocation().getDirection().multiply(1.25);

			public void run() {
				for (double j = 0; j < 3; j++) {
					ti += .5;
					loc.add(v);
					Eff.SPELL_WITCH.display(.1f, .1f, .1f, 0, 8, loc, 100);
					VersionUtils.sound(loc, "ENTITY_ENDERMEN_HURT", 3, 2);
					for (LivingEntity t : loc.getWorld().getEntitiesByClass(LivingEntity.class))
						if (Utils.canDmgEntity(p, loc, t) && t != p) {
							new BukkitRunnable() {
								final Location loc2 = t.getLocation();
								double y = 0;

								public void run() {
									for (int i = 0; i < 2; i++) {
										y += .05;
										for (int j = 0; j < 2; j++) {
											double xz = y * Math.PI * .8 + (j * Math.PI);
											Eff.REDSTONE.display(new Eff.OrdinaryColor(Color.PURPLE), loc2.clone().add(Math.cos(xz) * 1.3, y, Math.sin(xz) * 1.3), 150);
										}
									}
									if (y >= 2.5) {
										cancel();
									}
								}
							}.runTaskTimer(MoarBows.plugin, 0, 1);
							VersionUtils.sound(t.getLocation(), "ENTITY_FIREWORK_BLAST", 3, 0);
							Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, t.getLocation().add(0, 1, 0), 100);
							cancel();
							MoarBows.getNMS().damageEntity(p, t, dmg);
							return;
						}
				}
				if (ti >= 20 * e.getForce())
					cancel();
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
		return false;
	}
}
