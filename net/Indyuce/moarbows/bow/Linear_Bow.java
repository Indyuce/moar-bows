package net.Indyuce.moarbows.bow;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.ParticleEffect;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.version.VersionSound;

public class Linear_Bow extends MoarBow {
	public Linear_Bow() {
		super(new String[] { "Fires instant linear arrows", "that deals 8 damage to", "the first entity it hits." }, 0, 0, "redstone:90,90,255", new String[] { "FLINT,STICK,FLINT", "STICK,BOW,STICK", "FLINT,STICK,FLINT" });

		addModifier(new BowModifier("damage", 6));
	}

	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		double dmg = MoarBows.getLanguage().getBows().getDouble("LINEAR_BOW.damage");
		if (!BowUtils.consumeAmmo(p, new ItemStack(Material.ARROW)))
			return false;

		p.getWorld().playSound(p.getLocation(), VersionSound.ENTITY_ARROW_SHOOT.getSound(), 2, 0);
		int range = (int) (56 * e.getForce());
		Location loc = p.getEyeLocation();
		for (double j = 0; j < range; j++) {
			loc.add(p.getEyeLocation().getDirection());
			ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(Color.SILVER), loc, 200);
			if (loc.getBlock().getType().isSolid())
				break;
			for (Entity t : p.getNearbyEntities(30, 30, 30))
				if (BowUtils.canDmgEntity(p, loc, t) && t instanceof LivingEntity) {
					e.setCancelled(true);
					MoarBows.getNMS().damageEntity(p, (LivingEntity) t, dmg);
					ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, t.getLocation().add(0, 1, 0), 100);
					break;
				}
		}
		return false;
	}
}
