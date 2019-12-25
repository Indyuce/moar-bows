package net.Indyuce.moarbows.api;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;

public class ParticleData extends BukkitRunnable {
	private final Particle particle;
	
	private Color color = null;
	private Player player;
	private boolean offhand;

	private static int amount = MoarBows.plugin.getConfig().getInt("hand-particles.amount");

	public ParticleData(String formatted) {
		String[] split = formatted.split("\\:");
		particle = Particle.valueOf(split[0].toUpperCase().replace("-", "_"));
		if (split.length > 1) {
			String[] rgb = split[1].split("\\,");
			color = Color.fromRGB(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
		}
	}

	public ParticleData(Particle particle, Color color) {
		this.particle = particle;
		this.color = color;
	}

	public boolean isColorable() {
		return particle == Particle.SPELL_MOB || particle == Particle.SPELL_MOB_AMBIENT || particle == Particle.REDSTONE || particle == Particle.NOTE;
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

	/*
	 * (public) used to display arrow particles
	 */
	public void displayParticle(Location loc) {
		if (isColorable() && color != null)
			loc.getWorld().spawnParticle(particle, loc, 0, new Particle.DustOptions(color, 1));
		else
			loc.getWorld().spawnParticle(particle, loc, 0);
	}

	@Override
	public void run() {
		Location loc = player.getLocation().clone().add(0, .8, 0);
		loc.setYaw(player.getLocation().getYaw());
		loc.add(loc.getDirection().multiply(.2));
		loc.setPitch(0);
		loc.setYaw(player.getLocation().getYaw() + (offhand ? -90 : 90));
		loc.add(loc.getDirection().multiply(.3));

		if (isColorable() && color != null)
			loc.getWorld().spawnParticle(particle, loc, amount, .1, .1, .1, 0, new Particle.DustOptions(color, 1));
		else
			loc.getWorld().spawnParticle(particle, loc, amount, .1, .1, .1, 0);
	}
}
