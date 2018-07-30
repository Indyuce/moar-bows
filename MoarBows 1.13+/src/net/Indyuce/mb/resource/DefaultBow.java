package net.Indyuce.mb.resource;

import net.Indyuce.mb.api.BowModifier;
import net.Indyuce.mb.api.SpecialBow;
import net.Indyuce.mb.util.Utils;

public enum DefaultBow {
	SILVER_BOW("Silver Bow", new String[] { "Arrows deal 40% additional damage." }, "crit", 0, new String[] { "IRON_INGOT,IRON_INGOT,IRON_INGOT", "IRON_INGOT,BOW,IRON_INGOT", "IRON_INGOT,IRON_INGOT,IRON_INGOT" }, new BowModifier("damage-percent", 40), new BowModifier("block-effect-iD", 12)),
	EXPLOSIVE_BOW("Explosive Bow", new String[] { "Arrows explode when landing, deal", "8 damage to nearby entities." }, "explosion_normal", 0, new String[] { "TNT,TNT,TNT", "TNT,BOW,TNT", "TNT,TNT,TNT" }, new BowModifier("damage", 8)),
	SHOCKING_BOW("Shocking Bow", new String[] { "Fires enchanted arrows", "that shock your targets." }, "smoke_normal", 0, new String[] { "FLINT,FLINT,FLINT", "FLINT,BOW,FLINT", "FLINT,FLINT,FLINT" }, new BowModifier("duration", 2)),
	CORROSIVE_BOW("Corrosive Bow", new String[] { "Arrows poison your targets and", "nearby entities for 6 seconds." }, "villager_happy", 0, new String[] { "AIR,SLIME_BALL,AIR", "SLIME_BALL,BOW,SLIME_BALL", "AIR,SLIME_BALL,AIR" }, new BowModifier("duration", 6)),
	HUNTER_BOW("Hunter Bow", new String[] { "Arrows deal 75% additional", "damage to friendly mobs." }, "redstone:255,0,0", 0, new String[] { "CHICKEN,BEEF,CHICKEN", "BEEF,BOW,BEEF", "CHICKEN,BEEF,CHICKEN" }, new BowModifier("damage-percent", 75)),
	MARKED_BOW("Marked Bow", new String[] { "Arrows mark players. Hitting a", "marked player deals 40% additional", "damage. Milk dispels the mark." }, "spell_witch", 0, new String[] { "COAL,COAL,COAL", "COAL,BOW,COAL", "COAL,COAL,COAL" }, new BowModifier("damage-percent", 40), new BowModifier("particles", true)),
	LIGHTNING_BOWLT("Lightning Bow'lt", new String[] { "Shoots arrows that summon", "lightning upon landing." }, "fireworks_spark", 0, new String[] { "AIR,BEACON,AIR", "AIR,BOW,AIR", "AIR,AIR,AIR" }),
	VOID_BOW("Void Bow", new String[] { "Its arrows teleport you", "to where they land." }, "redstone:128,0,128", 5, new String[] { "AIR,ENDER_PEARL,AIR", "ENDER_PEARL,BOW,ENDER_PEARL", "AIR,ENDER_PEARL,AIR" }),
	ICE_BOW("Ice Bow", new String[] { "Shoots ice arrows that cause an ice", "explosion upon landing, temporarily", "slowing every nearby entity." }, "snow_shovel", 0, new String[] { "ICE,ICE,ICE", "ICE,BOW,ICE", "ICE,ICE,ICE" }, new BowModifier("amplifier", 2), new BowModifier("duration", 5)),
	FIRE_BOW("Fire Bow", new String[] { "Shoots burning arrows that cause a", "first burst upon landing, igniting", "any entity within a few blocks." }, "flame", 0, new String[] { "BLAZE_ROD,BLAZE_ROD,BLAZE_ROD", "BLAZE_ROD,BOW,BLAZE_ROD", "BLAZE_ROD,BLAZE_ROD,BLAZE_ROD" }, new BowModifier("duration", 4), new BowModifier("max-burning-time", 8)),
	TRIPPPLE_BOW("Trippple Bow", new String[] { "Shoots 3 arrows at a time." }, "redstone:255,255,255", 2.5, new String[] { "AIR,AIR,AIR", "BOW,BOW,BOW", "AIR,AIR,AIR" }),
	COMPOSITE_BOW("Composite Bow", new String[] { "Fires enchanted arrows that", "follow a linear trajectory." }, "redstone:91,60,17", 2.5, new String[] { "AIR,AIR,AIR", "BOW,NETHER_STAR,BOW", "AIR,AIR,AIR" }, new BowModifier("damage", 8)),
	GRAVITY_BOW("Gravity Bow", new String[] { "Shoots arrows that attract", "your target to yourself." }, "spell_instant", 0, new String[] { "AIR,FISHING_ROD,AIR", "AIR,BOW,AIR", "AIR,AIR,AIR" }, new BowModifier("force", 2.5), new BowModifier("y-static", .3)),
	CUPIDONS_BOW("Cupidon's Bow", new String[] { "Arrows heal players for 3 hearts.", "Also unmarks (&nMarked Bow&7) players." }, "heart", 0, new String[] { "GLISTERING_MELON_SLICE,GLISTERING_MELON_SLICE,GLISTERING_MELON_SLICE", "GLISTERING_MELON_SLICE,BOW,GLISTERING_MELON_SLICE", "GLISTERING_MELON_SLICE,GLISTERING_MELON_SLICE,GLISTERING_MELON_SLICE" }, new BowModifier("heal", 4)),
	SPARTAN_BOW("Spartan Bow", new String[] { "Summons a flurry of arrows from", "the sky when hitting a target." }, "redstone:180,180,180", 25, new String[] { "BOW,EMERALD,BOW", "EMERALD,BOW,EMERALD", "BOW,EMERALD,BOW" }, new BowModifier("duration", 1.5)),
	LINEAR_BOW("Linear Bow", new String[] { "Fires instant linear arrows", "that deals 8 damage to", "the first entity it hits." }, "redstone:90,90,255", 0, new String[] { "FLINT,STICK,FLINT", "STICK,BOW,STICK", "FLINT,STICK,FLINT" }),
	RAILGUN_BOW("Railgun Bow", new String[] { "Only works in minecarts. Arrows", "explode upon landing, causing", "a powerful explosion." }, "villager_angry", 7.5, new String[] { "TNT,RAIL,TNT", "RAIL,BOW,RAIL", "TNT,RAIL,TNT" }, new BowModifier("radius", 5)),
	WITHER_BOW("Wither Bow", new String[] { "Shoots an exploding wither skull." }, "redstone:0,0,0", 4.0, new String[] { "WITHER_SKELETON_SKULL,WITHER_SKELETON_SKULL,WITHER_SKELETON_SKULL", "WITHER_SKELETON_SKULL,BOW,WITHER_SKELETON_SKULL", "WITHER_SKELETON_SKULL,WITHER_SKELETON_SKULL,WITHER_SKELETON_SKULL" }),
	SNOW_BOW("Snow Bow", new String[] { "Shoots a few snowballs.", "The number depends on", "the bow pull force." }, "snow_shovel", 2.0, new String[] { "SNOW_BLOCK,SNOW_BLOCK,SNOW_BLOCK", "SNOW_BLOCK,BOW,SNOW_BLOCK", "SNOW_BLOCK,SNOW_BLOCK,SNOW_BLOCK" }),
	AUTOBOW("Autobow", new String[] { "Shoots a flurry of arrows.", "The number depends on the", "bow pull force." }, "crit", 8.0, new String[] { "BOW,BOW,BOW", "BOW,NETHER_STAR,BOW", "BOW,BOW,BOW" }),
	CHICKEN_BOW("Chicken Bow", new String[] { "Shoots a few eggs. The number", "depends on the bow pull force." }, "redstone:240,230,140", 3.0, new String[] { "EGG,EGG,EGG", "EGG,BOW,EGG", "EGG,EGG,EGG" }),
	SHADOW_BOW("Shadow Bow", new String[] { "Shoots a long ranged linear", "cursed arrow that deals 8", "damage tothe first entity it hits." }, "redstone:128,0,128", 10.0, new String[] { "ENDER_EYE,ENDER_EYE,ENDER_EYE", "ENDER_EYE,BOW,ENDER_EYE", "ENDER_EYE,ENDER_EYE,ENDER_EYE" }, new BowModifier("damage", 8)),
	BLAZE_BOW("Blaze Bow", new String[] { "Shoots a long ranged firebolt that", "deals 8 damage to the first entity it", "hits, igniting him for 4 seconds." }, "flame", 10.0, new String[] { "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM", "MAGMA_CREAM,BOW,MAGMA_CREAM", "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM" }, new BowModifier("damage", 8), new BowModifier("duration", 4)),
	METEOR_BOW("Meteor Bow", new String[] { "Shoots arrows that summon a fire", "comet upon landing, dealing damage", "and knockback to nearby entities." }, "lava", 10.0, new String[] { "FIRE_CHARGE,FIRE_CHARGE,FIRE_CHARGE", "FIRE_CHARGE,BOW,FIRE_CHARGE", "FIRE_CHARGE,FIRE_CHARGE,FIRE_CHARGE" }, new BowModifier("damage", 8), new BowModifier("knockback", 1)),
	PULSAR_BOW("Pulsar Bow", new String[] { "Shoots arrows that summon a black", "hole that attracts nearby enemies." }, "smoke_normal", 10.0, new String[] { "AIR,WITHER_SKELETON_SKULL,AIR", "WITHER_SKELETON_SKULL,BOW,WITHER_SKELETON_SKULL", "AIR,WITHER_SKELETON_SKULL,AIR" }, new BowModifier("duration", 3)),
	EARTHQUAKE_BOW("Earthquake Bow", new String[] { "Summons a shockwave when hitting", "anything, powerfully knocking up", "all enemies within 5 blocks." }, "redstone:128,0,0", 10.0, new String[] { "DIRT,DIRT,DIRT", "DIRT,BOW,DIRT", "DIRT,DIRT,DIRT" }, new BowModifier("knockup", 1), new BowModifier("radius", 5)),;

	public String name;
	public SpecialBow interfaceClass;
	public String[] lore;
	public String eff;
	public double cd;
	public String[] craft;
	public BowModifier[] mods;

	private DefaultBow(String name, String[] lore, String eff, double cd, String[] craft, BowModifier... mods) {
		try {
			interfaceClass = (SpecialBow) (Class.forName("net.Indyuce.mb.resource.bow." + Utils.caseOnWords(name().toLowerCase().replace("_", " ")).replace(" ", "")).newInstance());
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		this.name = "&f" + name;
		this.lore = lore;
		this.eff = eff;
		this.craft = craft;
		this.cd = cd;
		this.mods = mods;
	}
}