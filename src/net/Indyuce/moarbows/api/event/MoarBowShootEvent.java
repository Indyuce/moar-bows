package net.Indyuce.moarbows.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.PlayerData;

public class MoarBowShootEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private final PlayerData playerData;
	private final ArrowData arrow;

	private boolean cancelled = false;

	public MoarBowShootEvent(PlayerData playerData, ArrowData arrow) {
		super(playerData.getPlayer());

		this.playerData = playerData;
		this.arrow = arrow;
	}

	public PlayerData getPlayerData() {
		return playerData;
	}

	public ArrowData getArrowData() {
		return arrow;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
