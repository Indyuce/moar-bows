package net.Indyuce.mb.api;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.Indyuce.mb.Main;
import net.Indyuce.mb.resource.ItemTag;

public class MoarBow {

	// main
	private String id;
	private SpecialBow bowClass;
	private BowModifier[] mods;

	// display
	private String name;
	private String[] lore;
	private short data;

	// bow values
	private String pe;
	private double cd;
	private String[] craft;

	public MoarBow(SpecialBow bowClass, String id, String name, String[] lore, int durability, double cooldown, String particleEffect, String[] craft, BowModifier[] mods) {
		this.id = id.toUpperCase().replace("-", "_");
		this.bowClass = bowClass;
		this.name = name;
		this.lore = lore;
		this.data = (short) durability;
		this.pe = particleEffect;
		this.cd = cooldown;
		this.craft = craft;
		this.mods = mods;
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public SpecialBow getBowClass() {
		return bowClass;
	}

	public short getDurability() {
		return data;
	}

	public String[] getLore() {
		return lore == null ? new String[0] : lore;
	}

	public BowModifier[] getModifiers() {
		return mods == null ? new BowModifier[0] : mods;
	}

	public double getCooldown() {
		return cd;
	}

	public String[] getFormattedCraftingRecipe() {
		return craft == null ? new String[0] : craft;
	}

	public String getParticleEffect() {
		return pe;
	}

	public void update(FileConfiguration config) {
		name = config.getString(id + ".name");
		lore = config.getStringList(id + ".lore").toArray(new String[0]);
		pe = config.getString(id + ".eff");
		craft = config.getStringList(id + ".craft").toArray(new String[0]);
		cd = config.getDouble(id + ".cooldown");
		data = (short) config.getInt(id + ".durability");
	}

	public ItemStack a() {
		ItemStack i = new ItemStack(Material.BOW, 1, (short) data);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		meta.addItemFlags(ItemFlag.values());
		if (this.lore != null) {
			ArrayList<String> lore = new ArrayList<String>();
			for (String s : this.lore)
				lore.add(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', s));
			meta.setLore(lore);
		}
		i.setItemMeta(meta);

		// unbreakable?
		if (Main.plugin.getConfig().getBoolean("unbreakable-bows"))
			i = Main.nbttags.add(i, new ItemTag("Unbreakable", true));

		return i;
	}

	public void register(boolean msg) {

		// check for register boolean
		if (!Main.canRegisterBows()) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "[MoarBows] Failed attempt to register " + id + "! Please register your bows when the plugin is loading.");
			return;
		}

		// register
		Main.map.put(id, this);
		if (msg)
			Bukkit.getConsoleSender().sendMessage("[MoarBows] Successfully registered " + ChatColor.GREEN + id + ChatColor.WHITE + "!");
	}

	public static MoarBow get(ItemStack i) {
		for (MoarBow b : Main.map.values())
			if (ChatColor.translateAlternateColorCodes('&', b.getName()).equals(i.getItemMeta().getDisplayName()))
				return b;

		String tag = Main.nbttags.getStringTag(i, "MMOITEMS_MOARBOWS_ID");
		if (tag.equals(""))
			return null;

		return Main.map.containsKey(tag) ? Main.map.get(tag) : null;
	}
}
