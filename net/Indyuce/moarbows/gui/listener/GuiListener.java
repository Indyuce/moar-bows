package net.Indyuce.moarbows.gui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.gui.PluginInventory;

public class GuiListener implements Listener {
	@EventHandler
	public void a(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		if (event.getInventory().getHolder() instanceof PluginInventory)
			if (event.getClickedInventory() == event.getInventory() && BowUtils.isPluginItem(item, false))
				if (((PluginInventory) event.getInventory().getHolder()).whenClicked(event))
					event.setCancelled(true);
	}
}