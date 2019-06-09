package net.Indyuce.moarbows.manager;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.MoarBow;

public class BowManager {

	/*
	 * bows are registered in this map using their bow IDs and two bows with the
	 * same ID will override.
	 */
	private Map<String, MoarBow> map = new HashMap<>();

	/*
	 * the plugin must register the bows before the plugin is enabled otherwise
	 * it can't generate the required config files
	 */
	private boolean registration = true;

	public BowManager() {
		try {
			JarFile file = new JarFile(MoarBows.getJarFile());
			for (Enumeration<JarEntry> entry = file.entries(); entry.hasMoreElements();) {
				JarEntry jarEntry = entry.nextElement();
				String name = jarEntry.getName().replace("/", ".");

				// check for real & non anonymous classes
				if (name.endsWith(".class") && !name.contains("$") && name.startsWith("net.Indyuce.moarbows.bow."))
					register((MoarBow) Class.forName(name.substring(0, name.length() - 6)).newInstance());
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void register(MoarBow bow) {
		if (!registration) {
			MoarBows.plugin.getLogger().log(Level.WARNING, "Could not register the bow called " + bow.getID() + ". Make sure you register it before MMOItems loads.");
			return;
		}

		map.put(bow.getID(), bow);
	}

	public void stopRegistration() {
		registration = false;
	}

	public Set<MoarBow> getListeners() {
		return map.values().stream().filter(bow -> bow instanceof Listener).collect(Collectors.toSet());
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

		String tag = MoarBows.getNMS().getStringTag(item, "MMOITEMS_MOARBOWS_ID");
		if (tag.equals(""))
			return null;

		return tag.equals("") ? null : get(tag);
	}

	public MoarBow getFromName(ItemStack item) {
		for (MoarBow bow : getBows())
			if (bow.getName().equals(item.getItemMeta().getDisplayName()))
				return bow;
		return null;
	}
}
