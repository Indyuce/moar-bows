package net.Indyuce.moarbows.api;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.version.nms.NBTItem;

public class ArrowData {
	private final MoarBow bow;
	private final PlayerData playerData;
	private final Arrow arrow;

	/*
	 * the player instance is cached, that way the bow still works if the player
	 * disconnects.
	 */
	private final Player sender;

	private final NBTItem source;
	private final int level;

	public ArrowData(MoarBow bow, PlayerData playerData, Arrow arrow, ItemStack source) {
		this.bow = bow;
		this.playerData = playerData;
		this.sender = playerData.getPlayer();
		this.arrow = arrow;

		this.source = MoarBows.plugin.getNMS().getNBTItem(source);
		this.level = this.source.getInteger("MoarBowLevel");
	}

	public MoarBow getBow() {
		return bow;
	}

	public NBTItem getSource() {
		return source;
	}

	public PlayerData getPlayerData() {
		return playerData;
	}

	public Player getSender() {
		return sender;
	}

	public Arrow getArrow() {
		return arrow;
	}

	public int getLevel() {
		return level;
	}
	
	public double getDouble(String path) {
		return bow.getDouble(path, level);
	}
}
