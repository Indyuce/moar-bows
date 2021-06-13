package net.Indyuce.moarbows.version.wrapper;

import net.Indyuce.moarbows.MoarBows;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class VersionWrapper_Reflection extends VersionWrapper {
	public VersionWrapper_Reflection() {
		super("CustomModelData");
	}

	@Override
	public void sendJson(Player player, String message) {
		try {
			Class<?> icbc = nm("network.chat.IChatBaseComponent");
			Object chatMsg = icbc.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, message);
			Object titlePacket = nm("network.protocol.game.PacketPlayOutChat").getConstructor(icbc, nm("network.chat.ChatMessageType"), UUID.class)
					.newInstance(chatMsg, nm("network.chat.ChatMessageType").getDeclaredField("a").get(null), UUID.randomUUID());
			sendPacket(player, titlePacket);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException
				| NoSuchMethodException | ClassNotFoundException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	private void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object connection = handle.getClass().getDeclaredField("b").get(handle);
			connection.getClass().getMethod("sendPacket", nm("network.protocol.Packet")).invoke(connection, packet);
		} catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
				| InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	private Class<?> nm(String str) throws ClassNotFoundException {
		return Class.forName("net.minecraft." + MoarBows.plugin.getVersion().toString() + "." + str);
	}

	private Class<?> obc(String str) throws ClassNotFoundException {
		return Class.forName("org.bukkit.craftbukkit." + MoarBows.plugin.getVersion().toString() + "." + str);
	}

	@Override
	public NBTItem getNBTItem(ItemStack item) {
		try {
			return new NBTItem_Reflection(item);
		} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public class NBTItem_Reflection extends NBTItem {
		private final net.minecraft.world.item.ItemStack nms;
		private final NBTTagCompound compound;

		public NBTItem_Reflection(ItemStack item) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
			super(item);

			nms = (net.minecraft.world.item.ItemStack) obc("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(item);
			compound = nms.hasTag() ? nms.getTag() : new NBTTagCompound();
		}

		@Override
		public String getString(String path) {
			return compound.getString(path);
		}

		@Override
		public boolean hasTag(String path) {
			return compound.hasKey(path);
		}

		@Override
		public boolean getBoolean(String path) {
			return compound.getBoolean(path);
		}

		@Override
		public double getDouble(String path) {
			return compound.getDouble(path);
		}

		@Override
		public int getInteger(String path) {
			return compound.getInt(path);
		}

		@Override
		public NBTItem addTag(List<ItemTag> tags) {
			tags.forEach(tag -> {
				if (tag.getValue() instanceof Boolean)
					compound.setBoolean(tag.getPath(), (boolean) tag.getValue());
				else if (tag.getValue() instanceof Double)
					compound.setDouble(tag.getPath(), (double) tag.getValue());
				else if (tag.getValue() instanceof String)
					compound.setString(tag.getPath(), (String) tag.getValue());
				else if (tag.getValue() instanceof Integer)
					compound.setInt(tag.getPath(), (int) tag.getValue());
			});
			return this;
		}

		@Override
		public NBTItem removeTag(String... paths) {
			for (String path : paths)
				compound.remove(path);
			return this;
		}

		@Override
		public Set<String> getTags() {
			return compound.getKeys();
		}

		@Override
		public org.bukkit.inventory.ItemStack toItem() {
			nms.setTag(compound);
			return CraftItemStack.asBukkitCopy(nms);
		}
	}
}
