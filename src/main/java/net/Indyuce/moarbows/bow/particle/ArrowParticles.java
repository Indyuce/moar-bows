package net.Indyuce.moarbows.bow.particle;

import org.bukkit.entity.Arrow;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.moarbows.bow.MoarBow;

public class ArrowParticles extends BukkitRunnable implements Listener {
	private final Arrow arrow;
	private final ParticleData particleData;

	private static final double n = 3;

	public ArrowParticles(MoarBow bow, Arrow arrow) {
		this.particleData = bow.getParticles();
		this.arrow = arrow;
	}

	@Override
	public void run() {
		if (arrow.isDead() || arrow.isOnGround())
			cancel();
		else
			for (double j = 0; j < n; j++)
				particleData.displayParticle(arrow.getLocation().add(0, .25, 0).add(arrow.getVelocity().multiply(j / n)));
	}
}
