package net.Indyuce.moarbows.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.modifier.BooleanModifier;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.modifier.Modifier;
import net.Indyuce.moarbows.api.modifier.StringModifier;
import net.Indyuce.moarbows.version.nms.ItemTag;
import net.Indyuce.moarbows.version.nms.NBTItem;

public abstract class MoarBow {
	private final String id;
	private final Map<String, Modifier> mods = new HashMap<>();

	private String name, formattedParticleData;
	private String[] lore, craft;
	private int data;
	private ParticleData particleData;

	protected static final Random random = new Random();

	public MoarBow(String[] lore, int durability,  String particleEffect, String[] craft) {
		this.id = getClass().getSimpleName().toUpperCase();
		this.name = "&f" + getClass().getSimpleName().replace("_", " ");

		this.lore = lore;
		this.data = (short) durability;
		this.formattedParticleData = particleEffect;
		this.particleData = new ParticleData(particleEffect);
		this.craft = craft;
	}

	public MoarBow(String id, String name, String[] lore, int durability,  String particleEffect, String[] craft) {
		this.id = id.toUpperCase().replace("-", "_");
		this.name = name;

		this.lore = lore;
		this.data = (short) durability;
		this.formattedParticleData = particleEffect;
		this.particleData = new ParticleData(particleEffect);
		this.craft = craft;
	}

	public abstract boolean canShoot(EntityShootBowEvent event, ArrowData data);

	public abstract void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target);

	public abstract void whenLand(ArrowData data);

	public String getId() {
		return id;
	}

	public String getLowerCaseId() {
		return id.toLowerCase().replace("_", "-");
	}

	public String getUncoloredName() {
		return name;
	}

	public String getName() {
		return ChatColor.GREEN + ChatColor.translateAlternateColorCodes('&', name);
	}

	public int getData() {
		return data;
	}

	public boolean hasData() {
		return data > 0;
	}

	public String[] getLore() {
		return lore == null ? new String[0] : lore;
	}

	public Collection<Modifier> getModifiers() {
		return mods.values();
	}

	public Set<String> modifierKeys() {
		return mods.keySet();
	}

	public void addModifier(Modifier... modifiers) {
		for (Modifier modifier : modifiers)
			mods.put(modifier.getPath(), modifier);
	}

	public Modifier getModifier(String path) {
		return mods.get(path);
	}

	public boolean hasModifier(String path) {
		return mods.containsKey(path);
	}

	public double getDouble(String path, int x) {
		return ((DoubleModifier) getModifier(path)).calculate(x);
	}

	public boolean getBoolean(String path) {
		return ((BooleanModifier) getModifier(path)).getValue();
	}

	public String getString(String path) {
		return ((StringModifier) getModifier(path)).getValue();
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
		data = (short) config.getInt(id + ".durability");
		
		
	}
	
	public ItemStack getItem() {
		return getItem(1);
	}

	public ItemStack getItem(int level) {
		level = Math.max(1, level);
		
		ItemStack item = hasData() ? MoarBows.plugin.getVersion().getTextureHandler().textureItem(Material.BOW, data) : new ItemStack(Material.BOW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());

		if (MoarBows.plugin.getConfig().getBoolean("bow-options.hide-unbreakable"))
			meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

		if (MoarBows.plugin.getConfig().getBoolean("bow-options.hide-enchants"))
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		if (this.lore != null) {
			List<String> lore = new ArrayList<>();
			for (String str : this.lore)
				lore.add(ChatColor.GRAY + applyPlaceholders(str, level));
			meta.setLore(lore);
		}
		item.setItemMeta(meta);

		// unbreakable?
		NBTItem nbt = NBTItem.get(item);
		if (MoarBows.plugin.getConfig().getBoolean("bow-options.unbreakable"))
			nbt.addTag(new ItemTag("Unbreakable", true)).toItem();

		return nbt.addTag(new ItemTag("MoarBowLevel", level), new ItemTag("MoarBow", getId())).toItem();
	}

	public String applyPlaceholders(String str, int x) {

		Modifier modifier;
		while (str.contains("{") && str.substring(str.indexOf("{")).contains("}")) {
			String holder = str.substring(str.indexOf("{") + 1, str.indexOf("}")).replace("_", "-");
			str = str.replace("{" + holder + "}", hasModifier(holder) && (modifier = getModifier(holder)) instanceof DoubleModifier ? ((DoubleModifier) modifier).getDisplay(x) : "PHE");
		}

		return ChatColor.translateAlternateColorCodes('&', str);
	}

	public double getPowerDamageMultiplier(ItemStack item) {
		return item == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) ? 1 : 1 + .25 * (item.getItemMeta().getEnchantLevel(Enchantment.ARROW_DAMAGE) + 1);
	}
}
