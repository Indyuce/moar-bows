package net.Indyuce.moarbows.manager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;

public class ArrowManager {
	private Map<Integer, ArrowData> map = new HashMap<>();

	public void registerArrow(Arrow arrow, MoarBow bow, Player sender) {
		map.put(arrow.getEntityId(), new ArrowData(bow, sender, arrow));
	}

	public void unregisterArrow(Arrow arrow) {
		map.remove(arrow.getEntityId());
	}

	public boolean isCustomArrow(Arrow arrow) {
		return map.containsKey(arrow.getEntityId());
	}

	public ArrowData getArrowData(Arrow arrow) {
		return map.get(arrow.getEntityId());
	}

	@Deprecated
	public ArrowData safeGetArrowData(Arrow arrow) {
		return map.containsKey(arrow.getEntityId()) ? map.get(arrow.getEntityId()) : null;
	}
}
