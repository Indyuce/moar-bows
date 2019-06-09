package net.Indyuce.moarbows.bow;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;

public class Linear_Bow extends MoarBow {
	public Linear_Bow() {
		super(new String[] { "Fires instant linear arrows", "that deals 8 damage to", "the first entity it hits." }, 0, 0, "redstone:90,90,255", new String[] { "FLINT,STICK,FLINT", "STICK,BOW,STICK", "FLINT,STICK,FLINT" });

		addModifier(new BowModifier("damage", 6));
	}

	@Override
	public boolean shoot(EntityShootBowEvent event, Arrow arrow, Player player, ItemStack item) {
		double dmg = getValue("damage") * getPowerDamageMultiplier(item);
		if (!BowUtils.consumeAmmo(player, new ItemStack(Material.ARROW)))
			return false;

		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 2, 0);
		int range = (int) (56 * event.getForce());
		Location loc = player.getEyeLocation();
		for (double j = 0; j < range; j++) {
			loc.add(player.getEyeLocation().getDirection());
			loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 0, new Particle.DustOptions(Color.GRAY, 2));
			if (loc.getBlock().getType().isSolid())
				break;

			for (Entity entity : player.getNearbyEntities(30, 30, 30))
				if (BowUtils.canDmgEntity(player, loc, entity) && entity instanceof LivingEntity) {
					event.setCancelled(true);
					((LivingEntity) entity).damage(dmg, player);
					loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 0);
					break;
				}
		}
		return false;
	}
}
