package net.Indyuce.moarbows.bow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.ParticleEffect;
import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.BowModifier;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.version.VersionSound;

public class Marked_Bow extends MoarBow implements Listener {
	public Marked_Bow() {
		super(new String[] { "Arrows mark players. Hitting a", "marked player deals 40% additional", "damage. Milk dispels the mark." }, 0, 0, "spell_witch", new String[] { "COAL,COAL,COAL", "COAL,BOW,COAL", "COAL,COAL,COAL" });

		addModifier(new BowModifier("damage-percent", 40), new BowModifier("particles", true));
	}

	public static HashMap<UUID, Long> marked = new HashMap<UUID, Long>();

	@Override
	public void hit(EntityDamageByEntityEvent e, Arrow a, Entity p, Player t) {
		if (p.getType() != EntityType.PLAYER)
			return;

		effect(p.getLocation());
		p.getWorld().playSound(p.getLocation(), VersionSound.ENTITY_ENDERMEN_HURT.getSound(), 2, 1.5f);
		if (marked.containsKey(p.getUniqueId()))
			return;

		marked.put(p.getUniqueId(), System.currentTimeMillis());
		new BukkitRunnable() {
			public void run() {
				if (marked.get(p.getUniqueId()) + 10000 < System.currentTimeMillis())
					cancel();

				for (double j = 0; j < Math.PI * 2; j += Math.PI / 18)
					ParticleEffect.SMOKE_NORMAL.display(0, 0, 0, 0, 1, p.getLocation().clone().add(Math.cos(j) * .7, .1, Math.sin(j) * .7), 100);
			}
		}.runTaskTimer(MoarBows.plugin, 0, 20);
	}

	@EventHandler
	public void a(EntityDamageEvent e) {
		if (e.getEntity().getType() != EntityType.PLAYER)
			return;
		if (!isEntityAttacking(e))
			return;
		Player p = (Player) e.getEntity();
		if (marked.containsKey(p.getUniqueId())) {
			double per = 1 + (MoarBows.getLanguage().getBows().getDouble("MARKED_BOW.damage-percent") / 100);
			e.setDamage(e.getDamage() * per);
			effect(p.getLocation());
			marked.remove(p.getUniqueId());
			p.getWorld().playSound(p.getLocation(), VersionSound.ENTITY_ENDERMEN_DEATH.getSound(), 2, 2);
		}
	}

	@EventHandler
	public void b(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		if (i.getType() == Material.MILK_BUCKET && marked.containsKey(p.getUniqueId())) {
			marked.remove(p.getUniqueId());
			p.getWorld().playSound(p.getLocation(), VersionSound.ENTITY_ENDERMEN_DEATH.getSound(), 2, 2);
		}
	}

	public boolean isEntityAttacking(EntityDamageEvent e) {
		if (Arrays.asList(new DamageCause[] { DamageCause.ENTITY_ATTACK, DamageCause.ENTITY_EXPLOSION }).contains(e.getCause()))
			return true;
		return false;
	}

	private void effect(Location loc) {
		if (!MoarBows.getLanguage().getBows().getBoolean("MARKED_BOW.particles"))
			return;

		new BukkitRunnable() {
			double y = 0;

			public void run() {
				for (int j1 = 0; j1 < 3; j1++) {
					y += .07;
					for (int j = 0; j < 3; j++)
						ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(Color.BLACK), loc.clone().add(Math.cos(y * Math.PI + (j * Math.PI * 2 / 3)) * (3 - y) / 2.5, y, Math.sin(y * Math.PI + (j * Math.PI * 2 / 3)) * (3 - y) / 2.5), 100);
				}
				if (y > 3)
					cancel();
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
	}
}
