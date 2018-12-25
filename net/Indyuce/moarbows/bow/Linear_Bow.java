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
	public boolean shoot(EntityShootBowEvent event, Arrow arrow, Player player, ItemStack item) {
		double dmg = MoarBows.getLanguage().getBows().getDouble("LINEAR_BOW.damage") * getPowerDamageMultiplier(item);
		if (!BowUtils.consumeAmmo(player, new ItemStack(Material.ARROW)))
			return false;

		player.getWorld().playSound(player.getLocation(), VersionSound.ENTITY_ARROW_SHOOT.getSound(), 2, 0);
		int range = (int) (56 * event.getForce());
		Location loc = player.getEyeLocation();
		for (double j = 0; j < range; j++) {
			loc.add(player.getEyeLocation().getDirection());
			ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(Color.SILVER), loc, 200);
			if (loc.getBlock().getType().isSolid())
				break;
			
			for (Entity t : player.getNearbyEntities(30, 30, 30))
				if (BowUtils.canDmgEntity(player, loc, t) && t instanceof LivingEntity) {
					event.setCancelled(true);
					MoarBows.getNMS().damageEntity(player, (LivingEntity) t, dmg);
					ParticleEffect.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, t.getLocation().add(0, 1, 0), 100);
					break;
				}
		}
		return false;
	}
}
