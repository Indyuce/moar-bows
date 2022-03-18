package net.Indyuce.moarbows.gui;

import org.antlr.v4.runtime.misc.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class PluginInventory implements InventoryHolder {
    protected final Player player;

    public PluginInventory(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract boolean whenClicked(InventoryClickEvent event);

    @Override
    @NotNull
    public abstract Inventory getInventory();

    public void open() {
        player.openInventory(getInventory());
    }
}
