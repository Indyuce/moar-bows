package net.Indyuce.moarbows.api;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import net.Indyuce.moarbows.MoarBows;

public class ConfigData {
	private Plugin plugin;
	private String path, name;
	private FileConfiguration config;

	public ConfigData(String name) {
		this("", name);
	}

	public ConfigData(Plugin plugin, String name) {
		this(plugin, "", name);
	}

	public ConfigData(String path, String name) {
		this(MoarBows.plugin, path, name);
	}

	public ConfigData(Plugin plugin, String path, String name) {
		this.plugin = plugin;
		this.path = path;
		this.name = name;

		config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + path, name + ".yml"));
	}

	public void save() {
		try {
			config.save(new File(plugin.getDataFolder() + path, name + ".yml"));
		} catch (IOException e2) {
			plugin.getLogger().log(Level.SEVERE, "Could not save " + name + ".yml");
		}
	}

	public FileConfiguration getConfig() {
		return config;
	}
}