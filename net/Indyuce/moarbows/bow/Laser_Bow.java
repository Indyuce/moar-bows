package net.Indyuce.moarbows.bow;

import java.util.ArrayList;
import java.util.List;

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

public class Laser_Bow extends MoarBow {
	public Laser_Bow() {
		super(new String[] { "Fires instant laser arrows", "that deals 8 damage to", "every entity it hits." }, 0, 0, "redstone:255,0,0", new String[] { "REDSTONE_BLOCK,REDSTONE_BLOCK,REDSTONE_BLOCK", "REDSTONE_BLOCK,BOW,REDSTONE_BLOCK", "REDSTONE_BLOCK,REDSTONE_BLOCK,REDSTONE_BLOCK" });

		addModifier(new BowModifier("damage", 5));
	}

	@Override
	public boolean shoot(EntityShootBowEvent event, Arrow arrow, Player player, ItemStack item) {
		double damage = MoarBows.getLanguage().getBows().getDouble("LASER_BOW.damage") * getPowerDamageMultiplier(item);
		if (!BowUtils.consumeAmmo(player, new ItemStack(Material.ARROW)))
			return false;

		player.getWorld().playSound(player.getLocation(), VersionSound.ENTITY_ARROW_SHOOT.getSound(), 2, 0);
		int range = (int) (56 * event.getForce());
		Location loc = player.getEyeLocation();
		List<Integer> hit = new ArrayList<>();
		for (int j = 0; j < range; j++) {
			loc.add(player.getEyeLocation().getDirection());
			ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(Color.RED), loc, 100);
			if (loc.getBlock().getType().isSolid())
				break;

			for (Entity target : player.getNearbyEntities(100, 100, 100))
				if (!hit.contains(target.getEntityId()) && BowUtils.canDmgEntity(player, loc, target) && target instanceof LivingEntity) {
					hit.add(target.getEntityId());
					MoarBows.getNMS().damageEntity(player, (LivingEntity) target, damage);
				}
		}
		return false;
	}
}
