package net.Indyuce.moarbows;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BowUtils implements Listener {
    public static String caseOnWords(String str) {
        StringBuilder builder = new StringBuilder(str);
        boolean isLastSpace = true;
        for (int i = 0; i < builder.length(); i++) {
            char ch = builder.charAt(i);
            if (isLastSpace && ch >= 'a' && ch <= 'z') {
                builder.setCharAt(i, (char) (ch + ('A' - 'a')));
                isLastSpace = false;
            } else if (ch != ' ')
                isLastSpace = false;
            else
                isLastSpace = true;
        }
        return builder.toString();
    }

    public static boolean consumeAmmo(LivingEntity entity, ItemStack ammo) {

        // If sender is not a player, then do not consume any ammo
        if (!(entity instanceof Player))
            return true;

        // Does not consume ammo if the player is in creative mode
        Player player = (Player) entity;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return true;

        // Returns false if the player has no item
        if (!player.getInventory().containsAtLeast(ammo, 1))
            return false;

        // Does not consume ammo if the bow has infinity enchantment
        ItemStack bowInHand = getBowInHand(player);
        if (Objects.nonNull(bowInHand) && bowInHand.getEnchantments().containsKey(Enchantment.ARROW_INFINITE)) {
            return true;
        }

        // Returns true and consumes the ammo if the player has enough
        player.getInventory().removeItem(ammo);
        return true;
    }

    private static ItemStack getBowInHand(Player player) {
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (itemInMainHand.getType() == Material.BOW) {
            return itemInMainHand;
        }

        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
        if (itemInOffHand.getType() == Material.BOW) {
            return itemInOffHand;
        }
        return null;
    }

    public static boolean isPluginItem(ItemStack item, boolean lore) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && (!lore || item.getItemMeta().hasLore());
    }

    public static int getBowLevel(ItemStack item) {
        return item.hasItemMeta() ? item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(MoarBows.plugin, "MoarBowLevel"), PersistentDataType.INTEGER) : 0;
    }

    public static double getPowerDamageMultiplier(ItemStack item) {
        return item == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) ? 1
                : 1 + .25 * (item.getItemMeta().getEnchantLevel(Enchantment.ARROW_DAMAGE) + 1);
    }

    public static boolean canTarget(LivingEntity shooter, Location loc, Entity target) {
        if (target.hasMetadata("NPC"))
            return false;

        return !target.equals(shooter) && (loc == null || target.getBoundingBox().expand(.5, .5, .5).contains(loc.toVector()));
    }

    public static double truncation(double x, int n) {
        double pow = Math.pow(10.0, n);
        return Math.floor(x * pow) / pow;
    }

    public static Vector rotateFunc(Vector v, Location loc) {
        double yaw = loc.getYaw() / 180 * Math.PI;
        double pitch = loc.getPitch() / 180 * Math.PI;
        v = rotAxisX(v, pitch);
        v = rotAxisY(v, -yaw);
        return v;
    }

    private static Vector rotAxisX(Vector v, double a) {
        double y = v.getY() * Math.cos(a) - v.getZ() * Math.sin(a);
        double z = v.getY() * Math.sin(a) + v.getZ() * Math.cos(a);
        return v.setY(y).setZ(z);
    }

    private static Vector rotAxisY(Vector v, double b) {
        double x = v.getX() * Math.cos(b) + v.getZ() * Math.sin(b);
        double z = v.getX() * -Math.sin(b) + v.getZ() * Math.cos(b);
        return v.setX(x).setZ(z);
    }

    /**
     * Method to get all entities surrounding a location. This method does not
     * take every entity in the world but rather takes all the entities from the
     * 9 chunks around the entity, so even if the location is at the border of a
     * chunk (worst case border of 4 chunks), the entity will still be included
     */
    public static void forEachNearbyChunkEntity(Location loc, Consumer<Entity> action) {

        /*
         * Another method to save performance is if an entity bounding box
         * calculation is made twice in the same tick then the method does not
         * need to be called twice, it can utilize the same entity list since
         * the entities have not moved (e.g fireball which does 2+ calculations
         * per tick)
         */
        int cx = loc.getChunk().getX();
        int cz = loc.getChunk().getZ();

        for (int x = -1; x < 2; x++)
            for (int z = -1; z < 2; z++)
                for (Entity entity : loc.getWorld().getChunkAt(cx + x, cz + z).getEntities())
                    action.accept(entity);
    }

    public static <T> void clean(Iterable<T> collection, Predicate<T> clean) {
        Iterator<T> it = collection.iterator();
        while (it.hasNext()) {
            T next = it.next();
            if (clean.test(next))
                it.remove();
        }
    }
}
