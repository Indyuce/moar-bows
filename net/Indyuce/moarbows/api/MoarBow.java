package net.Indyuce.moarbows.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.Indyuce.moarbows.BowUtils;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.version.nms.ItemTag;

public class MoarBow {

	// main
	private String id;
	private List<BowModifier> mods = new ArrayList<BowModifier>();

	// display
	private String name;
	private String[] lore;
	private short data;

	// bow values
	private String pe;
	private double cooldown;
	private String[] craft;

	// bow cooldowns are stored here
	public static Map<UUID, Map<String, Long>> bowCooldown = new HashMap<UUID, Map<String, Long>>();

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
		this.pe = particleEffect;
		this.cooldown = cooldown;
		this.craft = craft;
	}

	// true = arrow effects
	// false = no arrow effects, arrow is NOT removed systematically
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		return true;
	}

	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
	}

	public void land(Player p, Arrow a) {
	}

	public String getID() {
		return id;
	}

	public String getLowerCaseID() {
		return id.toLowerCase().replace("_", "-");
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
		cooldown = config.getDouble(id + ".cooldown");
		data = (short) config.getInt(id + ".durability");
	}

	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.BOW, 1, (short) data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());
		meta.addItemFlags(ItemFlag.values());
		if (this.lore != null) {
			ArrayList<String> lore = new ArrayList<String>();
			for (String s : this.lore)
				lore.add(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', s));
			meta.setLore(lore);
		}
		item.setItemMeta(meta);

		// unbreakable?
		if (MoarBows.plugin.getConfig().getBoolean("unbreakable-bows"))
			item = MoarBows.getNMS().addTag(item, new ItemTag("Unbreakable", true));

		return item;
	}

	public boolean canUse(Player p, EntityShootBowEvent e) {
		if (getCooldown() <= 0)
			return true;

		Map<String, Long> cd = bowCooldown.containsKey(p.getUniqueId()) ? bowCooldown.get(p.getUniqueId()) : new HashMap<String, Long>();
		Long last = cd.containsKey(id) ? cd.get(id) : 0;
		double remaining = last + cooldown * 1000 - System.currentTimeMillis();

		if (remaining > 0) {
			e.setCancelled(true);
			p.sendMessage(Message.ON_COOLDOWN.translate().replace("%left%", "" + BowUtils.truncation(remaining / 1000, 1)));
			return false;
		}
		
		cd.put(id, System.currentTimeMillis());
		bowCooldown.put(p.getUniqueId(), cd);
		return true;
	}

	public void register() {
		MoarBows.registerBow(this);
		MoarBows.plugin.getLogger().log(Level.CONFIG, "Successfully register " + id + ".");
	}

	public static MoarBow get(ItemStack i) {
		for (MoarBow bow : MoarBows.getBows())
			if (bow.getName().equals(i.getItemMeta().getDisplayName()))
				return bow;

		String tag = MoarBows.getNMS().getStringTag(i, "MMOITEMS_MOARBOWS_ID");
		if (tag.equals(""))
			return null;

		return MoarBows.hasBow(tag) ? MoarBows.getBow(tag) : null;
	}

	public static MoarBow getFromDisplayName(ItemStack i) {
		for (MoarBow bow : MoarBows.getBows())
			if (bow.getName().equals(i.getItemMeta().getDisplayName()))
				return bow;
		return null;
	}
}
