package net.Indyuce.moarbows;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import net.Indyuce.moarbows.api.Message;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.version.VersionSound;

public class GUI implements Listener {

	static int getAvailableSlot(Inventory inv) {
		Integer[] slots = new Integer[] { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43 };
		for (int available : slots)
			if (inv.getItem(available) == null)
				return available;
		return -1;
	}

	public static void openInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.UNDERLINE + Message.GUI_NAME.translate());

		for (MoarBow bow : MoarBows.getBows())
			inv.setItem(getAvailableSlot(inv), bow.getItem());

		p.openInventory(inv);
	}

	public static String statsInLore(FileConfiguration config, String lore1, String main_name) {
		if (lore1.contains("#")) {
			String stat = lore1.split("#")[1];
			double mod = config.getDouble(main_name + "." + stat);

			lore1 = lore1.replace("#" + stat + "#", ChatColor.WHITE + "" + mod + "" + ChatColor.GRAY);
			lore1 = statsInLore(config, lore1, main_name);
		}
		return lore1;
	}

	@EventHandler
	public void a(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (!e.getInventory().getName().equals(ChatColor.UNDERLINE + Message.GUI_NAME.translate()))
			return;

		e.setCancelled(true);
		if (e.getClickedInventory() != e.getInventory())
			return;

		p.getWorld().playSound(p.getLocation(), VersionSound.BLOCK_NOTE_PLING.getSound(), 1, 2);
		p.getInventory().addItem(e.getCurrentItem());
	}
}