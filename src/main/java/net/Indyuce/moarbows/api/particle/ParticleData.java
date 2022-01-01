package net.Indyuce.moarbows.api.particle;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleData {
	private final Particle particle;
	private final Color color;
	private final int amount;

	public ParticleData(ConfigurationSection config) {
		Validate.notNull(config, "Could not read config");

		Validate.isTrue(config.contains("particle"), "Could not read particle");
		particle = Particle.valueOf(config.getString("particle"));

		color = config.contains("color") ? Color.fromRGB(config.getInt("color.red"), config.getInt("color.green"), config.getInt("color.blue"))
				: null;
		amount = config.getInt("amount", 2);
	}

	public ParticleData(Particle particle) {
		this(particle, null, 2);
	}

	public ParticleData(Particle particle, Color color, int amount) {
		this.particle = particle;
		this.color = color;
		this.amount = amount;
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

	public void setup(ConfigurationSection config) {
		config.set("particle", particle.name());
		config.set("amount", amount);
		if (color != null) {
			config.set("color.red", color.getRed());
			config.set("color.green", color.getGreen());
			config.set("color.blue", color.getBlue());
		}
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
