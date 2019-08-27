package net.Indyuce.moarbows.version.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSHandler {
	NBTItem getNBTItem(ItemStack item);

	void sendJson(Player player, String message);
}
