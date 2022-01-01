package net.Indyuce.moarbows.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;

public class ItemPrevents implements Listener {
	@EventHandler
	public void a(InventoryClickEvent event) {
		if (event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.ANVIL || !MoarBows.plugin.getLanguage().disableRepair || event.getSlot() != 2)
			return;

		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getClickedInventory().getItem(0);
		if (!player.hasPermission("moarbows.repair") && MoarBows.plugin.getBowManager().get(item) != null)
			event.setCancelled(true);
	}

	@EventHandler
	public void b(EnchantItemEvent event) {
		if (!MoarBows.plugin.getLanguage().disableEnchant)
			return;

		ItemStack item = event.getItem();
		if (!BowUtils.isPluginItem(item, false))
			return;

		Player player = event.getEnchanter();
		if (!player.hasPermission("moarbows.enchant") && MoarBows.plugin.getBowManager().get(item) != null)
			event.setCancelled(true);
	}

	@EventHandler
	public void c(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;

		if (event.getClickedInventory().getType() != InventoryType.ANVIL || event.getSlot() != 2)
			return;

		ItemStack item = event.getCurrentItem();
		if (!BowUtils.isPluginItem(item, false))
			return;

		Player player = (Player) event.getWhoClicked();
		if (!player.hasPermission("moarbows.anvil") && MoarBows.plugin.getBowManager().get(item) != null)
			event.setCancelled(true);
	}
}
