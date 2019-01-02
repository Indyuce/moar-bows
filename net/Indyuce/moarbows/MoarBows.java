package net.Indyuce.moarbows;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.PlayerData;
import net.Indyuce.moarbows.command.MoarBowsCommand;
import net.Indyuce.moarbows.command.completion.MoarBowsCompletion;
import net.Indyuce.moarbows.comp.ArrowLand_v1_8;
import net.Indyuce.moarbows.comp.Metrics;
import net.Indyuce.moarbows.comp.Version_1_12;
import net.Indyuce.moarbows.comp.worldguard.WGPlugin;
import net.Indyuce.moarbows.comp.worldguard.WorldGuardOff;
import net.Indyuce.moarbows.comp.worldguard.WorldGuardOn;
import net.Indyuce.moarbows.gui.listener.GuiListener;
import net.Indyuce.moarbows.listener.ArrowLand;
import net.Indyuce.moarbows.listener.HitEntity;
import net.Indyuce.moarbows.listener.ItemPrevents;
import net.Indyuce.moarbows.listener.PlayerListener;
import net.Indyuce.moarbows.listener.ShootBow;
import net.Indyuce.moarbows.manager.BowManager;
import net.Indyuce.moarbows.manager.LanguageManager;
import net.Indyuce.moarbows.version.ServerVersion;
import net.Indyuce.moarbows.version.SpigotPlugin;
import net.Indyuce.moarbows.version.nms.NMSHandler;

public class MoarBows extends JavaPlugin {
	public static MoarBows plugin;

	private static NMSHandler nms;
	private static WGPlugin wgPlugin;
	private static LanguageManager language;
	private static ServerVersion version;
	private static BowManager bowManager;

	public void onLoad() {
		plugin = this;
		version = new ServerVersion(Bukkit.getServer().getClass());
		wgPlugin = getServer().getPluginManager().getPlugin("WorldGuard") != null ? new WorldGuardOn() : new WorldGuardOff();
		bowManager = new BowManager();
	}

	@SuppressWarnings("deprecation")
	public void onEnable() {
		bowManager.stopRegistration();
		new SpigotPlugin(36387, this).checkForUpdate();

		try {

			// nms handle
			getLogger().log(Level.INFO, "Detected Bukkit Version: " + version.toString());
			nms = (NMSHandler) Class.forName("net.Indyuce.moarbows.version.nms.NMSHandler_" + version.toString().substring(1)).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			getLogger().log(Level.SEVERE, "Your server version is not compatible.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		new Metrics(this);
		saveDefaultConfig();
		language = new LanguageManager();

		Bukkit.getServer().getPluginManager().registerEvents(new BowUtils(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GuiListener(), this);

		Bukkit.getServer().getPluginManager().registerEvents(new ShootBow(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ItemPrevents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new HitEntity(), this);
		Bukkit.getServer().getPluginManager().registerEvents(version.isBelowOrEqual(1, 9) ? new ArrowLand_v1_8() : new ArrowLand(), this);
		if (getConfig().getBoolean("hand-particles.enabled"))
			new BukkitRunnable() {
				public void run() {
					Bukkit.getOnlinePlayers().forEach(player -> PlayerData.get(player).updateItems());
				}
			}.runTaskTimer(plugin, 100, 10);

		bowManager.getListeners().forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents((Listener) listener, this));

		/*
		 * setup player datas for players that have been online e.g during a
		 * reload, so the plugin doesn't crash since normally the player datas
		 * are initialized whenever players log on the server
		 */
		Bukkit.getOnlinePlayers().forEach(player -> PlayerData.setup(player));
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

		// worldguard flags
		if (getServer().getPluginManager().isPluginEnabled("WorldGuard"))
			getLogger().log(Level.INFO, "Hooked onto WorldGuard");

		// commands
		getCommand("moarbows").setExecutor(new MoarBowsCommand());
		getCommand("moarbows").setTabCompleter(new MoarBowsCompletion());

		// crafting recipes
		if (!getConfig().getBoolean("disable-bow-craftings"))
			for (MoarBow bow : bowManager.getBows())
				if (getLanguage().getBows().getBoolean(bow.getID() + ".craft-enabled")) {
					ShapedRecipe recipe = version.isBelowOrEqual(1, 11) ? new ShapedRecipe(bow.getItem()) : new ShapedRecipe(Version_1_12.key(bow.getID()), bow.getItem());
					recipe.shape(new String[] { "ABC", "DEF", "GHI" });
					char[] chars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };
					boolean check = true;
					List<String> list = getLanguage().getBows().getStringList(bow.getID() + ".craft");
					for (int j = 0; j < 9; j++) {
						char c = chars[j];
						if (list.size() != 3) {
							getLogger().log(Level.WARNING, "Couldn't register the recipe of " + bow.getID() + " (format error)");
							check = false;
							break;
						}

						List<String> line = Arrays.asList(list.get(j / 3).split("\\,"));
						if (line.size() < 3) {
							getLogger().log(Level.WARNING, "Couldn't register the recipe of " + bow.getID() + " (format error)");
							check = false;
							break;
						}

						String s = line.get(j % 3);
						Material material = null;
						try {
							material = Material.valueOf(s.split("\\:")[0].replace("-", "_").toUpperCase());
						} catch (Exception e1) {
							getLogger().log(Level.WARNING, "Couldn't register the recipe of " + bow.getID() + " (" + s.split("\\:")[0] + " is not a valid material)");
							check = false;
							break;
						}

						if (s.contains(":")) {
							int durability = 0;
							try {
								durability = Integer.parseInt(s.split("\\:")[1]);
							} catch (Exception e1) {
								getLogger().log(Level.WARNING, "Couldn't register the recipe of " + bow.getID() + " (" + s.split("\\:")[1] + " is not a valid number)");
								check = false;
								break;
							}
							recipe.setIngredient(c, material, durability);
							continue;
						}
						recipe.setIngredient(c, material);
					}
					if (check)
						getServer().addRecipe(recipe);
				}
	}

	public static BowManager getBowManager() {
		return bowManager;
	}

	public static NMSHandler getNMS() {
		return nms;
	}

	public static LanguageManager getLanguage() {
		return language;
	}

	public static ServerVersion getVersion() {
		return version;
	}

	public static WGPlugin getWorldGuard() {
		return wgPlugin;
	}

	public static File getJarFile() {
		return plugin.getFile();
	}
}