package net.Indyuce.moarbows.manager;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.MoarBow;
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class BowManager {

    /**
     * Bows are registered in this map using their bow
     * IDs and two bows with the same ID will override
     */
    private final Map<String, MoarBow> map = new HashMap<>();

    /**
     * The plugin must register the bows before the plugin
     * is enabled otherwise it can't generate the required
     * config files
     */
    private boolean registration = true;

    public BowManager() {
        try {
            JarFile file = new JarFile(MoarBows.plugin.getJarFile());
            for (Enumeration<JarEntry> entry = file.entries(); entry.hasMoreElements(); ) {
                JarEntry jarEntry = entry.nextElement();
                String name = jarEntry.getName().replace("/", ".");

                // Check for real & non anonymous classes
                if (name.endsWith(".class") && !name.contains("$") && name.startsWith("net.Indyuce.moarbows.bow.")) {
                    Class<?> javaClass = Class.forName(name.substring(0, name.length() - 6));
                    if (javaClass.isAssignableFrom(MoarBow.class))
                        register((MoarBow) javaClass.getDeclaredConstructor().newInstance());
                }
            }
            file.close();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException | NoSuchMethodException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
    }

    public void register(MoarBow bow) {
        Validate.isTrue(registration, "Bows must be registered before MoarBows enables");
        Validate.isTrue(!map.containsKey(bow.getId()), "A bow with the same ID already exists");

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

    @Nullable
    public MoarBow get(String id) {
        return map.get(id);
    }

    @Nullable
    public MoarBow get(ItemStack item) {
        if (item == null || !item.hasItemMeta())
            return null;

        String tag = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MoarBows.plugin, "MoarBow"), PersistentDataType.STRING);
        return map.get(tag);
    }
}
