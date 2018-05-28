package me.Indyuce.mb.resource;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Indyuce.mb.Main;
import me.Indyuce.mb.reflect.NBTTags;
import me.Indyuce.mb.util.Utils;

public enum Bow {
	SILVER_BOW("Silver Bow", new String[] { "Arrows deal 40% additional damage." }, "crit", 0, new String[] { "IRON_INGOT,IRON_INGOT,IRON_INGOT", "IRON_INGOT,BOW,IRON_INGOT", "IRON_INGOT,IRON_INGOT,IRON_INGOT" }, new BowMod("damagePercent", 40), new BowMod("blockEffectID", 12)),
	EXPLOSIVE_BOW("Explosive Bow", new String[] { "Arrows deal area of effect damage.", "Deals 8 damage when hitting the ground." }, "explosion_normal", 0, new String[] { "TNT,TNT,TNT", "TNT,BOW,TNT", "TNT,TNT,TNT" }, new BowMod("damage", 8)),
	SHOCKING_BOW("Shocking Bow", new String[] { "Fires enchanted arrows that shock your targets." }, "smoke_normal", 0, new String[] { "FLINT,FLINT,FLINT", "FLINT,BOW,FLINT", "FLINT,FLINT,FLINT" }, new BowMod("duration", 2)),
	CORROSIVE_BOW("Corrosive Bow", new String[] { "Arrows poisons your target and living", "beings around it for 6 seconds." }, "villager_happy", 0, new String[] { "AIR,SLIME_BALL,AIR", "SLIME_BALL,BOW,SLIME_BALL", "AIR,SLIME_BALL,AIR" }, new BowMod("duration", 6)),
	HUNTER_BOW("Hunter Bow", new String[] { "Arrows deal 75% additional", "damage to friendly mobs." }, "redstone", 0, new String[] { "RAW_CHICKEN,RAW_BEEF,RAW_CHICKEN", "RAW_BEEF,BOW,RAW_BEEF", "RAW_CHICKEN,RAW_BEEF,RAW_CHICKEN" }, new BowMod("damagePercent", 75)),
	MARKED_BOW("Marked Bow", new String[] { "Arrows mark players. Hitting a marked", "player deals 40% additional damage.", "Milk dispels the mark." }, "spell_witch", 0, new String[] { "COAL,COAL,COAL", "COAL,BOW,COAL", "COAL,COAL,COAL" }, new BowMod("damagePercent", 40), new BowMod("particles", true)),
	LIGHTNING_BOWLT("Lightning Bow'lt", new String[] { "Arrows summon a thunder bolt when", "hitting the ground, or a player." }, "fireworks_spark", 0, new String[] { "AIR,BEACON,AIR", "AIR,BOW,AIR", "AIR,AIR,AIR" }),
	VOID_BOW("Void Bow", new String[] { "Arrows teleport you to where they land." }, "redstone:128,0,128", 5, new String[] { "AIR,ENDER_PEARL,AIR", "ENDER_PEARL,BOW,ENDER_PEARL", "AIR,ENDER_PEARL,AIR" }),
	ICE_BOW("Ice Bow", new String[] { "Fires ice arrows that slows your target and", "living beings around it for 5 seconds." }, "snow_shovel", 0, new String[] { "ICE,ICE,ICE", "ICE,BOW,ICE", "ICE,ICE,ICE" }, new BowMod("amplifier", 2), new BowMod("duration", 5)),
	FIRE_BOW("Fire Bow", new String[] { "Fires burning arrows that ignites your", "target and every living being", "around it for 4 seconds." }, "flame", 0, new String[] { "BLAZE_ROD,BLAZE_ROD,BLAZE_ROD", "BLAZE_ROD,BOW,BLAZE_ROD", "BLAZE_ROD,BLAZE_ROD,BLAZE_ROD" }, new BowMod("duration", 4), new BowMod("maxBurningTime", 8)),
	TRIPPPLE_BOW("Trippple Bow", new String[] { "Fires 3 arrows at a time." }, "redstone:255,255,255", 2.5, new String[] { "AIR,AIR,AIR", "BOW,BOW,BOW", "AIR,AIR,AIR" }),
	COMPOSITE_BOW("Composite Bow", new String[] { "Fires enchanted bigger arrows", "that follow a linear path." }, "redstone:91,60,17", 2.5, new String[] { "AIR,AIR,AIR", "BOW,NETHER_STAR,BOW", "AIR,AIR,AIR" }, new BowMod("damage", 8)),
	GRAVITY_BOW("Gravity Bow", new String[] { "Fires arrows that attracts", "your target to yourself." }, "spell_instant", 0, new String[] { "AIR,FISHING_ROD,AIR", "AIR,BOW,AIR", "AIR,AIR,AIR" }),
	CUPIDONS_BOW("Cupidon's Bow", new String[] { "Arrows heal hit players for 3 hearts.", "Also unmarks (§nMarked Bow§7) players." }, "heart", 0, new String[] { "SPECKLED_MELON,SPECKLED_MELON,SPECKLED_MELON", "SPECKLED_MELON,BOW,SPECKLED_MELON", "SPECKLED_MELON,SPECKLED_MELON,SPECKLED_MELON" }, new BowMod("heal", 4)),
	SPARTAN_BOW("Spartan Bow", new String[] { "Summons a flurry of arrows from", "the sky when hitting a target." }, "redstone:180,180,180", 25, new String[] { "BOW,EMERALD,BOW", "EMERALD,BOW,EMERALD", "BOW,EMERALD,BOW" }, new BowMod("duration", 1.5)),
	LINEAR_BOW("Linear Bow", new String[] { "Fires instant linear arrows that deals", "8 damage to the first living being it hits." }, "redstone:90,90,255", 0, new String[] { "FLINT,STICK,FLINT", "STICK,BOW,STICK", "FLINT,STICK,FLINT" }),
	RAILGUN_BOW("Railgun Bow", new String[] { "Only works in minecarts.", "Arrows explode on ground or when hitting", "a living being, causing an explosion." }, "villager_angry", 7.5, new String[] { "TNT,RAILS,TNT", "RAILS,BOW,RAILS", "TNT,RAILS,TNT" }, new BowMod("radius", 5)),
	FLARE_BOW("Flare Bow", new String[] { "Shoots an arrow that explodes in a beautiful firework." }, "redstone:0,255,0", 3.0, new String[] { "FIREWORK,FIREWORK,FIREWORK", "FIREWORK,BOW,FIREWORK", "FIREWORK,FIREWORK,FIREWORK" }, new BowMod("multicolor", false)),
	WITHER_BOW("Wither Bow", new String[] { "Shoots an exploding wither skull." }, "redstone:0,0,0", 4.0, new String[] { "SKULL_ITEM:1,SKULL_ITEM:1,SKULL_ITEM:1", "SKULL_ITEM:1,BOW,SKULL_ITEM:1", "SKULL_ITEM:1,SKULL_ITEM:1,SKULL_ITEM:1" }),
	SNOW_BOW("Snow Bow", new String[] { "Shoots a few snowballs.", "The number depends on the bow pull force." }, "snow_shovel", 2.0, new String[] { "SNOW_BLOCK,SNOW_BLOCK,SNOW_BLOCK", "SNOW_BLOCK,BOW,SNOW_BLOCK", "SNOW_BLOCK,SNOW_BLOCK,SNOW_BLOCK" }),
	AUTOBOW("Autobow", new String[] { "Shoots a flurry of arrows.", "The number depends on the bow pull force." }, "crit", 8.0, new String[] { "BOW,BOW,BOW", "BOW,NETHER_STAR,BOW", "BOW,BOW,BOW" }),
	CHICKEN_BOW("Chicken Bow", new String[] { "Shoots a few eggs.", "The number depends on the bow pull force." }, "redstone:240,230,140", 3.0, new String[] { "EGG,EGG,EGG", "EGG,BOW,EGG", "EGG,EGG,EGG" }),
	SHADOW_BOW("Shadow Bow", new String[] { "Shoots a long ranged, linear void shadow arrow.", "that deals 8 damage to the first entity it hits." }, "redstone:128,0,128", 10.0, new String[] { "EYE_OF_ENDER,EYE_OF_ENDER,EYE_OF_ENDER", "EYE_OF_ENDER,BOW,EYE_OF_ENDER", "EYE_OF_ENDER,EYE_OF_ENDER,EYE_OF_ENDER" }, new BowMod("damage", 8)),
	BLAZE_BOW("Blaze Bow", new String[] { "Shoots a long ranged firebolt that", "deals 8 damage to the first entity it", "hits, igniting him for 4 seconds." }, "flame", 10.0, new String[] { "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM", "MAGMA_CREAM,BOW,MAGMA_CREAM", "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM" }, new BowMod("damage", 8), new BowMod("duration", 4)),
	METEOR_BOW("Meteor Bow", new String[] { "Shoots a special arrow. When it lands,", "it summons a magic fire meteor that", "deals 8 damage to enemies it hits,", "pushing them powerfuly back." }, "lava", 10.0, new String[] { "FIREBALL,FIREBALL,FIREBALL", "FIREBALL,BOW,FIREBALL", "FIREBALL,FIREBALL,FIREBALL" }, new BowMod("damage", 8), new BowMod("knockback", 1)),
	PULSAR_BOW("Pulsar Bow", new String[] { "Shoots an arrow that explodes when", "hitting something, powerfully attracting", "enemies within 5 blocks." }, "smoke_normal", 10.0, new String[] { "AIR,SKULL_ITEM:1,AIR", "SKULL_ITEM:1,BOW,SKULL_ITEM:1", "AIR,SKULL_ITEM:1,AIR" }),;

	public String name;
	public SpecialBow interfaceClass;
	public String[] lore;
	public short durability;
	public String eff;
	public double cd;
	public String[] craft;
	public BowMod[] mods;

	private Bow(String name, String[] lore, String eff, double cd, String[] craft, BowMod... mods) {
		try {
			interfaceClass = (SpecialBow) (Class.forName("me.Indyuce.mb.resource.bow." + Utils.caseOnWords(name().toLowerCase().replace("_", " ")).replace(" ", "")).newInstance());
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		this.name = Utils.updateString("&f" + name, name() + ".name");
		this.lore = Utils.updateList(lore, name() + ".lore");
		this.eff = Utils.updateString(eff, name() + ".eff");
		this.craft = Utils.updateList(craft, name() + ".craft");
		this.cd = Utils.updateDouble(cd, name() + ".cooldown");
		this.durability = (short) Utils.updateDouble(0, name() + ".durability");
		this.mods = mods;
	}

	public ItemStack a() {
		ItemStack i = new ItemStack(Material.BOW, 1, (short) durability);
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

		// unbreakable
		if (Main.plugin.getConfig().getBoolean("unbreakable-bows"))
			i = NBTTags.add(i, new ItemTag("Unbreakable", true));

		return i;
	}

	public void initializeConfig(FileConfiguration config) {
		if (!config.contains(name()))
			config.createSection(name());

		String[] paths = new String[] { "name", "lore", "cooldown", "durability", "craft-enabled", "craft", "effect" };
		Object[] values = new Object[] { name, lore, cd, 0, true, craft, eff };
		ConfigurationSection section = config.getConfigurationSection(name());
		for (int j = 0; j < paths.length; j++)
			if (!section.contains(paths[j]))
				config.set(name() + "." + paths[j], values[j]);

		for (BowMod mod : mods)
			if (!section.contains(mod.path))
				config.set(name() + "." + mod.path, mod.value);
	}
}