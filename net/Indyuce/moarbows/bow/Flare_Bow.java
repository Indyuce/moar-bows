package net.Indyuce.moarbows.bow;

//import java.lang.reflect.Method;
//import java.util.Random;
//
//import org.bukkit.Color;
//import org.bukkit.FireworkEffect;
//import org.bukkit.Location;
//import org.bukkit.World;
//import org.bukkit.entity.Arrow;
//import org.bukkit.entity.Firework;
//import org.bukkit.entity.Player;
//import org.bukkit.event.entity.EntityShootBowEvent;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.FireworkMeta;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import net.Indyuce.moarbows.FEP;
//import net.Indyuce.moarbows.MoarBows;
//import net.Indyuce.moarbows.api.BowModifier;
//import net.Indyuce.moarbows.api.MoarBow;
//
//public class Flare_Bow extends MoarBow {
//	public Flare_Bow() {
//		super(new String[] { "Shoots arrows that explode into", "a beautiful firework show." }, 0, 3.0, "redstone:0,255,0", new String[] { "FIREWORK,FIREWORK,FIREWORK", "FIREWORK,BOW,FIREWORK", "FIREWORK,FIREWORK,FIREWORK" });
//
//		addModifier(new BowModifier("multicolor", false));
//	}
//
//	@Override
//	public boolean shoot(EntityShootBowEvent e, Arrow a, Player p, ItemStack i) {
//		new BukkitRunnable() {
//			public void run() {
//				if (!a.isDead())
//					land(p, a);
//			}
//		}.runTaskLater(MoarBows.plugin, 40);
//		return true;
//	}
//
//	@Override
//	public void land(Player p, Arrow a) {
//		a.remove();
//		for (int x = -2; x < 3; x += 2) {
//			for (int y = -2; y < 3; y += 2) {
//				for (int z = -2; z < 3; z += 2) {
//					Location loc = a.getLocation().clone();
//					loc.add(x * 3 + ((new Random().nextDouble() - .5) * 2), y * 3 + ((new Random().nextDouble() - .5) * 2), z * 3 + ((new Random().nextDouble() - .5) * 2));
//					int r = 0;
//					int g = 255;
//					int b = 0;
//					if (MoarBows.bows.getBoolean("FLARE_BOW.multicolor")) {
//						r = new Random().nextInt(255);
//						g = new Random().nextInt(255);
//						b = new Random().nextInt(255);
//					}
//					FireworkEffect fe = FireworkEffect.builder().withColor(Color.fromBGR(r, g, b)).with(FireworkEffect.Type.BALL_LARGE).build();
//					try {
//						new FEP().playFirework(loc, fe);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}
//
//	public class FEP {
//		private Method world_getHandle = null;
//		private Method nms_world_broadcastEntityEffect = null;
//		private Method firework_getHandle = null;
//
//		public void playFirework(Location loc, FireworkEffect fe) throws Exception {
//			World world = loc.getWorld();
//			Firework fw = (Firework) world.spawn(loc, Firework.class);
//			Object nms_world = null;
//			Object nms_firework = null;
//			if (world_getHandle == null) {
//				world_getHandle = getMethod(world.getClass(), "getHandle");
//				firework_getHandle = getMethod(fw.getClass(), "getHandle");
//			}
//			nms_world = world_getHandle.invoke(world, (Object[]) null);
//			nms_firework = firework_getHandle.invoke(fw, (Object[]) null);
//			if (nms_world_broadcastEntityEffect == null)
//				nms_world_broadcastEntityEffect = getMethod(nms_world.getClass(), "broadcastEntityEffect");
//			FireworkMeta data = (FireworkMeta) fw.getFireworkMeta();
//			data.clearEffects();
//			data.setPower(1);
//			data.addEffect(fe);
//			fw.setFireworkMeta(data);
//			nms_world_broadcastEntityEffect.invoke(nms_world, new Object[] { nms_firework, (byte) 17 });
//			fw.remove();
//		}
//
//		private Method getMethod(Class<?> cl, String method) {
//			for (Method m : cl.getMethods())
//				if (m.getName().equals(method))
//					return m;
//			return null;
//		}
//	}
//}
