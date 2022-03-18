package net.Indyuce.moarbows.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.bow.MoarBow;
import net.Indyuce.moarbows.util.ConfigFile;

public class ConfigManager {

	/*
	 * cache message config TODO store inside a map
	 */
	private final ConfigFile language;

	/*
	 * cache config options for easier access later
	 */
	public final boolean fullPullRestriction, arrowParticles, disableEnchant, disableRepair, unbreakable, hideUnbreakable, hideEnchants;

	public ConfigManager() {

		MoarBows.plugin.reloadConfig();

		loadDefaultFile("bows.yml");
		loadDefaultFile("language.yml");
		language = new ConfigFile("language");

		FileConfiguration bows = new ConfigFile("bows").getConfig();
		for (MoarBow bow : MoarBows.plugin.getBowManager().getBows())
			try {
				bow.update(bows.getConfigurationSection(bow.getId()));
			} catch (Exception exception) {
				MoarBows.plugin.getLogger().log(Level.INFO, "Could not reload bow '" + bow.getId() + "': " + exception.getMessage());
			}

		fullPullRestriction = MoarBows.plugin.getConfig().getBoolean("full-pull-restriction");
		arrowParticles = MoarBows.plugin.getConfig().getBoolean("hand-particles.enabled");
		disableEnchant = MoarBows.plugin.getConfig().getBoolean("disable.enchant");
		disableRepair = MoarBows.plugin.getConfig().getBoolean("disable.repair");

		unbreakable = MoarBows.plugin.getConfig().getBoolean("bow-options.unbreakable");
		hideUnbreakable = MoarBows.plugin.getConfig().getBoolean("bow-options.hide-unbreakable");
		hideEnchants = MoarBows.plugin.getConfig().getBoolean("bow-options.hide-enchants");
	}

	public String formatMessage(String path, Object... placeholders) {
		String str = ChatColor.translateAlternateColorCodes('&', language.getConfig().getString(path, "<TranslationNotFound:" + path + ">"));
		for (int j = 0; j < placeholders.length; j += 2)
			str = str.replace("{" + placeholders[j] + "}", placeholders[j + 1].toString());
		return str;
	}

	public void loadDefaultFile(String name) {
		try {
			File file = new File(MoarBows.plugin.getDataFolder(), name);
			if (!file.exists())
				Files.copy(MoarBows.plugin.getResource("default/" + name), file.getAbsoluteFile().toPath());
		} catch (IOException exception) {
			MoarBows.plugin.getLogger().log(Level.WARNING, "Could not load default file '" + name + "': " + exception.getMessage());
		}
	}
}
