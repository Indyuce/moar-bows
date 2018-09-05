package net.Indyuce.moarbows;

import java.util.ArrayList;
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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.Message;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.bow.Marked_Bow;
import net.Indyuce.moarbows.command.MoarBowsCommand;
import net.Indyuce.moarbows.command.completion.MoarBowsCompletion;
import net.Indyuce.moarbows.comp.ArrowLand_v1_8;
import net.Indyuce.moarbows.comp.Version_1_12;
import net.Indyuce.moarbows.comp.worldguard.WGPlugin;
import net.Indyuce.moarbows.comp.worldguard.WorldGuardOff;
import net.Indyuce.moarbows.comp.worldguard.WorldGuardOn;
import net.Indyuce.moarbows.listener.ArrowLand;
import net.Indyuce.moarbows.listener.HandParticles;
import net.Indyuce.moarbows.listener.HitEntity;
import net.Indyuce.moarbows.listener.ItemPrevents;
import net.Indyuce.moarbows.listener.ShootBow;
import net.Indyuce.moarbows.util.Utils;
import net.Indyuce.moarbows.util.VersionUtils;
import net.Indyuce.moarbows.version.nms.NMSHandler;

public class MoarBows extends JavaPlugin {

	// where all bows are stored
	// bows need different bow IDs otherwise it just overrides
	private static HashMap<String, MoarBow> map = new HashMap<String, MoarBow>();

	// plugin instances
	public static MoarBows plugin;
	public static WGPlugin wgPlugin;
	private static NMSHandler nms;

	// saves temporarily the files to it is more accessible
	// improves performance, easier to access
	public static FileConfiguration bows;
	public static FileConfiguration messages;

	// must register the bows before the plugin is enabled
	// otherwise it can't generate the required config files
	private static boolean registration = true;

	public void onLoad() {
		plugin = this;

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

		// version
		try {
			VersionUtils.version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
			VersionUtils.splitVersion = VersionUtils.version.split("\\_");
			getLogger().log(Level.INFO, "Detected Bukkit Version: " + VersionUtils.version);

			nms = (NMSHandler) Class.forName("net.Indyuce.moarbows.version.nms.NMSHandler_" + VersionUtils.version.substring(1)).newInstance();
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Your server version is not compatible.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		Bukkit.getServer().getPluginManager().registerEvents(new Utils(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GUI(), this);

		Bukkit.getServer().getPluginManager().registerEvents(new ShootBow(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ItemPrevents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new HitEntity(), this);
		if (VersionUtils.isBelow(1, 8))
			Bukkit.getServer().getPluginManager().registerEvents(new ArrowLand_v1_8(), this);
		else
			Bukkit.getServer().getPluginManager().registerEvents(new ArrowLand(), this);
		HandParticles.initialize();

		Bukkit.getServer().getPluginManager().registerEvents(new Marked_Bow(), this);

		// worldguard flags
		if (getServer().getPluginManager().isPluginEnabled("WorldGuard"))
			getLogger().log(Level.INFO, "WorldGuard detected: enabled flags.");

		// config files
		saveDefaultConfig();

		// initialize bow configs
		FileConfiguration bows = ConfigData.getCD(this, "", "bows");
		for (MoarBow b : map.values()) {
			if (!bows.contains(b.getID()))
				bows.createSection(b.getID());

			List<String> lore = new ArrayList<String>(Arrays.asList(b.getLore()));
			if (lore != null && !lore.isEmpty()) {
				lore.add(0, "&8&m------------------------------");
				lore.add("&8&m------------------------------");
			}

			String[] paths = new String[] { "name", "lore", "cooldown", "durability", "craft-enabled", "craft", "eff" };
			Object[] values = new Object[] { b.getName(), lore, b.getCooldown(), b.getDurability(), b.getFormattedCraftingRecipe().length > 0, Arrays.asList(b.getFormattedCraftingRecipe()), b.getParticleEffect() };
			ConfigurationSection section = bows.getConfigurationSection(b.getID());
			for (int j = 0; j < paths.length; j++)
				if (!section.contains(paths[j]))
					bows.set(b.getID() + "." + paths[j], values[j]);

			for (BowModifier mod : b.getModifiers())
				if (!section.contains(mod.getPath()))
					bows.set(b.getID() + "." + mod.getPath(), mod.getDefaultValue());

			b.update(bows);
		}
		ConfigData.saveCD(this, bows, "", "bows");
		MoarBows.bows = bows;

		// in-game messages
		FileConfiguration messages = ConfigData.getCD(this, "", "messages");
		for (Message pa : Message.values()) {
			String path = pa.name().toLowerCase().replace("_", "-");
			if (!messages.contains(path))
				messages.set(path, pa.value);
		}
		ConfigData.saveCD(this, messages, "", "messages");
		MoarBows.messages = messages;

		// commands
		getCommand("moarbows").setExecutor(new MoarBowsCommand());
		getCommand("moarbows").setTabCompleter(new MoarBowsCompletion());

		// crafting recipes
		if (!getConfig().getBoolean("disable-bow-craftings"))
			for (MoarBow b : map.values())
				if (bows.getBoolean(b.getID() + ".craft-enabled")) {
					ShapedRecipe recipe = VersionUtils.isBelow(1, 11) ? new ShapedRecipe(b.getItem()) : new ShapedRecipe(Version_1_12.key(b.getID()), b.getItem());
					recipe.shape(new String[] { "ABC", "DEF", "GHI" });
					char[] chars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };
					boolean check = true;
					List<String> list = bows.getStringList(b.getID() + ".craft");
					for (int j = 0; j < 9; j++) {
						char c = chars[j];
						if (list.size() != 3) {
							getLogger().log(Level.WARNING, "Couldn't register the recipe of " + b.getID() + " (format error)");
							check = false;
							break;
						}
						List<String> line = Arrays.asList(list.get(j / 3).split(Pattern.quote(",")));
						if (line.size() < 3) {
							getLogger().log(Level.WARNING, "Couldn't register the recipe of " + b.getID() + " (format error)");
							check = false;
							break;
						}
						String s = line.get(j % 3);
						Material material = null;
						try {
							material = Material.valueOf(s.split(Pattern.quote(":"))[0].replace("-", "_").toUpperCase());
						} catch (Exception e1) {
							getLogger().log(Level.WARNING, "Couldn't register the recipe of " + b.getID() + " (" + s.split(Pattern.quote(":"))[0] + " is not a valid material)");
							check = false;
							break;
						}
						if (s.contains(":")) {
							int durability = 0;
							try {
								durability = Integer.parseInt(s.split(Pattern.quote(":"))[1]);
							} catch (Exception e1) {
								getLogger().log(Level.WARNING, "Couldn't register the recipe of " + b.getID() + " (" + s.split(Pattern.quote(":"))[1] + " is not a valid number)");
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