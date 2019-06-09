package net.Indyuce.moarbows.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import net.Indyuce.moarbows.api.MoarBow;

public class MoarBowShootEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	private MoarBow bow;
	private boolean cancelled = false;

	public MoarBowShootEvent(Player who, MoarBow bow) {
		super(who);
		this.bow = bow;
	}

	public MoarBow getBow() {
		return bow;
	}

	public void setBow(MoarBow bow) {
		this.bow = bow;
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
