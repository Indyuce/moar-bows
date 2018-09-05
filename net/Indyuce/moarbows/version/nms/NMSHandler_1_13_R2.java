package net.Indyuce.moarbows.version.nms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_13_R2.AxisAlignedBB;
import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagFloat;
import net.minecraft.server.v1_13_R2.NBTTagInt;
import net.minecraft.server.v1_13_R2.NBTTagList;
import net.minecraft.server.v1_13_R2.NBTTagString;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class NMSHandler_1_13_R2 implements NMSHandler {
	@Override
	public ItemStack addTag(ItemStack i, ItemTag... tags) {
		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();

		for (ItemTag tag : tags) {
			if (tag.getValue() instanceof Boolean) {
				compound.setBoolean(tag.getPath(), (boolean) tag.getValue());
				continue;
			}
			if (tag.getValue() instanceof Double) {
				compound.setDouble(tag.getPath(), (double) tag.getValue());
				continue;
			}
			compound.setString(tag.getPath(), (String) tag.getValue());
		}

		nmsi.setTag(compound);
		i = CraftItemStack.asBukkitCopy(nmsi);
		return i;
	}

	@Override
	public List<String> getTags(ItemStack i) {
		if (i == null || i.getType() == Material.AIR)
			return new ArrayList<String>();

		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return new ArrayList<String>(compound.getKeys());
	}

	@Override
	public ItemStack addAttribute(ItemStack i, Attribute... attributes) {
		net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsStack.getTag();
		if (compound == null) {
			nmsStack.setTag(new NBTTagCompound());
			compound = nmsStack.getTag();
		}

		NBTTagList modifiers = new NBTTagList();
		for (Attribute attribute : attributes) {
			NBTTagCompound added = new NBTTagCompound();
			added.set("AttributeName", new NBTTagString("generic." + attribute.getName()));
			added.set("Name", new NBTTagString("generic." + attribute.getName()));
			added.set("Amount", new NBTTagFloat(attribute.getValue()));
			added.set("Operation", new NBTTagInt(0));
			added.set("UUIDLeast", new NBTTagInt(UUID.randomUUID().hashCode()));
			added.set("UUIDMost", new NBTTagInt(UUID.randomUUID().hashCode()));
			added.set("Slot", new NBTTagString(attribute.getSlot()));
			modifiers.add(added);
		}

		compound.set("AttributeModifiers", modifiers);
		nmsStack.setTag(compound);
		i = CraftItemStack.asBukkitCopy(nmsStack);
		return i;
	}

	@Override
	public boolean getBooleanTag(ItemStack i, String path) {
		if (i == null || i.getType() == Material.AIR)
			return false;

		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getBoolean(path);
	}

	@Override
	public double getDoubleTag(ItemStack i, String path) {
		if (i == null || i.getType() == Material.AIR)
			return 0;

		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getDouble(path);
	}

	@Override
	public String getStringTag(ItemStack i, String path) {
		if (i == null || i.getType() == Material.AIR)
			return "";

		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getString(path);
	}

	@Override
	public HashMap<String, Double> requestDoubleTags(ItemStack i, HashMap<String, Double> map) {
		if (i == null || i.getType() == Material.AIR)
			return map;

		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		for (String s : map.keySet())
			map.put(s, map.get(s) + compound.getDouble("MMOITEMS_" + s) + compound.getDouble("MMOITEMS_ADDITIONAL_" + s));

		return map;
	}

	@Override
	public double[] getBoundingBox(Entity p) {
		net.minecraft.server.v1_13_R2.Entity nmsEntity = ((CraftEntity) p).getHandle();
		AxisAlignedBB boundingBox = nmsEntity.getBoundingBox();
		return new double[] { boundingBox.a, boundingBox.b, boundingBox.c, boundingBox.d, boundingBox.e, boundingBox.f };
	}

	@Override
	public void damageEntity(Player p, LivingEntity t, double value) {
		((CraftLivingEntity) t).getHandle().damageEntity(DamageSource.playerAttack((EntityHuman) ((CraftPlayer) p).getHandle()), (float) value);
	}

	@Override
	public void sendJson(Player p, String msg) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(msg));
		PlayerConnection co = ((CraftPlayer) p).getHandle().playerConnection;
		co.sendPacket(packet);
	}

	@Override
	public void sendTitle(Player player, String msgTitle, String msgSubTitle, int fadeIn, int ticks, int fadeOut) {
		IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + msgTitle + "\"}");
		IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\": \"" + msgSubTitle + "\"}");

		PacketPlayOutTitle p = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle p2 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubTitle);
		PacketPlayOutTitle p3 = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, fadeIn, ticks, fadeOut);

		((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(p2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(p3);
	}

	@Override
	public void sendActionBar(Player player, String message) {
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
	}
}
