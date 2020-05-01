package net.Indyuce.moarbows.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.Modifier;
import net.Indyuce.moarbows.api.util.ConfigData;
import net.Indyuce.moarbows.api.util.Message;

public class ConfigManager {

	/*
	 * cache message config TODO store inside a map
	 */
	private final ConfigData language;

	/*
	 * cache config options for easier access later
	 */
	public final boolean fullPullRestriction, arrowParticles, disableEnchant, disableRepair, unbreakable, hideUnbreakable, hideEnchants;

	public ConfigManager() {

		MoarBows.plugin.reloadConfig();

		ConfigData bows = new ConfigData("bows");
		for (MoarBow bow : MoarBows.plugin.getBowManager().getBows()) {
			if (!bows.getConfig().contains(bow.getId()))
				bows.getConfig().createSection(bow.getId());

			List<String> lore = new ArrayList<>(bow.getLore());
			if (lore != null && !lore.isEmpty()) {
				lore.add(0, "");
				lore.add("");
				lore.add("&e{cooldown}s Cooldown");
			}

			String[] paths = { "name", "lore", "durability", "craft-enabled", "craft", "eff" };
			Object[] values = { bow.getUncoloredName(), lore, bow.getData(), bow.getFormattedCraftingRecipe().length > 0,
					Arrays.asList(bow.getFormattedCraftingRecipe()), bow.getParticles().toString() };
			ConfigurationSection section = bows.getConfig().getConfigurationSection(bow.getId());
			for (int j = 0; j < paths.length; j++)
				if (!section.contains(paths[j]))
					bows.getConfig().set(bow.getId() + "." + paths[j], values[j]);

			for (Modifier modifier : bow.getModifiers())
				if (!section.contains(modifier.getPath()))
					modifier.setup(section);

			try {
				// update all bows, very important
				bow.update(bows.getConfig().getConfigurationSection(bow.getId()));

			} catch (IllegalArgumentException exception) {
				MoarBows.plugin.getLogger().log(Level.WARNING, "Could not load " + bow.getId() + ": " + exception.getMessage());
			}
		}
		bows.save();

		language = new ConfigData("language");
		for (Message message : Message.values()) {
			String path = message.name().toLowerCase().replace("_", "-");
			if (!language.getConfig().contains(path))
				language.getConfig().set(path, message.getDefaultValue());
		}
		language.save();

		fullPullRestriction = MoarBows.plugin.getConfig().getBoolean("full-pull-restriction");
		arrowParticles = MoarBows.plugin.getConfig().getBoolean("hand-particles.enabled");
		disableEnchant = MoarBows.plugin.getConfig().getBoolean("disable.enchant");
		disableRepair = MoarBows.plugin.getConfig().getBoolean("disable.repair");

		unbreakable = MoarBows.plugin.getConfig().getBoolean("bow-options.unbreakable");
		hideUnbreakable = MoarBows.plugin.getConfig().getBoolean("bow-options.hide-unbreakable");
		hideEnchants = MoarBows.plugin.getConfig().getBoolean("bow-options.hide-enchants");
	}

	public String getTranslation(String path) {
		return language.getConfig().getString(path);
	}
}
