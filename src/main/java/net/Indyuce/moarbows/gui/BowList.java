package net.Indyuce.moarbows.gui;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.bow.MoarBow;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class BowList extends PluginInventory {
    private final List<MoarBow> bows = new ArrayList<>();
    private final int maxPage;

    private int page = 1;

    private static final int[] SLOTS = {00, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
    private static final int BOWS_PER_PAGE = SLOTS.length;
    private static final NamespacedKey INV_FUNCTION_NAMESPACED_KEY = new NamespacedKey(MoarBows.plugin, "InventoryFunction");

    public BowList(Player player) {
        super(player);

        bows.addAll(MoarBows.plugin.getBowManager().getBows());
        maxPage = Math.max((int) Math.ceil(bows.size() / BOWS_PER_PAGE), 1);
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, 54, "Available Bows (" + page + "/" + maxPage + ")");

        for (int j = 0; j < BOWS_PER_PAGE; j++) {
            int index = (page - 1) * BOWS_PER_PAGE + j;
            if (bows.size() == index)
                break;

            inv.setItem(getAvailableSlot(inv), bows.get(index).getItem(1));
        }

        ItemStack prev = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = prev.getItemMeta();
        prevMeta.setDisplayName(ChatColor.WHITE + "Previous Page");
        prevMeta.getPersistentDataContainer().set(INV_FUNCTION_NAMESPACED_KEY, PersistentDataType.STRING, "prev");
        prev.setItemMeta(prevMeta);
        inv.setItem(45, prev);

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(ChatColor.WHITE + "Next Page");
        nextMeta.getPersistentDataContainer().set(INV_FUNCTION_NAMESPACED_KEY, PersistentDataType.STRING, "next");
        next.setItemMeta(nextMeta);
        inv.setItem(53, next);

        return inv;
    }

    @Override
    public boolean whenClicked(InventoryClickEvent event) {

        if (!BowUtils.isPluginItem(event.getCurrentItem(), false))
            return false;

        // Pagination
        ItemMeta meta = event.getCurrentItem().getItemMeta();
        if (meta.getPersistentDataContainer().has(INV_FUNCTION_NAMESPACED_KEY, PersistentDataType.STRING)) {
            String tag = meta.getPersistentDataContainer().get(INV_FUNCTION_NAMESPACED_KEY, PersistentDataType.STRING);
            if (tag.equals("prev") && page > 0) {
                page--;
                open();
                return true;
            }

            if (tag.equals("next") && page < maxPage) {
                page++;
                open();
                return true;
            }

            return true;
        }

        // Give Bow
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
        player.getInventory().addItem(event.getCurrentItem());
        return true;
    }

    private int getAvailableSlot(Inventory inv) {
        Integer[] slots = new Integer[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42,
                43};
        for (int available : slots)
            if (inv.getItem(available) == null)
                return available;
        return -1;
    }
}