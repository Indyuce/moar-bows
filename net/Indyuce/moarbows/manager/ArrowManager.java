package net.Indyuce.moarbows.manager;

import java.util.HashMap;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;

public class ArrowManager {
	private static HashMap<Integer, ArrowData> map = new HashMap<Integer, ArrowData>();

	public static void registerArrow(Arrow arrow, MoarBow bow, Player sender) {
		map.put(arrow.getEntityId(), new ArrowData(bow, sender, arrow));
	}

	public static void unregisterArrow(Arrow arrow) {
		map.remove(arrow.getEntityId());
	}

	public static boolean isCustomArrow(Arrow arrow) {
		return map.containsKey(arrow.getEntityId());
	}

	public static ArrowData getArrowData(Arrow arrow) {
		return map.get(arrow.getEntityId());
	}

	@Deprecated
	public static ArrowData safeGetArrowData(Arrow arrow) {
		return map.containsKey(arrow.getEntityId()) ? map.get(arrow.getEntityId()) : null;
	}
}
