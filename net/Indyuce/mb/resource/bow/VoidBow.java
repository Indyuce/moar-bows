package net.Indyuce.mb.resource.bow;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.mb.Eff;
import net.Indyuce.mb.resource.SpecialBow;
import net.Indyuce.mb.resource.TaskState;
import net.Indyuce.mb.util.VersionUtils;

public class VoidBow implements SpecialBow {
	@Override
	public TaskState shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t, Object... obj) {
		explode(a);
		return TaskState.CONTINUE;
	}

	@Override
	public TaskState land(Arrow a) {
		explode(a);
		return TaskState.CONTINUE;
	}
	
	private void explode(Arrow a) {
		a.remove();
		Location loc = a.getLocation();
		loc.setPitch(((Player) a.getShooter()).getLocation().getPitch());
		loc.setYaw(((Player) a.getShooter()).getLocation().getYaw());
		((Player) a.getShooter()).teleport(loc);
		Eff.EXPLOSION_LARGE.display(0, 0, 0, 0, 1, loc, 200);
		VersionUtils.sound(a.getLocation(), "ENTITY_ENDERMEN_TELEPORT", 3, 1);
	}
}
