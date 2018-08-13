package net.Indyuce.mb.comp;

import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.resource.CustomFlag;

public class WorldGuardUtils {
	public static boolean boolFlag(Player p, CustomFlag cf) {
		WorldGuardPlugin wgp = ((WorldGuardPlugin) Main.plugin.getServer().getPluginManager().getPlugin("WorldGuard"));
		ApplicableRegionSet regions = wgp.getRegionContainer().createQuery().getApplicableRegions(p.getLocation());
		StateFlag.State bool = null;
		StateFlag flag = null;
		for (String s : CUtils.flags.keySet())
			if (s.equals(cf.getPath()))
				flag = (StateFlag) CUtils.flags.get(s);
		if (regions.queryValue(wgp.wrapPlayer(p), flag) != null)
			bool = regions.queryValue(wgp.wrapPlayer(p), flag);
		// true by default
		if (bool == StateFlag.State.DENY)
			return false;
		return true;
	}

	public static void setup() {
		FlagRegistry registry = wg().getFlagRegistry();
		for (CustomFlag cf : CustomFlag.values()) {
			StateFlag flag = new StateFlag(cf.getPath(), true);
			try {
				registry.register(flag);
				CUtils.flags.put(flag.getName(), flag);
			} catch (FlagConflictException e) {
				e.printStackTrace();
			}
		}
	}

	public static WorldGuardPlugin wg() {
		return (WorldGuardPlugin) Main.plugin.getServer().getPluginManager().getPlugin("WorldGuard");
	}
}
