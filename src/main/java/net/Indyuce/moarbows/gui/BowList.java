package net.Indyuce.moarbows.gui;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.MoarBow;

public class BowList extends PluginInventory {
	public BowList(Player player) {
		super(player);
	}

	@Override
	public Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(this, 54, "Available Bows");
		for (MoarBow bow : MoarBows.plugin.getBowManager().getBows())
			inv.setItem(getAvailableSlot(inv), bow.getItem(1));
		return inv;
	}

	@Override
	public boolean whenClicked(InventoryClickEvent event) {
		player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
		player.getInventory().addItem(event.getCurrentItem());
		return true;
	}

	private int getAvailableSlot(Inventory inv) {
		Integer[] slots = new Integer[] { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42,
				43 };
		for (int available : slots)
			if (inv.getItem(available) == null)
				return available;
		return -1;
	}
}