package net.Indyuce.moarbows.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.api.MoarBow;

// only use MoarBow.getFromDisplayName in this
// class class since MMOItems already deals with
// bows that have the MMOITEMS_MOAR_BOW_ID nbttag
public class ItemPrevents implements Listener {
	@EventHandler
	public void a(InventoryClickEvent e) {
		if (e.getClickedInventory() == null)
			return;

		if (e.getClickedInventory().getType() != InventoryType.ANVIL || !MoarBows.plugin.getConfig().getBoolean("disable-repair") || e.getSlot() != 2)
			return;

		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getClickedInventory().getItem(0);
		if (!p.hasPermission("moarbows.repair") && BowUtils.isPluginItem(i, false))
			if (MoarBow.getFromDisplayName(i) != null)
				e.setCancelled(true);
	}

	@EventHandler
	public void b(EnchantItemEvent e) {
		if (!MoarBows.plugin.getConfig().getBoolean("disable-enchanting"))
			return;

		ItemStack i = e.getItem();
		if (!BowUtils.isPluginItem(i, false))
			return;

		Player p = e.getEnchanter();
		if (!p.hasPermission("moarbows.enchant") && BowUtils.isPluginItem(i, false))
			if (MoarBow.getFromDisplayName(i) != null)
				e.setCancelled(true);
	}

	@EventHandler
	public void c(InventoryClickEvent e) {
		if (e.getClickedInventory() == null)
			return;

		if (e.getClickedInventory().getType() != InventoryType.ANVIL || e.getSlot() != 2)
			return;

		ItemStack i = e.getCurrentItem();
		if (!BowUtils.isPluginItem(i, false))
			return;

		Player p = (Player) e.getWhoClicked();
		if (!p.hasPermission("moarbows.anvil-create") && BowUtils.isPluginItem(i, false))
			if (MoarBow.getFromDisplayName(i) != null)
				e.setCancelled(true);
	}
}
