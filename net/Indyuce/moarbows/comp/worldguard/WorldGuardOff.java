package net.Indyuce.moarbows.comp.worldguard;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardOff implements WGPlugin {
	@Override
	public boolean isPvpAllowed(Location loc) {
		return true;
	}

	@Override
	public boolean isFlagAllowed(Player p, CustomFlag cf) {
		return true;
	}
}
