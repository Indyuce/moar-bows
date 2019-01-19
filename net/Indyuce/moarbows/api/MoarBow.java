package net.Indyuce.moarbows.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.version.nms.ItemTag;

public class MoarBow {
	private String id, name, formattedParticleData;
	private List<BowModifier> mods = new ArrayList<>();
	private String[] lore, craft;
	private short data;
	private ParticleData particleData;
	private double cooldown;

	public MoarBow(String[] lore, int durability, double cooldown, String particleEffect, String[] craft) {
		this("", "", lore, durability, cooldown, particleEffect, craft);

		this.id = getClass().getSimpleName().toUpperCase();
		this.name = "&f" + getClass().getSimpleName().replace("_", " ");
	}

	public MoarBow(String id, String name, String[] lore, int durability, double cooldown, String particleEffect, String[] craft) {
		this.id = id.toUpperCase().replace("-", "_");
		this.name = name;
		this.lore = lore;
		this.data = (short) durability;
		this.formattedParticleData = particleEffect;
		this.particleData = new ParticleData(particleEffect);
		this.cooldown = cooldown;
		this.craft = craft;
	}

	// true = arrow effects
	// false = no arrow effects, arrow is NOT removed systematically
	public boolean shoot(EntityShootBowEvent event, Arrow arrow, Player player, ItemStack item) {
		return true;
	}

	public void hit(EntityDamageByEntityEvent event, Arrow arrow, Entity player, Player target) {
	}

	public void land(Player player, Arrow arrow) {
	}

	public String getID() {
		return id;
	}

	public String getLowerCaseID() {
		return id.toLowerCase().replace("_", "-");
	}

	public String getUncoloredName() {
		return name;
	}

	public String getName() {
		return ChatColor.translateAlternateColorCodes('&', name);
	}

	public short getDurability() {
		return data;
	}

	public String[] getLore() {
		return lore == null ? new String[0] : lore;
	}

	public List<BowModifier> getModifiers() {
		return mods == null ? new ArrayList<BowModifier>() : mods;
	}

	public void addModifier(BowModifier... modifiers) {
		for (BowModifier modifier : modifiers)
			mods.add(modifier);
	}

	public double getCooldown() {
		return cooldown;
	}

	public double getValue(String path) {
		return MoarBows.getLanguage().getDoubleValue(getID() + "." + path);
	}

	public boolean getBooleanValue(String path) {
		return MoarBows.getLanguage().getBooleanValue(getID() + "." + path);
	}

	public String getStringValue(String path) {
		return MoarBows.getLanguage().getStringValue(getID() + "." + path);
	}

	public String[] getFormattedCraftingRecipe() {
		return craft == null ? new String[0] : craft;
	}

	public String getFormattedParticleData() {
		return formattedParticleData;
	}

	public ParticleData createParticleData() {
		return particleData.clone();
	}

	public void update(FileConfiguration config) {
		name = config.getString(id + ".name");
		lore = config.getStringList(id + ".lore").toArray(new String[0]);
		particleData = new ParticleData(config.getString(id + ".eff"));
		craft = config.getStringList(id + ".craft").toArray(new String[0]);
		cooldown = config.getDouble(id + ".cooldown");
		data = (short) config.getInt(id + ".durability");
	}

	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.BOW, 1, (short) data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());

		if (MoarBows.plugin.getConfig().getBoolean("bow-options.hide-unbreakable"))
			meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		if (MoarBows.plugin.getConfig().getBoolean("bow-options.hide-enchants"))
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		if (this.lore != null) {
			ArrayList<String> lore = new ArrayList<String>();
			for (String s : this.lore)
				lore.add(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', s));
			meta.setLore(lore);
		}
		item.setItemMeta(meta);

		// unbreakable?
		if (MoarBows.plugin.getConfig().getBoolean("bow-options.unbreakable"))
			item = MoarBows.getNMS().addTag(item, new ItemTag("Unbreakable", true));

		return item;
	}

	public double getPowerDamageMultiplier(ItemStack item) {
		return item == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) ? 1 : 1 + .25 * (item.getItemMeta().getEnchantLevel(Enchantment.ARROW_DAMAGE) + 1);
	}
}
