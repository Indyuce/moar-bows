package net.Indyuce.moarbows.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class PluginInventory implements InventoryHolder {
	protected Player player;

	public PluginInventory(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean whenClicked(InventoryClickEvent event) {
		return false;
	}

	public void open() {
		player.openInventory(getInventory());
	}

	@Override
	public abstract Inventory getInventory();
}
