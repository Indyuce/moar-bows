package net.Indyuce.moarbows.api.runnable;

import org.bukkit.entity.Arrow;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.api.MoarBow;
import net.Indyuce.moarbows.api.ParticleData;

public class ArrowParticles extends BukkitRunnable implements Listener {
	private Arrow arrow;
	private ParticleData particleData;

	private static final double n = 3;

	public ArrowParticles(MoarBow bow, Arrow arrow) {
		this.particleData = bow.createParticleData();
		this.arrow = arrow;
	}

	@Override
	public void run() {
		if (arrow.isDead() || arrow.isOnGround()) {
			cancel();
			return;
		}

		for (double j = 0; j < n; j++)
			particleData.displayParticle(arrow.getLocation().add(0, .25, 0).add(arrow.getVelocity().multiply(j / n)));
	}
}
