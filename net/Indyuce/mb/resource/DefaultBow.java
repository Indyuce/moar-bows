package net.Indyuce.mb.resource;

import net.Indyuce.mb.api.MoarBow;

public enum DefaultBow {
	SPARTAN_BOW("Spartan Bow", new String[] { "Summons a flurry of arrows from", "the sky when hitting a target." }, "redstone:180,180,180", 25, new String[] { "BOW,EMERALD,BOW", "EMERALD,BOW,EMERALD", "BOW,EMERALD,BOW" }, new BowModifier("duration", 1.5)),
	LINEAR_BOW("Linear Bow", new String[] { "Fires instant linear arrows", "that deals 8 damage to", "the first entity it hits." }, "redstone:90,90,255", 0, new String[] { "FLINT,STICK,FLINT", "STICK,BOW,STICK", "FLINT,STICK,FLINT" }),
	RAILGUN_BOW("Railgun Bow", new String[] { "Only works in minecarts. Arrows ", "explode upon landing, causing", "a powerful explosion." }, "villager_angry", 7.5, new String[] { "TNT,RAILS,TNT", "RAILS,BOW,RAILS", "TNT,RAILS,TNT" }, new BowModifier("radius", 5)),
	FLARE_BOW("Flare Bow", new String[] { "Shoots arrows that explode into", "a beautiful firework show." }, "redstone:0,255,0", 3.0, new String[] { "FIREWORK,FIREWORK,FIREWORK", "FIREWORK,BOW,FIREWORK", "FIREWORK,FIREWORK,FIREWORK" }, new BowModifier("multicolor", false)),
	WITHER_BOW("Wither Bow", new String[] { "Shoots an exploding wither skull." }, "redstone:0,0,0", 4.0, new String[] { "SKULL_ITEM:1,SKULL_ITEM:1,SKULL_ITEM:1", "SKULL_ITEM:1,BOW,SKULL_ITEM:1", "SKULL_ITEM:1,SKULL_ITEM:1,SKULL_ITEM:1" }),
//	SNOW_BOW("Snow Bow", new String[] { "Shoots a few snowballs.", "The number depends on", "the bow pull force." }, "snow_shovel", 2.0, new String[] { "SNOW_BLOCK,SNOW_BLOCK,SNOW_BLOCK", "SNOW_BLOCK,BOW,SNOW_BLOCK", "SNOW_BLOCK,SNOW_BLOCK,SNOW_BLOCK" }),
//	AUTOBOW("Autobow", new String[] { "Shoots a flurry of arrows.", "The number depends on the", "bow pull force." }, "crit", 8.0, new String[] { "BOW,BOW,BOW", "BOW,NETHER_STAR,BOW", "BOW,BOW,BOW" }),
//	CHICKEN_BOW("Chicken Bow", new String[] { "Shoots a few eggs. The number", "depends on the bow pull force." }, "redstone:240,230,140", 3.0, new String[] { "EGG,EGG,EGG", "EGG,BOW,EGG", "EGG,EGG,EGG" }),
//	SHADOW_BOW("Shadow Bow", new String[] { "Shoots a long ranged linear", "cursed arrow that deals 8", "damage tothe first entity it hits." }, "redstone:128,0,128", 10.0, new String[] { "EYE_OF_ENDER,EYE_OF_ENDER,EYE_OF_ENDER", "EYE_OF_ENDER,BOW,EYE_OF_ENDER", "EYE_OF_ENDER,EYE_OF_ENDER,EYE_OF_ENDER" }, new BowModifier("damage", 8)),
//	BLAZE_BOW("Blaze Bow", new String[] { "Shoots a long ranged firebolt that", "deals 8 damage to the first entity it", "hits, igniting him for 4 seconds." }, "flame", 10.0, new String[] { "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM", "MAGMA_CREAM,BOW,MAGMA_CREAM", "MAGMA_CREAM,MAGMA_CREAM,MAGMA_CREAM" }, new BowModifier("damage", 8), new BowModifier("duration", 4)),
//	METEOR_BOW("Meteor Bow", new String[] { "Shoots arrows that summon a fire", "comet upon landing, dealing damage", "and knockback to nearby entities." }, "lava", 10.0, new String[] { "FIREBALL,FIREBALL,FIREBALL", "FIREBALL,BOW,FIREBALL", "FIREBALL,FIREBALL,FIREBALL" }, new BowModifier("damage", 8), new BowModifier("knockback", 1)),
//	PULSAR_BOW("Pulsar Bow", new String[] { "Shoots arrows that summon a black", "hole that attracts nearby enemies." }, "smoke_normal", 10.0, new String[] { "AIR,SKULL_ITEM:1,AIR", "SKULL_ITEM:1,BOW,SKULL_ITEM:1", "AIR,SKULL_ITEM:1,AIR" }, new BowModifier("duration", 3)),
//	EARTHQUAKE_BOW("Earthquake Bow", new String[] { "Summons a shockwave when hitting", "anything, powerfully knocking up", "all enemies within 5 blocks." }, "redstone:128,0,0", 10.0, new String[] { "DIRT,DIRT,DIRT", "DIRT,BOW,DIRT", "DIRT,DIRT,DIRT" }, new BowModifier("knockup", 1), new BowModifier("radius", 5)),;
;
	
	private DefaultBow(MoarBow bow) {
		bow.register(false);
	}
}