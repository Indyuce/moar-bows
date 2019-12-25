package net.Indyuce.moarbows.comp.worldguard;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface WGPlugin {
	boolean isPvpAllowed(Location loc);

	boolean isFlagAllowed(Player player, CustomFlag customFlag);

	boolean isFlagAllowed(Location loc, CustomFlag customFlag);
}
