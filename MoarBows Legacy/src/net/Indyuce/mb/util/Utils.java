package net.Indyuce.mb.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.api.MoarBow;

public class Utils implements Listener {
	public static HashMap<UUID, HashMap<String, Long>> cd = new HashMap<UUID, HashMap<String, Long>>();

	public static String msg(String path) {
		return ChatColor.translateAlternateColorCodes('&', Main.messages.getString(path));
	}

	public static String caseOnWords(String s) {
		StringBuilder builder = new StringBuilder(s);
		boolean isLastSpace = true;
		for (int i = 0; i < builder.length(); i++) {
			char ch = builder.charAt(i);
			if (isLastSpace && ch >= 'a' && ch <= 'z') {
				builder.setCharAt(i, (char) (ch + ('A' - 'a')));
				isLastSpace = false;
			} else if (ch != ' ')
				isLastSpace = false;
			else
				isLastSpace = true;
		}
		return builder.toString();
	}

	public static boolean consumeAmmo(Player p, ItemStack i) {
		if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR)
			return true;
		if (!p.getInventory().containsAtLeast(i, 1))
			return false;
		p.getInventory().removeItem(i);
		return true;
	}

	public static boolean checkPl(CommandSender sender, boolean msg) {
		boolean b = sender instanceof Player;
		if (!b && msg)
			sender.sendMessage(ChatColor.RED + "[MoarBows] This command is for players only.");
		return b;
	}

	public static boolean isFriendly(Entity t) {
		// BASE VERSION: 1.9.4
		List<EntityType> types = new ArrayList<EntityType>();
		for (EntityType type : new EntityType[] { EntityType.BAT, EntityType.CHICKEN, EntityType.COW, EntityType.HORSE, EntityType.IRON_GOLEM, EntityType.WOLF, EntityType.MUSHROOM_COW, EntityType.OCELOT, EntityType.PIG, EntityType.RABBIT, EntityType.SHEEP, EntityType.VILLAGER, })
			types.add(type);
		if (!VersionUtils.isBelow(1, 9))
			types.add(EntityType.valueOf("POLAR_BEAR"));
		if (!VersionUtils.isBelow(1, 10))
			types.add(EntityType.valueOf("POLAR_BEAR"));
		types.add(EntityType.valueOf("DONKEY"));
		types.add(EntityType.valueOf("LLAMA"));
		types.add(EntityType.valueOf("MULE"));
		return types.contains(t.getType());
	}

	public static boolean isPluginItem(ItemStack i, boolean lore) {
		if (i == null)
			return false;
		if (i.getItemMeta() == null)
			return false;
		if (i.getItemMeta().getDisplayName() == null)
			return false;
		if (lore && i.getItemMeta().getLore() == null)
			return false;
		return true;
	}

	public static ItemStack removeDisplayName(ItemStack i) {
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(null);
		i.setItemMeta(meta);
		return i;
	}

	public static boolean canDmgEntity(Player p, Location loc, Entity t) {
		if (t.hasMetadata("NPC"))
			return false;
		double[] boundingBox = VersionUtils.getBoundingBox(t);
		if ((loc == null ? true : loc.getX() >= boundingBox[0] - .5 && loc.getY() >= boundingBox[1] - .5 && loc.getZ() >= boundingBox[2] - .5 && loc.getX() <= boundingBox[3] + .5 && loc.getY() <= boundingBox[4] + .5 && loc.getZ() <= boundingBox[5] + .5) && t != p)
			return true;
		return false;
	}

	public static boolean canUseBow(Player p, MoarBow b, EntityShootBowEvent e) {
		if (b.getCooldown() <= 0)
			return true;

		HashMap<String, Long> cd = Utils.cd.containsKey(p.getUniqueId()) ? Utils.cd.get(p.getUniqueId()) : new HashMap<String, Long>();
		Long last = cd.containsKey(b.getID()) ? cd.get(b.getID()) : 0;
		double remaining = last + b.getCooldown() * 1000 - System.currentTimeMillis();

		if (remaining > 0) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED + msg("on-cooldown").replace("%left%", "" + MathUtils.tronc(remaining / 1000, 1)));
			return false;
		}
		cd.put(b.getID(), System.currentTimeMillis());
		Utils.cd.put(p.getUniqueId(), cd);
		return true;
	}
}
