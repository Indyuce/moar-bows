package net.Indyuce.moarbows.comp.worldguard;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import net.Indyuce.moarbows.MoarBows;

public class WorldGuardOn implements WGPlugin {
	private final WorldGuard worldguard;
	private final WorldGuardPlugin worldguardPlugin;
	private final Map<String, StateFlag> flags = new HashMap<>();

	public WorldGuardOn() {
		worldguard = WorldGuard.getInstance();
		worldguardPlugin = (WorldGuardPlugin) MoarBows.plugin.getServer().getPluginManager().getPlugin("WorldGuard");

		FlagRegistry registry = worldguard.getFlagRegistry();
		for (CustomFlag cf : CustomFlag.values()) {
			StateFlag flag = new StateFlag(cf.getPath(), true);
			try {
				registry.register(flag);
				flags.put(cf.getPath(), flag);
			} catch (/* FlagConflict */Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	@Override
	public boolean isPvpAllowed(Location loc) {
		ApplicableRegionSet set = getApplicableRegion(loc);
		return set.queryState(null, Flags.PVP) != StateFlag.State.DENY;
	}

	@Override
	public boolean isFlagAllowed(Player player, CustomFlag customFlag) {
		ApplicableRegionSet regions = getApplicableRegion(player.getLocation());
		StateFlag flag = flags.get(customFlag.getPath());
		return regions.queryValue(worldguardPlugin.wrapPlayer(player), flag) != StateFlag.State.DENY;
	}

	private ApplicableRegionSet getApplicableRegion(Location loc) {
		com.sk89q.worldguard.protection.regions.RegionContainer container = worldguard.getPlatform().getRegionContainer();
		return container.createQuery().getApplicableRegions(BukkitAdapter.adapt(loc));
	}

	@Override
	public boolean isFlagAllowed(Location loc, CustomFlag customFlag) {
		ApplicableRegionSet regions = getApplicableRegion(loc);
		StateFlag flag = flags.get(customFlag.getPath());
		return regions.queryValue(null, flag) != StateFlag.State.DENY;
	}
}
