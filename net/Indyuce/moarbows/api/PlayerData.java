package net.Indyuce.moarbows.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.MoarBows;

public class PlayerData {
	private Player player;
	private UUID uuid;

	/*
	 * used to check twice a second if the player changed the item he's been
	 * holding.
	 */
	private ItemStack mainhand, offhand;
	private ParticleData mainparticles, offparticles;

	/*
	 * where cooldowns from bows are all stored.
	 */
	private Map<MoarBow, Long> bowCooldown = new HashMap<>();

	private static boolean offhandEnabled = MoarBows.getVersion().isStrictlyHigher(1, 8);
	private static Map<UUID, PlayerData> playerDatas = new HashMap<>();

	/*
	 * private constructor since it is only used when setting up playerDatas,
	 * this way there is no possible confusion
	 */
	private PlayerData(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
	}

	public static PlayerData get(Player player) {
		return playerDatas.get(player.getUniqueId());
	}

	public static void setup(Player player) {
		if (!playerDatas.containsKey(player.getUniqueId()))
			playerDatas.put(player.getUniqueId(), new PlayerData(player));
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public Player getPlayer() {
		return player;
	}

	@SuppressWarnings("deprecation")
	public void updateItems() {
		if (mainhand == null || !mainhand.isSimilar(player.getInventory().getItemInHand())) {
			mainhand = player.getInventory().getItemInHand();
			if (mainparticles != null)
				mainparticles.cancel();
			MoarBow mainbow = MoarBows.getBowManager().get(mainhand);
			if (mainbow != null)
				(mainparticles = mainbow.createParticleData().setPlayer(player)).runTaskTimer(MoarBows.plugin, 0, 4);
		}

		if (offhandEnabled && (offhand == null || !offhand.isSimilar(player.getInventory().getItemInOffHand()))) {
			offhand = player.getInventory().getItemInOffHand();
			if (offparticles != null)
				offparticles.cancel();
			MoarBow offbow = MoarBows.getBowManager().get(offhand);
			if (offbow != null)
				(offparticles = offbow.createParticleData().setPlayer(player).setOffhand(true)).runTaskTimer(MoarBows.plugin, 0, 4);
		}
	}

	public void cancelRunnables() {
		mainhand = null;
		offhand = null;

		if (mainparticles != null)
			mainparticles.cancel();
		if (offparticles != null)
			offparticles.cancel();
	}

	public boolean hasCooldown(MoarBow bow) {
		return bowCooldown.containsKey(bow) && bowCooldown.get(bow) + bow.getCooldown() * 1000 > System.currentTimeMillis();
	}

	public double getRemainingCooldown(MoarBow bow) {
		return !hasCooldown(bow) ? 0 : (double) (bowCooldown.get(bow) + bow.getCooldown() * 1000 - System.currentTimeMillis()) / 1000;
	}

	public void applyCooldown(MoarBow bow) {
		bowCooldown.put(bow, System.currentTimeMillis());
	}
}
