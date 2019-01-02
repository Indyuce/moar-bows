package net.Indyuce.moarbows.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.ConfigData;
import net.Indyuce.moarbows.api.Message;
import net.Indyuce.moarbows.api.MoarBow;

public class LanguageManager {
	private FileConfiguration bows, language;

	public LanguageManager() {
		ConfigData bows = new ConfigData("bows");
		for (MoarBow bow : MoarBows.getBowManager().getBows()) {
			if (!bows.getConfig().contains(bow.getID()))
				bows.getConfig().createSection(bow.getID());

			List<String> lore = new ArrayList<>(Arrays.asList(bow.getLore()));
			if (lore != null && !lore.isEmpty()) {
				lore.add(0, "&8&m------------------------------");
				lore.add("&8&m------------------------------");
			}

			String[] paths = { "name", "lore", "cooldown", "durability", "craft-enabled", "craft", "eff" };
			Object[] values = { bow.getUncoloredName(), lore, bow.getCooldown(), bow.getDurability(), bow.getFormattedCraftingRecipe().length > 0, Arrays.asList(bow.getFormattedCraftingRecipe()), bow.getFormattedParticleData() };
			ConfigurationSection section = bows.getConfig().getConfigurationSection(bow.getID());
			for (int j = 0; j < paths.length; j++)
				if (!section.contains(paths[j]))
					bows.getConfig().set(bow.getID() + "." + paths[j], values[j]);

			for (BowModifier modifier : bow.getModifiers())
				if (!section.contains(modifier.getPath()))
					bows.getConfig().set(bow.getID() + "." + modifier.getPath(), modifier.getDefaultValue());

			bow.update(bows.getConfig());
		}
		bows.save();

		ConfigData language = new ConfigData("language");
		for (Message message : Message.values()) {
			String path = message.name().toLowerCase().replace("_", "-");
			if (!language.getConfig().contains(path))
				language.getConfig().set(path, message.getDefaultValue());
		}
		language.save();

		reloadConfigFiles();
	}

	public FileConfiguration getBows() {
		return bows;
	}

	public double getDoubleValue(String path) {
		return bows.getDouble(path);
	}

	public String getStringValue(String path) {
		return bows.getString(path);
	}

	public boolean getBooleanValue(String path) {
		return bows.getBoolean(path);
	}

	public String getTranslation(String path) {
		return language.getString(path);
	}

	public void reloadConfigFiles() {
		bows = new ConfigData("bows").getConfig();
		language = new ConfigData("language").getConfig();
	}
}
