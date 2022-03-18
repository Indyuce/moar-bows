package net.Indyuce.moarbows;

import net.Indyuce.moarbows.player.PlayerData;
import net.Indyuce.moarbows.bow.MoarBow;
import net.Indyuce.moarbows.command.MoarBowsCommand;
import net.Indyuce.moarbows.command.completion.MoarBowsCompletion;
import net.Indyuce.moarbows.comp.Metrics;
import net.Indyuce.moarbows.comp.worldguard.WGPlugin;
import net.Indyuce.moarbows.comp.worldguard.WorldGuardOff;
import net.Indyuce.moarbows.comp.worldguard.WorldGuardOn;
import net.Indyuce.moarbows.gui.listener.GuiListener;
import net.Indyuce.moarbows.listener.*;
import net.Indyuce.moarbows.manager.ArrowManager;
import net.Indyuce.moarbows.manager.BowManager;
import net.Indyuce.moarbows.manager.ConfigManager;
import net.Indyuce.moarbows.version.SpigotPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class MoarBows extends JavaPlugin {
    public static MoarBows plugin;

    private final BowManager bowManager = new BowManager();
    private final ArrowManager arrowManager = new ArrowManager();

    private WGPlugin wgPlugin;
    private ConfigManager language;

    public void onLoad() {
        plugin = this;
        wgPlugin = getServer().getPluginManager().getPlugin("WorldGuard") != null ? new WorldGuardOn() : new WorldGuardOff();
    }

    public void onEnable() {
        bowManager.stopRegistration();

        new SpigotPlugin(36387, this).checkForUpdate();

        new Metrics(this);

        saveDefaultConfig();
        language = new ConfigManager();

        Bukkit.getServer().getPluginManager().registerEvents(new BowUtils(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new GuiListener(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new ShootBow(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ItemPrevents(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new HitEntity(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArrowLand(), this);
        if (getConfig().getBoolean("hand-particles"))
            Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(player -> PlayerData.get(player).updateItems()), 100,
                    10);

        // Automatically registers external listeners
        bowManager.getBows().stream().filter(bow -> bow instanceof Listener)
                .forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents((Listener) listener, this));

        /*
         * Makes sure the arrow map does not retain arrow instances for longer than
         * 10 minutes to make sure there are no memory issues, sometimes a plugin
         * bug happens and makes unregistering the instance impossible
         */
        Bukkit.getScheduler().scheduleSyncRepeatingTask(MoarBows.plugin, () -> arrowManager.flushArrowData(), 5 * 60 * 20, 5 * 60 * 20);

        /*
         * Setup player datas for players that have been online e.g during a
         * reload, so the plugin doesn't crash since normally the player datas
         * are initialized whenever players log on the server
         */
        Bukkit.getOnlinePlayers().forEach(player -> PlayerData.setup(player));
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        // Commands
        getCommand("moarbows").setExecutor(new MoarBowsCommand());
        getCommand("moarbows").setTabCompleter(new MoarBowsCompletion());

        // Crafting recipes
        if (getConfig().getBoolean("bow-crafting-recipes"))
            for (MoarBow bow : bowManager.getBows())
                if (bow.isCraftEnabled())
                    try {
                        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(MoarBows.plugin, "MoarBows_" + bow.getId()), bow.getItem(1));
                        recipe.shape(new String[]{"ABC", "DEF", "GHI"});
                        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
                        List<String> list = Arrays.asList(bow.getFormattedCraftingRecipe());
                        Validate.isTrue(list.size() == 3, "Crafting recipe must have 3 lines");

                        for (int i = 0; i < 3; i++) {
                            List<String> line = Arrays.asList(list.get(i).split("\\,"));
                            Validate.isTrue(line.size() == 3, "Line n" + (i + 1) + " does not have 3 elements");
                            for (int j = 0; j < 3; j++)
                                recipe.setIngredient(chars[i * 3 + j], Material.valueOf(line.get(j).replace("-", "_").toUpperCase()));
                        }

                        Bukkit.addRecipe(recipe);
                    } catch (IllegalArgumentException exception) {
                        getLogger().log(Level.WARNING, "Could not register recipe of '" + bow.getId() + "': " + exception.getMessage());
                    }
    }

    public BowManager getBowManager() {
        return bowManager;
    }

    public ArrowManager getArrowManager() {
        return arrowManager;
    }

    public ConfigManager getLanguage() {
        return language;
    }

    public WGPlugin getWorldGuard() {
        return wgPlugin;
    }

    public void reloadPlugin() {
        reloadConfig();
        language = new ConfigManager();
    }

    public File getJarFile() {
        return plugin.getFile();
    }
}