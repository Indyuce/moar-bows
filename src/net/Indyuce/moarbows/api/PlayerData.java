package net.Indyuce.moarbows.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.particle.ParticleData.ParticleRunnable;

public class PlayerData {
	private final UUID uuid;

	private Player player;

	/*
	 * used to check twice a second if the player changed the item he's been
	 * holding.
	 */
	private ItemStack mainhand, offhand;
	private ParticleRunnable mainparticles, offparticles;

	/*
	 * where cooldowns from bows are all stored.
	 */
	private final Map<String, Long> cooldowns = new HashMap<>();

	private static Map<UUID, PlayerData> playerDatas = new HashMap<>();

	/*
	 * private constructor since it is only used when setting up playerDatas,
	 * this way there is no possible confusion
	 */
	private PlayerData(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
	}

	public UUID getUniqueId() {
		return uuid;
	}

	public Player getPlayer() {
		return player;
	}

	public void updateItems() {
		if (mainhand == null || !mainhand.isSimilar(player.getInventory().getItemInMainHand())) {
			mainhand = player.getInventory().getItemInMainHand();
			if (mainparticles != null)
				mainparticles.cancel();
			MoarBow mainbow = MoarBows.plugin.getBowManager().get(mainhand);
			if (mainbow != null)
				(mainparticles = mainbow.getParticles().newRunnable(player, false)).runTaskTimer(MoarBows.plugin, 0, 4);
		}

		if (offhand == null || !offhand.isSimilar(player.getInventory().getItemInOffHand())) {
			offhand = player.getInventory().getItemInOffHand();
			if (offparticles != null)
				offparticles.cancel();
			MoarBow offbow = MoarBows.plugin.getBowManager().get(offhand);
			if (offbow != null)
				(offparticles = offbow.getParticles().newRunnable(player, true)).runTaskTimer(MoarBows.plugin, 0, 4);
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

	public boolean hasCooldown(MoarBow bow, int level) {
		return cooldowns.containsKey(bow.getId()) && cooldowns.get(bow.getId()) + bow.getDouble("cooldown", level) * 1000 > System.currentTimeMillis();
	}

	public double getRemainingCooldown(MoarBow bow, int level) {
		return cooldowns.containsKey(bow.getId()) ? (double) Math.max(0, cooldowns.get(bow.getId()) + bow.getDouble("cooldown", level) * 1000 - System.currentTimeMillis()) / 1000. : 0;
	}

	public void applyCooldown(MoarBow bow) {
		cooldowns.put(bow.getId(), System.currentTimeMillis());
	}

	public static PlayerData get(Player player) {
		return playerDatas.get(player.getUniqueId());
	}

	public static PlayerData setup(Player player) {

		/*
		 * creates player data if it does not exist
		 */
		if (!playerDatas.containsKey(player.getUniqueId())) {
			PlayerData data = new PlayerData(player);
			playerDatas.put(player.getUniqueId(), data);
			return data;
		}

		/*
		 * refreshes player if the data already exists
		 */
		PlayerData data = new PlayerData(player);
		data.player = player;
		return data;
	}
}
