package net.Indyuce.moarbows;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.PlayerData;
import net.Indyuce.moarbows.command.MoarBowsCommand;
import net.Indyuce.moarbows.command.completion.MoarBowsCompletion;
import net.Indyuce.moarbows.comp.Metrics;
import net.Indyuce.moarbows.comp.worldguard.WGPlugin;
import net.Indyuce.moarbows.comp.worldguard.WorldGuardOff;
import net.Indyuce.moarbows.comp.worldguard.WorldGuardOn;
import net.Indyuce.moarbows.gui.listener.GuiListener;
import net.Indyuce.moarbows.listener.ArrowLand;
import net.Indyuce.moarbows.listener.HitEntity;
import net.Indyuce.moarbows.listener.ItemPrevents;
import net.Indyuce.moarbows.listener.PlayerListener;
import net.Indyuce.moarbows.listener.ShootBow;
import net.Indyuce.moarbows.manager.ArrowManager;
import net.Indyuce.moarbows.manager.BowManager;
import net.Indyuce.moarbows.manager.ConfigManager;
import net.Indyuce.moarbows.version.ServerVersion;
import net.Indyuce.moarbows.version.SpigotPlugin;
import net.Indyuce.moarbows.version.nms.NMSHandler;

public class MoarBows extends JavaPlugin {
	public static MoarBows plugin;

	private NMSHandler nms;
	private WGPlugin wgPlugin;
	private ConfigManager language;
	private ServerVersion version;
	private BowManager bowManager;
	private final ArrowManager arrowManager = new ArrowManager();

	public void onLoad() {
		plugin = this;
		version = new ServerVersion(Bukkit.getServer().getClass());
		wgPlugin = getServer().getPluginManager().getPlugin("WorldGuard") != null ? new WorldGuardOn() : new WorldGuardOff();
		bowManager = new BowManager();
	}

	public void onEnable() {
		bowManager.stopRegistration();
		new SpigotPlugin(36387, this).checkForUpdate();

		try {

			// nms handle
			getLogger().log(Level.INFO, "Detected Bukkit Version: " + version.toString());
			nms = (NMSHandler) Class.forName("net.Indyuce.moarbows.version.nms.NMSHandler_" + version.toString().substring(1)).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException exception) {
			getLogger().log(Level.WARNING, "Your server version is not compatible.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		new Metrics(this);

		saveDefaultConfig();
		language = new ConfigManager();

		Bukkit.getServer().getPluginManager().registerEvents(new BowUtils(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GuiListener(), this);

		Bukkit.getServer().getPluginManager().registerEvents(new ShootBow(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ItemPrevents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new HitEntity(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ArrowLand(), this);
		if (getConfig().getBoolean("hand-particles.enabled"))
			Bukkit.getScheduler().runTaskTimer(this, () -> Bukkit.getOnlinePlayers().forEach(player -> PlayerData.get(player).updateItems()), 100,
					10);

		bowManager.getBows().stream().filter(bow -> bow instanceof Listener)
				.forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents((Listener) listener, this));

		/*
		 * setup player datas for players that have been online e.g during a
		 * reload, so the plugin doesn't crash since normally the player datas
		 * are initialized whenever players log on the server
		 */
		Bukkit.getOnlinePlayers().forEach(player -> PlayerData.setup(player));
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

		// commands
		getCommand("moarbows").setExecutor(new MoarBowsCommand());
		getCommand("moarbows").setTabCompleter(new MoarBowsCompletion());

		// crafting recipes
		if (!getConfig().getBoolean("disable-bow-craftings"))
			bowLoop: for (MoarBow bow : bowManager.getBows())
				if (bow.isCraftEnabled()) {
					ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(MoarBows.plugin, "MoarBows_" + bow.getId()), bow.getItem(1));
					recipe.shape(new String[] { "ABC", "DEF", "GHI" });
					char[] chars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };
					List<String> list = Arrays.asList(bow.getFormattedCraftingRecipe());
					for (int j = 0; j < 9; j++) {
						char c = chars[j];
						if (list.size() != 3) {
							getLogger().log(Level.WARNING, "Couldn't register the recipe of " + bow.getId() + " (format error)");
							continue bowLoop;
						}

						List<String> line = Arrays.asList(list.get(j / 3).split("\\,"));
						if (line.size() < 3) {
							getLogger().log(Level.WARNING, "Couldn't register the recipe of " + bow.getId() + " (format error)");
							continue bowLoop;
						}

						String s = line.get(j % 3);
						Material material = null;
						try {
							material = Material.valueOf(s.replace("-", "_").toUpperCase());
						} catch (Exception e1) {
							getLogger().log(Level.WARNING,
									"Couldn't register the recipe of " + bow.getId() + " (" + s.split("\\:")[0] + " is not a valid material)");
							continue bowLoop;
						}

						recipe.setIngredient(c, material);
					}

					Bukkit.addRecipe(recipe);
				}
	}

	public BowManager getBowManager() {
		return bowManager;
	}

	public ArrowManager getArrowManager() {
		return arrowManager;
	}

	public NMSHandler getNMS() {
		return nms;
	}

	public ConfigManager getLanguage() {
		return language;
	}

	public ServerVersion getVersion() {
		return version;
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