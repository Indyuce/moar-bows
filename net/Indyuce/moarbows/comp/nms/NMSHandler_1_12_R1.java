package net.Indyuce.moarbows.comp.nms;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_12_R1.PlayerConnection;

public class NMSHandler_1_12_R1 implements NMSHandler {
	@Override
	public ItemStack addTag(ItemStack i, ItemTag... tags) {
		net.minecraft.server.v1_12_R1.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
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
	public String getStringTag(ItemStack i, String path) {
		if (i == null || i.getType() == Material.AIR)
			return "";

		net.minecraft.server.v1_12_R1.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getString(path);
	}

	@Override
	public double[] getBoundingBox(Entity p) {
		net.minecraft.server.v1_12_R1.Entity nmsEntity = ((CraftEntity) p).getHandle();
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
}
