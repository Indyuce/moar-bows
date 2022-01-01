package net.Indyuce.moarbows.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;

public class ArrowManager {
	private final Map<Integer, ArrowData> map = new HashMap<>();

	public ArrowManager() {

		/*
		 * makes sure the map does not retain arrow instances for longer than 10
		 * minutes to make sure there are no memory issues, sometimes a plugin
		 * bug happens and makes unregistering the instance impossible
		 */
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MoarBows.plugin, () -> BowUtils.clean(map.values(), arrow -> arrow.hasTimedOut()),
				5 * 60 * 20, 5 * 60 * 20);
	}

	public void registerArrow(ArrowData data) {
		map.put(data.getArrow().getEntityId(), data);
	}

	public void unregisterArrow(Arrow arrow) {
		map.remove(arrow.getEntityId());
	}

	public Collection<ArrowData> getActive() {
		return map.values();
	}

	@Deprecated
	public boolean isCustomArrow(Arrow arrow) {
		return map.containsKey(arrow.getEntityId());
	}

	public Optional<ArrowData> getArrowData(Arrow arrow) {
		return map.containsKey(arrow.getEntityId()) ? Optional.of(map.get(arrow.getEntityId())) : Optional.empty();
	}

	@Deprecated
	public ArrowData safeGetArrowData(Arrow arrow) {
		return map.containsKey(arrow.getEntityId()) ? map.get(arrow.getEntityId()) : null;
	}
}
