package net.Indyuce.moarbows.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.Indyuce.moarbows.ConfigData;
import net.Indyuce.moarbows.MoarBows;

public class LanguageManager {
	private FileConfiguration bows;
	private FileConfiguration language;

	public LanguageManager() {
		FileConfiguration bows = ConfigData.getCD(MoarBows.plugin, "", "bows");
		for (MoarBow b : MoarBows.getBows()) {
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
		ConfigData.saveCD(MoarBows.plugin, bows, "", "bows");

		FileConfiguration language = ConfigData.getCD(MoarBows.plugin, "", "language");
		for (Message pa : Message.values()) {
			String path = pa.name().toLowerCase().replace("_", "-");
			if (!language.contains(path))
				language.set(path, pa.getDefaultValue());
		}
		ConfigData.saveCD(MoarBows.plugin, language, "", "language");

		reloadConfigFiles();
	}

	public FileConfiguration getBows() {
		return bows;
	}

	public String getTranslation(String path) {
		return language.getString(path);
	}

	public void reloadConfigFiles() {
		bows = ConfigData.getCD(MoarBows.plugin, "", "bows");
		language = ConfigData.getCD(MoarBows.plugin, "", "language");
	}
}
