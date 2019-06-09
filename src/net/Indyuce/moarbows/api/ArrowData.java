package net.Indyuce.moarbows.api;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

public class ArrowData {
	private MoarBow bow;
	private Player sender;
	private Arrow arrow;

	public ArrowData(MoarBow bow, Player sender, Arrow arrow) {
		this.bow = bow;
		this.sender = sender;
		this.arrow = arrow;
	}

	public MoarBow getBow() {
		return bow;
	}

	public Player getSender() {
		return sender;
	}

	public void setSender(Player sender) {
		this.sender = sender;
	}

	public Arrow getArrow() {
		return arrow;
	}
}
