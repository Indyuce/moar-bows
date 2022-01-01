package net.Indyuce.moarbows.bow;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.api.ArrowData;
import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.modifier.BooleanModifier;
import net.Indyuce.moarbows.api.modifier.DoubleModifier;
import net.Indyuce.moarbows.api.particle.ParticleData;
import net.Indyuce.moarbows.api.util.LinearFormula;

public class Marked_Bow extends MoarBow implements Listener {
	private static final Map<Integer, Mark> marked = new HashMap<>();

	public Marked_Bow() {
		super(new String[] { "Arrows mark players. Hitting a", "marked player deals &c{extra}% &7additional", "damage. Milk dispels the mark." },
				new ParticleData(Particle.SPELL_WITCH), new String[] { "COAL,COAL,COAL", "COAL,BOW,COAL", "COAL,COAL,COAL" });

		addModifier(new DoubleModifier("cooldown", new LinearFormula(10, -1, 3, 10)), new DoubleModifier("extra", new LinearFormula(40, 20)),
				new DoubleModifier("duration", new LinearFormula(6, 1)), new BooleanModifier("particles", true));
	}

	public static boolean isMarked(Entity entity) {
		return marked.containsKey(entity.getEntityId());
	}

	public static Mark getMark(Entity entity) {
		return marked.get(entity.getEntityId());
	}

	@Override
	public boolean canShoot(EntityShootBowEvent event, ArrowData data) {
		return true;
	}

	@Override
	public void whenHit(EntityDamageByEntityEvent event, ArrowData data, Entity target) {

		if (isMarked(target))
			return;

		playEffect(target.getLocation());
		new Mark(target, data.getDouble("extra"), data.getDouble("duration"));
		target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_HURT, 2, 1.5f);
	}

	@Override
	public void whenLand(ArrowData data) {
		// TODO Auto-generated method stub

	}

	private void playEffect(Location loc) {
		new BukkitRunnable() {
			double y = 0;

			public void run() {
				for (int j1 = 0; j1 < 3; j1++) {
					y += .07;
					for (int j = 0; j < 3; j++)
						loc.getWorld().spawnParticle(Particle.REDSTONE, loc.clone().add(Math.cos(y * Math.PI + (j * Math.PI * 2 / 3)) * (3 - y) / 2.5,
								y, Math.sin(y * Math.PI + (j * Math.PI * 2 / 3)) * (3 - y) / 2.5), 0, new Particle.DustOptions(Color.BLACK, 1));
				}
				if (y > 3)
					cancel();
			}
		}.runTaskTimer(MoarBows.plugin, 0, 1);
	}

	public class Mark extends BukkitRunnable implements Listener {
		private final Entity entity;
		private final double coef;

		public Mark(Entity entity, double extra, double duration) {
			this.entity = entity;
			this.coef = 1 + extra / 100;

			marked.put(entity.getEntityId(), this);

			Bukkit.getPluginManager().registerEvents(this, MoarBows.plugin);
			Bukkit.getScheduler().runTaskLater(MoarBows.plugin, () -> close(), (long) (duration * 20));
			runTaskTimer(MoarBows.plugin, 0, 9);
		}

		@EventHandler
		public void a(EntityDamageByEntityEvent event) {
			if (event.getEntity().equals(entity)) {
				event.setDamage(event.getDamage() * coef);
				playEffect(entity.getLocation());
				entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ENDERMAN_DEATH, 2, 2);
			}
		}

		@EventHandler
		public void b(PlayerItemConsumeEvent event) {
			if (event.getPlayer().equals(entity)) {
				entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 2, 2);
				close();
			}
		}

		@EventHandler
		public void c(EntityDeathEvent event) {
			if (event.getEntity().equals(entity))
				close();
		}

		public void close() {
			marked.remove(entity.getEntityId());
			EntityDamageByEntityEvent.getHandlerList().unregister(this);
			PlayerItemConsumeEvent.getHandlerList().unregister(this);
			cancel();
		}

		@Override
		public void run() {
			for (double j = 0; j < Math.PI * 2; j += Math.PI / 18)
				entity.getWorld().spawnParticle(Particle.SMOKE_NORMAL, entity.getLocation().clone().add(Math.cos(j) * .7, .1, Math.sin(j) * .7), 0);
		}
	}
}
