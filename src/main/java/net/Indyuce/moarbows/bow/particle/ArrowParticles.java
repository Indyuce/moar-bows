package net.Indyuce.moarbows.bow.particle;

import com.github.Anon8281.universalScheduler.UniversalRunnable;
import net.Indyuce.moarbows.bow.MoarBow;
import org.bukkit.entity.Arrow;
import org.bukkit.event.Listener;

public class ArrowParticles extends UniversalRunnable implements Listener {
    private static final double n = 3;
    private final Arrow arrow;
    private final ParticleData particleData;

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
