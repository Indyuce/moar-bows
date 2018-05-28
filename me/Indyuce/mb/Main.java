package me.Indyuce.mb;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import me.Indyuce.mb.command.MoarBowsCommand;
import me.Indyuce.mb.comp.Version_1_12;
import me.Indyuce.mb.comp.WorldGuardUtils;
import me.Indyuce.mb.listener.ArrowLand;
import me.Indyuce.mb.listener.HandParticles;
import me.Indyuce.mb.listener.HitEntity;
import me.Indyuce.mb.listener.ItemPrevents;
import me.Indyuce.mb.listener.ShootBow;
import me.Indyuce.mb.resource.Bow;
import me.Indyuce.mb.resource.CustomMessage;
import me.Indyuce.mb.resource.bow.MarkedBow;
import me.Indyuce.mb.util.Utils;
import me.Indyuce.mb.util.VersionUtils;

public class Main extends JavaPlugin {
	public static Main plugin;

	public void onDisable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void onLoad() {
		plugin = this;
		if (getServer().getPluginManager().getPlugin("WorldGuard") != null)
			WorldGuardUtils.setup();
	}

	@SuppressWarnings("deprecation")
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(new Utils(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new GUI(), this);

		Bukkit.getServer().getPluginManager().registerEvents(new ShootBow(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ItemPrevents(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new HitEntity(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ArrowLand(), this);
		new HandParticles();

		Bukkit.getServer().getPluginManager().registerEvents(new MarkedBow(), this);

		// version
		try {
			VersionUtils.version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
			Bukkit.getConsoleSender().sendMessage("[MoarBows] " + ChatColor.DARK_GRAY + "Detected Bukkit Version: " + VersionUtils.version);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("[MoarBows] " + ChatColor.RED + "Your server version is not compatible.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		// worldguard flags
		if (getServer().getPluginManager().isPluginEnabled("WorldGuard"))
			Bukkit.getConsoleSender().sendMessage("[MoarBows] " + ChatColor.DARK_GRAY + "WorldGuard detected: enabled flags.");

		// config files
		saveDefaultConfig();
		ConfigData.setupCD(this, "", "bows");
		ConfigData.setupCD(this, "", "messages");
		FileConfiguration bows = ConfigData.getCD(this, "", "bows");
		FileConfiguration messages = ConfigData.getCD(this, "", "messages");
		for (Bow b : Bow.values())
			b.initializeConfig(bows);
		for (CustomMessage pa : CustomMessage.values()) {
			String path = pa.name().toLowerCase().replace("_", "-");
			if (!messages.contains(path))
				messages.set(path, pa.value);
		}
		ConfigData.saveCD(this, bows, "", "bows");
		ConfigData.saveCD(this, messages, "", "messages");

		// commands
		getCommand("moarbows").setExecutor(new MoarBowsCommand());

		// crafting recipes
		for (Bow i : Bow.values())
			if (bows.getBoolean(i.name() + ".craft-enabled") && !getConfig().getBoolean("disable-all-bow-craftings")) {
				ShapedRecipe recipe = (VersionUtils.isBelow(1, 11) ? new ShapedRecipe(i.a()) : new ShapedRecipe(Version_1_12.key(i.name()), i.a()));
				recipe.shape(new String[] { "ABC", "DEF", "GHI" });
				char[] chars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I' };
				boolean check = true;
				List<String> list = bows.getStringList(i.name() + ".craft");
				for (int j = 0; j < 9; j++) {
					char c = chars[j];
					if (list.size() != 3) { 
						Bukkit.getConsoleSender().sendMessage("[MoarBows] " + ChatColor.RED + "Couldn't create the crafting recipe of " + i.name() + ". Format error.");
						check = false;
						break;
					}
					List<String> line = Arrays.asList(list.get(j / 3).split(Pattern.quote(",")));
					if (line.size() < 3) {
						Bukkit.getConsoleSender().sendMessage("[MoarBows] " + ChatColor.RED + "Couldn't create the crafting recipe of " + i.name() + ". Format error.");
						check = false;
						break;
					}
					String s = line.get(j % 3);
					Material material = null;
					try {
						material = Material.valueOf(s.split(Pattern.quote(":"))[0].replace("-", "_").toUpperCase());
					} catch (Exception e1) {
						Bukkit.getConsoleSender().sendMessage("[MoarBows] " + ChatColor.RED + "Couldn't create the crafting recipe of " + i.name() + ". " + s.split(Pattern.quote(":"))[0] + " is not a valid material.");
						check = false;
						break;
					}
					if (s.contains(":")) {
						int durability = 0;
						try {
							durability = Integer.parseInt(s.split(Pattern.quote(":"))[1]);
						} catch (Exception e1) {
							Bukkit.getConsoleSender().sendMessage("[MoarBows] " + ChatColor.RED + "Couldn't create the crafting recipe of " + i.name() + ". " + s.split(Pattern.quote(":"))[1] + " is not a valid number.");
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
}