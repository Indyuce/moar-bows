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

// only use MoarBow.getFromDisplayName in this
// class class since MMOItems already deals with
// bows that have the MMOITEMS_MOAR_BOW_ID nbttag
public class ItemPrevents implements Listener {
	@EventHandler
	public void a(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;

		if (event.getClickedInventory().getType() != InventoryType.ANVIL || !MoarBows.plugin.getConfig().getBoolean("disable-repair") || event.getSlot() != 2)
			return;

		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getClickedInventory().getItem(0);
		if (!player.hasPermission("moarbows.repair") && BowUtils.isPluginItem(item, false))
			if (MoarBows.getFromName(item) != null)
				event.setCancelled(true);
	}

	@EventHandler
	public void b(EnchantItemEvent event) {
		if (!MoarBows.plugin.getConfig().getBoolean("disable-enchanting"))
			return;

		ItemStack item = event.getItem();
		if (!BowUtils.isPluginItem(item, false))
			return;

		Player player = event.getEnchanter();
		if (!player.hasPermission("moarbows.enchant") && BowUtils.isPluginItem(item, false))
			if (MoarBows.getFromName(item) != null)
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
		if (!player.hasPermission("moarbows.anvil-create") && BowUtils.isPluginItem(item, false))
			if (MoarBows.getFromName(item) != null)
				event.setCancelled(true);
	}
}
