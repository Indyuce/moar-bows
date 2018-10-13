package net.Indyuce.moarbows;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import net.Indyuce.moarbows.api.LanguageManager;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.bow.Marked_Bow;
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
import net.Indyuce.moarbows.listener.HandParticles;
import net.Indyuce.moarbows.listener.HitEntity;
import net.Indyuce.moarbows.listener.ItemPrevents;
import net.Indyuce.moarbows.listener.ShootBow;
import net.Indyuce.moarbows.listener.UpdateNotify;
import net.Indyuce.moarbows.version.ServerVersion;
import net.Indyuce.moarbows.version.SpigotPlugin;
import net.Indyuce.moarbows.version.nms.NMSHandler;

public class MoarBows extends JavaPlugin {

	// where all bows are stored
	// bows need different bow IDs otherwise it just overrides
	private static HashMap<String, MoarBow> map = new HashMap<String, MoarBow>();

	// interfaces
	public static WGPlugin wgPlugin;
	private static NMSHandler nms;

	// plugin
	public static MoarBows plugin;
	private static LanguageManager language;
	private static ServerVersion version;
	private static SpigotPlugin spigotPlugin;

	// must register the bows before the plugin is enabled
	// otherwise it can't generate the required config files
	private static boolean registration = true;

	public void onLoad() {
		plugin = this;

		version = new ServerVersion(Bukkit.getServer().getClass());

		// load default bows
		try {
			JarFile file = new JarFile(getFile());
			for (Enumeration<JarEntry> entry = file.entries(); entry.hasMoreElements();) {
				JarEntry jarEntry = entry.nextElement();
				String name = jarEntry.getName().replace("/", ".");

				// check non anonymous class
				// check real class
				if (name.endsWith(".class") && !name.contains("$") && name.startsWith("net.Indyuce.moarbows.bow."))
					registerBow((MoarBow) Class.forName(name.substring(0, name.length() - 6)).newInstance());
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// register WG flags before it is enabled
		wgPlugin = getServer().getPluginManager().getPlugin("WorldGuard") != null ? new WorldGuardOn() : new WorldGuardOff();
	}

	@SuppressWarnings("deprecation")
	public void onEnable() {

		// other plugins must register bows while the plugin is loading
		// otherwise the plugin can't create config files for the bows
		registration = false;

		// check for latest version
		spigotPlugin = new SpigotPlugin(this, 36387);
		if (spigotPlugin.isOutOfDate())
			for (String s : spigotPlugin.getOutOfDateMessage())
				getLogger().log(Level.INFO, "\u001B[32m" + s + "\u001B[37m");

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

		// config files
		saveDefaultConfig();

		Bukkit.getServer().getPluginManager().registerEvents(new BowUtils(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GuiListener(), this);

		Bukkit.getServer().getPluginManager().registerEvents(new ShootBow(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ItemPrevents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new HitEntity(), this);
		Bukkit.getServer().getPluginManager().registerEvents(version.isBelowOrEqual(1, 9) ? new ArrowLand_v1_8() : new ArrowLand(), this);
		if (getConfig().getBoolean("update-notify"))
			Bukkit.getServer().getPluginManager().registerEvents(new UpdateNotify(), this);
		if (getConfig().getBoolean("hand-particles.enabled"))
			new HandParticles();

		Bukkit.getServer().getPluginManager().registerEvents(new Marked_Bow(), this);

		// worldguard flags
		if (getServer().getPluginManager().isPluginEnabled("WorldGuard"))
			getLogger().log(Level.INFO, "Hooked onto WorldGuard");

		language = new LanguageManager();

		// commands
		getCommand("moarbows").setExecutor(new MoarBowsCommand());
		getCommand("moarbows").setTabCompleter(new MoarBowsCompletion());

		// crafting recipes
		if (!getConfig().getBoolean("disable-bow-craftings"))
			for (MoarBow bow : getBows())
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

						List<String> line = Arrays.asList(list.get(j / 3).split(Pattern.quote(",")));
						if (line.size() < 3) {
							getLogger().log(Level.WARNING, "Couldn't register the recipe of " + bow.getID() + " (format error)");
							check = false;
							break;
						}

						String s = line.get(j % 3);
						Material material = null;
						try {
							material = Material.valueOf(s.split(Pattern.quote(":"))[0].replace("-", "_").toUpperCase());
						} catch (Exception e1) {
							getLogger().log(Level.WARNING, "Couldn't register the recipe of " + bow.getID() + " (" + s.split(Pattern.quote(":"))[0] + " is not a valid material)");
							check = false;
							break;
						}

						if (s.contains(":")) {
							int durability = 0;
							try {
								durability = Integer.parseInt(s.split(Pattern.quote(":"))[1]);
							} catch (Exception e1) {
								getLogger().log(Level.WARNING, "Couldn't register the recipe of " + bow.getID() + " (" + s.split(Pattern.quote(":"))[1] + " is not a valid number)");
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

	public static boolean canRegisterBows() {
		return registration;
	}

	public static boolean hasBow(String id) {
		return map.containsKey(id);
	}

	public static MoarBow getBow(String id) {
		return map.get(id);
	}

	@Deprecated
	public static MoarBow safeGetBow(String id) {
		return map.containsKey(id) ? map.get(id) : null;
	}

	public static Collection<MoarBow> getBows() {
		return map.values();
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

	public static SpigotPlugin getSpigotPlugin() {
		return spigotPlugin;
	}

	public static void registerBow(MoarBow bow) {

		// check for register boolean
		if (!canRegisterBows()) {
			MoarBows.plugin.getLogger().log(Level.WARNING, "Failed attempt to register " + bow.getID() + "!");
			MoarBows.plugin.getLogger().log(Level.WARNING, "Please register the bows when the plugin is loading.");
			return;
		}

		// register
		map.put(bow.getID(), bow);
	}
}