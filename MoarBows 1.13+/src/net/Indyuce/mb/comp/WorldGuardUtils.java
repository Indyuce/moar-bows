package net.Indyuce.mb.comp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import net.Indyuce.mb.resource.CustomFlag;

public class WorldGuardUtils {
	public static WorldGuardPlugin wg;

	public static boolean boolFlag(Player p, CustomFlag cf) {
//		WorldGuardPlugin wgp = ((WorldGuardPlugin) Main.plugin.getServer().getPluginManager().getPlugin("WorldGuard"));
//		ApplicableRegionSet regions = wgp.getRegionContainer().createQuery().getApplicableRegions(p.getLocation());
//		StateFlag.State bool = null;
//		StateFlag flag = null;
//		for (String s : CUtils.flags.keySet())
//			if (s.equals(cf.getPath()))
//				flag = (StateFlag) CUtils.flags.get(s);
//		if (regions.queryValue(wgp.wrapPlayer(p), flag) != null)
//			bool = regions.queryValue(wgp.wrapPlayer(p), flag);
//		// true by default
//		if (bool == StateFlag.State.DENY)
//			return false;
		return true;
	}

	public static void setup() {
		wg = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");

//		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
//		for (CustomFlag cf : CustomFlag.values()) {
//			StateFlag flag = new StateFlag(cf.getPath(), true);
//			try {
//				registry.register(flag);
//				CUtils.flags.put(flag.getName(), flag);
//			} catch (FlagConflictException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
