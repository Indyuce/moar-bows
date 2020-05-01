package net.Indyuce.moarbows.manager;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.MoarBow;

public class BowManager {

	/*
	 * bows are registered in this map using their bow IDs and two bows with the
	 * same ID will override.
	 */
	private final Map<String, MoarBow> map = new HashMap<>();

	/*
	 * the plugin must register the bows before the plugin is enabled otherwise
	 * it can't generate the required config files
	 */
	private boolean registration = true;

	public BowManager() {
		try {
			JarFile file = new JarFile(MoarBows.plugin.getJarFile());
			for (Enumeration<JarEntry> entry = file.entries(); entry.hasMoreElements();) {
				JarEntry jarEntry = entry.nextElement();
				String name = jarEntry.getName().replace("/", ".");

				// check for real & non anonymous classes
				if (name.endsWith(".class") && !name.contains("$") && name.startsWith("net.Indyuce.moarbows.bow."))
					register((MoarBow) Class.forName(name.substring(0, name.length() - 6)).newInstance());
			}
			file.close();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException exception) {
			exception.printStackTrace();
		}
	}

	public void register(MoarBow bow) {
		Validate.isTrue(registration, "Could not register bow '" + bow.getId() + "': bows must be registered before MoarBows enables.");
		Validate.isTrue(!map.containsKey(bow.getId()), "Could not register '" + bow.getId() + "': a bow with the same ID is already registered.");

		map.put(bow.getId(), bow);
	}

	public void stopRegistration() {
		registration = false;
	}

	public Collection<MoarBow> getBows() {
		return map.values();
	}

	public boolean has(String id) {
		return map.containsKey(id);
	}

	/*
	 * returns null if no bow exists otherwise return
	 */
	public MoarBow safeGet(String id) {
		return map.containsKey(id) ? get(id) : null;
	}

	public MoarBow get(String id) {
		return map.get(id);
	}

	public MoarBow get(ItemStack item) {
		if (item.hasItemMeta())
			for (MoarBow bow : getBows())
				if (bow.getName().equals(item.getItemMeta().getDisplayName()))
					return bow;
		return null;
	}
}
