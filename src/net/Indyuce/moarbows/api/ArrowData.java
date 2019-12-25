package net.Indyuce.moarbows.api;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
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
	private final LivingEntity shooter;

	private final NBTItem source;
	private final int level;

	/*
	 * when a non-player entity uses the bow
	 */
	public ArrowData(MoarBow bow, LivingEntity shooter, Arrow arrow, ItemStack source) {
		this.bow = bow;
		this.playerData = null;
		this.shooter = shooter;
		this.arrow = arrow;

		this.source = MoarBows.plugin.getNMS().getNBTItem(source);
		this.level = this.source.getInteger("MoarBowLevel");
	}

	/*
	 * when a player uses the bow
	 */
	public ArrowData(MoarBow bow, PlayerData playerData, Arrow arrow, ItemStack source) {
		this.bow = bow;
		this.playerData = playerData;
		this.shooter = playerData.getPlayer();
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

	public LivingEntity getShooter() {
		return shooter;
	}

	public boolean hasPlayer() {
		return playerData!=null;
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
