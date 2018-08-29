package net.Indyuce.mb.comp.worldguard;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import net.Indyuce.mb.MoarBows;

public class WorldGuardOn implements WGPlugin {
	public static WorldGuardPlugin wg;
	public static HashMap<String, StateFlag> flags = new HashMap<String, StateFlag>();

	public WorldGuardOn() {
		wg = (WorldGuardPlugin) MoarBows.plugin.getServer().getPluginManager().getPlugin("WorldGuard");

		FlagRegistry registry = wg.getFlagRegistry();
		for (CustomFlag cf : CustomFlag.values()) {
			StateFlag flag = new StateFlag(cf.getPath(), true);
			try {
				registry.register(flag);
				flags.put(cf.getPath(), flag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isPvpAllowed(Location loc) {
		ApplicableRegionSet set = wg.getRegionContainer().createQuery().getApplicableRegions(loc);
		return set.queryState(null, DefaultFlag.PVP) != StateFlag.State.DENY;
	}

	@Override
	public boolean isFlagAllowed(Player p, CustomFlag cf) {
		ApplicableRegionSet regions = wg.getRegionContainer().createQuery().getApplicableRegions(p.getLocation());
		StateFlag flag = flags.get(cf.getPath());
		return regions.queryValue(wg.wrapPlayer(p), flag) != StateFlag.State.DENY;
	}
}
