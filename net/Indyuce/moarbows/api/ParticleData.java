package net.Indyuce.moarbows.api;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;
import net.Indyuce.moarbows.ParticleEffect;
import net.Indyuce.moarbows.ParticleEffect.OrdinaryColor;

public class ParticleData extends BukkitRunnable {
	private ParticleEffect particle;
	private OrdinaryColor color = null;
	private Player player;
	private boolean offhand;

	private Random random = new Random();

	private static int amount = MoarBows.plugin.getConfig().getInt("hand-particles.amount");

	public ParticleData(String formatted) {
		String[] split = formatted.split("\\:");
		particle = ParticleEffect.valueOf(split[0].toUpperCase().replace("-", "_"));
		if (split.length > 1) {
			String[] rgb = split[1].split("\\,");
			color = new ParticleEffect.OrdinaryColor(Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
		}
	}

	public ParticleData(ParticleEffect particle, OrdinaryColor color) {
		this.particle = particle;
		this.color = color;
	}

	public boolean isColorable() {
		return particle == ParticleEffect.SPELL_MOB || particle == ParticleEffect.SPELL_MOB_AMBIENT || particle == ParticleEffect.REDSTONE || particle == ParticleEffect.NOTE;
	}

	public ParticleData clone() {
		return new ParticleData(particle, color);
	}

	public ParticleData setPlayer(Player player) {
		this.player = player;
		return this;
	}

	public ParticleData setOffhand(boolean offhand) {
		this.offhand = offhand;
		return this;
	}

	public void displayParticle(Location loc) {
		if (isColorable() && color != null)
			particle.display(color, loc, 100);
		else
			particle.display(0, 0, 0, 0, 1, loc, 100);
	}

	@Override
	public void run() {
		Location loc = player.getLocation().clone().add(0, .8, 0);
		loc.setYaw(player.getLocation().getYaw());
		loc.add(loc.getDirection().multiply(.2));
		loc.setPitch(0);
		loc.setYaw(player.getLocation().getYaw() + (offhand ? -90 : 90));
		loc.add(loc.getDirection().multiply(.3));

		if (isColorable() && color != null) {
			for (int j = 0; j < amount; j++)
				particle.display(color, loc.clone().add(random(), random(), random()), 100);
			return;
		}

		particle.display(.1f, .1f, .1f, 0, amount, loc, 100);
	}

	// generates a random offset
	private double random() {
		return random.nextDouble() / 2.5 - .2;
	}
}
