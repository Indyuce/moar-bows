package net.Indyuce.moarbows.api;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.particle.ParticleData.ParticleRunnable;
import org.antlr.v4.runtime.misc.NotNull;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;

    private Player player;

    /*
     * used to check twice a second if the player changed the item he's been
     * holding.
     */
    private ItemStack mainhand, offhand;
    private ParticleRunnable mainparticles, offparticles;

    /**
     * Where cooldowns from bows are all stored.
     */
    private final Map<String, Long> cooldowns = new HashMap<>();

    private static Map<UUID, PlayerData> playerDatas = new HashMap<>();

    /**
     * Private constructor since it is only used when setting
     * up playerDatas, this way there is no possible confusion
     */
    private PlayerData(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public UUID getUniqueId() {
        return uuid;
    }

    @NotNull
    public Player getPlayer() {
        return Objects.requireNonNull(player, "Player is offline");
    }

    public void updateItems() {
        if (mainhand == null || !mainhand.isSimilar(player.getInventory().getItemInMainHand())) {
            mainhand = player.getInventory().getItemInMainHand();
            if (mainparticles != null)
                mainparticles.cancel();
            MoarBow mainbow = MoarBows.plugin.getBowManager().get(mainhand);
            if (mainbow != null && mainbow.hasParticles())
                (mainparticles = mainbow.getParticles().newRunnable(player, false)).runTaskTimer(MoarBows.plugin, 0, 4);
        }

        if (offhand == null || !offhand.isSimilar(player.getInventory().getItemInOffHand())) {
            offhand = player.getInventory().getItemInOffHand();
            if (offparticles != null)
                offparticles.cancel();
            MoarBow offbow = MoarBows.plugin.getBowManager().get(offhand);
            if (offbow != null && offbow.hasParticles())
                (offparticles = offbow.getParticles().newRunnable(player, true)).runTaskTimer(MoarBows.plugin, 0, 4);
        }
    }

    public void logOff() {
        mainhand = null;
        offhand = null;
        player = null;

        if (mainparticles != null)
            mainparticles.cancel();
        if (offparticles != null)
            offparticles.cancel();
    }

    public boolean hasCooldown(MoarBow bow, int level) {
        return cooldowns.containsKey(bow.getId())
                && cooldowns.get(bow.getId()) + bow.getDouble("cooldown", level) * 1000 > System.currentTimeMillis();
    }

    public double getRemainingCooldown(MoarBow bow, int level) {
        return cooldowns.containsKey(bow.getId())
                ? (double) Math.max(0, cooldowns.get(bow.getId()) + bow.getDouble("cooldown", level) * 1000 - System.currentTimeMillis()) / 1000.
                : 0;
    }

    public void applyCooldown(MoarBow bow) {
        cooldowns.put(bow.getId(), System.currentTimeMillis());
    }

    @NotNull
    public static PlayerData get(OfflinePlayer player) {
        return Objects.requireNonNull(playerDatas.get(player.getUniqueId()), "Player data not loaded");
    }

    public static PlayerData setup(Player player) {
        PlayerData found = playerDatas.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerData(player));
        found.player = player;
        return found;
    }
}
