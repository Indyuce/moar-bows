package net.Indyuce.moarbows.api.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.MoarBows;

public class ParticleData {
	private final Particle particle;
	private final Color color;

	private static int amount = MoarBows.plugin.getConfig().getInt("hand-particles.amount");

	public ParticleData(String formatted) {
		String[] split = formatted.split("\\:");
		particle = Particle.valueOf(split[0].toUpperCase().replace("-", "_"));
		color = split.length > 1
				? Color.fromRGB(Integer.parseInt((split = split[1].split("\\,"))[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]))
				: null;
	}

	public ParticleData(Particle particle) {
		this(particle, null);
	}

	public ParticleData(Particle particle, Color color) {
		this.particle = particle;
		this.color = color;
	}

	public boolean isColorable() {
		return particle == Particle.SPELL_MOB || particle == Particle.SPELL_MOB_AMBIENT || particle == Particle.REDSTONE || particle == Particle.NOTE;
	}

	public ParticleRunnable newRunnable(Player player, boolean offhand) {
		return new ParticleRunnable(player, offhand);
	}

	public void displayParticle(Location loc) {
		if (isColorable() && color != null)
			loc.getWorld().spawnParticle(particle, loc, 0, new Particle.DustOptions(color, 1));
		else
			loc.getWorld().spawnParticle(particle, loc, 0);
	}

	@Override
	public String toString() {
		return particle.name().toLowerCase() + (color != null ? ":" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() : "");
	}

	public class ParticleRunnable extends BukkitRunnable {
		private final Player player;
		private final boolean offhand;

		public ParticleRunnable(Player player, boolean offhand) {
			this.player = player;
			this.offhand = offhand;
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
}
