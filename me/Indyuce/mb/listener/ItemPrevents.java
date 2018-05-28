package me.Indyuce.mb.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import me.Indyuce.mb.Main;
import me.Indyuce.mb.resource.Bow;
import me.Indyuce.mb.util.Utils;

public class ItemPrevents implements Listener {
	@EventHandler
	public void a(InventoryClickEvent e) {
		if (e.getClickedInventory() == null)
			return;
		if (e.getClickedInventory().getType() != InventoryType.ANVIL || !Main.plugin.getConfig().getBoolean("disable-repair") || e.getSlot() != 2)
			return;
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getClickedInventory().getItem(0);
		if (!Utils.isPluginItem(i, false))
			return;
		if (!p.hasPermission("moarbows.repair"))
			for (Bow b : Bow.values())
				if (i.getItemMeta().getDisplayName().equals(b.name)) {
					e.setCancelled(true);
					break;
				}
	}

	@EventHandler
	public void b(EnchantItemEvent e) {
		if (!Main.plugin.getConfig().getBoolean("disable-enchanting"))
			return;
		Player p = e.getEnchanter();
		ItemStack i = e.getItem();
		if (!Utils.isPluginItem(i, false))
			return;
		if (!p.hasPermission("moarbows.enchant"))
			for (Bow b : Bow.values())
				if (i.getItemMeta().getDisplayName().equals(b.name)) {
					e.setCancelled(true);
					break;
				}
	}

	@EventHandler
	public void c(InventoryClickEvent e) {
		if (e.getClickedInventory() == null)
			return;
		if (e.getClickedInventory().getType() != InventoryType.ANVIL || e.getSlot() != 2)
			return;
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getCurrentItem();
		if (i.hasItemMeta() && !p.hasPermission("moarbows.anvil-create"))
			if (i.getItemMeta().hasDisplayName())
				for (Bow b : Bow.values())
					if (i.getItemMeta().getDisplayName().equals(b.name)) {
						e.setCancelled(true);
						break;
					}
	}
}
