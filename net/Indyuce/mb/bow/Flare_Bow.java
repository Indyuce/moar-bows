package net.Indyuce.mb.bow;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.Indyuce.mb.FEP;
import net.Indyuce.mb.MoarBows;
import net.Indyuce.mb.api.BowModifier;
import net.Indyuce.mb.api.MoarBow;

public class Flare_Bow extends MoarBow {
	public Flare_Bow() {
		super(new String[] { "Shoots arrows that explode into", "a beautiful firework show." }, 0, 3.0, "redstone:0,255,0", new String[] { "FIREWORK,FIREWORK,FIREWORK", "FIREWORK,BOW,FIREWORK", "FIREWORK,FIREWORK,FIREWORK" });

		addModifier(new BowModifier("multicolor", false));
	}

	@Override
	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
		new BukkitRunnable() {
			public void run() {
				if (!a.isDead())
					land(p, a);
			}
		}.runTaskLater(MoarBows.plugin, 40);
		return true;
	}

	@Override
	public void land(Player p, Arrow a) {
		a.remove();
		for (int x = -2; x < 3; x += 2) {
			for (int y = -2; y < 3; y += 2) {
				for (int z = -2; z < 3; z += 2) {
					Location loc = a.getLocation().clone();
					loc.add(x * 3 + ((new Random().nextDouble() - .5) * 2), y * 3 + ((new Random().nextDouble() - .5) * 2), z * 3 + ((new Random().nextDouble() - .5) * 2));
					int r = 0;
					int g = 255;
					int b = 0;
					if (MoarBows.bows.getBoolean("FLARE_BOW.multicolor")) {
						r = new Random().nextInt(255);
						g = new Random().nextInt(255);
						b = new Random().nextInt(255);
					}
					FireworkEffect fe = FireworkEffect.builder().withColor(Color.fromBGR(r, g, b)).with(FireworkEffect.Type.BALL_LARGE).build();
					try {
						new FEP().playFirework(loc, fe);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
