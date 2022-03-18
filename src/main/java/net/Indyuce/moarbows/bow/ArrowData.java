package net.Indyuce.moarbows.bow;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.player.PlayerData;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class ArrowData {
	private final MoarBow bow;
	private final PlayerData playerData;
	private final Arrow arrow;

	/**
	 * The player instance is cached, that way the bow still
	 * works if the player disconnects.
	 */
	private final LivingEntity shooter;

	private final ItemStack source;
	private final int level;
	private final long date = System.currentTimeMillis();

	/**
	 * When a non-player entity uses the bow
	 */
	public ArrowData(MoarBow bow, LivingEntity shooter, Arrow arrow, ItemStack source) {
		this.bow = bow;
		this.playerData = null;
		this.shooter = shooter;
		this.arrow = arrow;

		this.source = source;
		this.level = BowUtils.getBowLevel(source);
	}

	/**
	 * When a player uses the bow
	 */
	public ArrowData(MoarBow bow, PlayerData playerData, Arrow arrow, ItemStack source) {
		this.bow = bow;
		this.playerData = playerData;
		this.shooter = playerData.getPlayer();
		this.arrow = arrow;

		this.source = source;
		this.level = BowUtils.getBowLevel(source);
	}

	public MoarBow getBow() {
		return bow;
	}

	public ItemStack getSource() {
		return source;
	}

	public PlayerData getPlayerData() {
		return playerData;
	}

	public LivingEntity getShooter() {
		return shooter;
	}

	/**
	 * Lets you know if a player or a monster shot the arrow that way it can
	 * apply cooldowns to players and not do anything for monsters
	 */
	public boolean hasPlayer() {
		return playerData != null;
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

	/**
	 * The arrow instance must be flushed from the database if it exists for
	 * longer than 10 minutes which is reasonable since landing an arrow should
	 * only take about a few seconds max
	 */
	public boolean hasTimedOut() {
		return date + 10 * 60 * 1000 < System.currentTimeMillis();
	}
}
