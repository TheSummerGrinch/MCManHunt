package io.github.thesummergrinch.mcmanhunt.utils;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.HashMap;
import java.util.UUID;

public final class PlayerInventoryUtilities {

    private static final HashMap<UUID, ItemStack> COMPASS_MAP = new HashMap<>();

    private static boolean storeCompass(final UUID playerUUID, final ItemStack compass) {
        if (!compass.getType().equals(Material.COMPASS)) return false;
        COMPASS_MAP.put(playerUUID, compass);
        return true;
    }

    private static ItemStack getCompass(final UUID playerUUID) {
        if (COMPASS_MAP.containsKey(playerUUID)) return COMPASS_MAP.get(playerUUID);
        return null;
    }

    /**
     * Updates the Lodestone Location in the CompassMeta, to track the target player.
     * Used whenever a Hunter interacts with an Air-block while holding a Tracking Compass in either hand. Does not
     * the location if the target is in the Nether.
     *
     * @param compassMeta - The CompassMeta-object that will be updated.
     * @return CompassMeta - The updated CompassMeta-object.
     */
    public static CompassMeta updateCompassMeta(final CompassMeta compassMeta) {
        final String playerName = compassMeta.getDisplayName().split(" ")[0];
        final Player player = ManHuntUtilities.getPlayer(playerName);
        if (player != null && player.isOnline() && !player.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            compassMeta.setLodestone(ManHuntUtilities.getPlayer(playerName).getLocation());
        }
        return compassMeta;
    }

    private static ItemStack createTrackingCompass(final UUID playerUUID) {
        final Player player = ManHuntUtilities.getPlayer(playerUUID).getPlayer();
        if (player != null) {
            if (COMPASS_MAP.containsKey(playerUUID)) {
                final ItemStack compass = getCompass(playerUUID);
                compass.setItemMeta(updateCompassMeta((CompassMeta) compass.getItemMeta()));
                return compass;
            }
            final ItemStack compass = new ItemStack(Material.COMPASS);
            final CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
            if ((!player.getWorld().getEnvironment().equals(World.Environment.NETHER) &&
                    !player.getWorld().getEnvironment().equals(World.Environment.THE_END))) {
                compassMeta.setLodestone(player.getLocation());
            } else {
                compassMeta.setLodestoneTracked(true);
            }
            compass.addEnchantment(Enchantment.VANISHING_CURSE, 1);
            compass.setItemMeta(compassMeta);
            storeCompass(playerUUID, compass);
            return compass;
        }
        return null;
    }

    /**
     * Distributes one compass per Runner to every Hunter. Used at the start of a game.
     */
    static synchronized void distributeTrackers() {
        final Player[] hunters = ManHuntUtilities.getHunters().toArray(new Player[0]);
        final Player[] runners = ManHuntUtilities.getRunners().toArray(new Player[0]);
        for (int x = 0; x < runners.length; x++) {
            for (Player hunter : hunters) {
                hunter.getInventory().setItem(x, createTrackingCompass(runners[x].getUniqueId()));
            }
        }
    }

    /**
     * Gives the specified player one compass per player on the Runner-team. Typically used when a Hunter respawns after
     * dying.
     *
     * @param player - The Player-object that will receive new tracking compasses.
     */
    public static void givePlayerHunterCompasses(final Player player) {
        final Player[] runners = ManHuntUtilities.getRunners().toArray(new Player[0]);
        for (int x = 0; x < runners.length; x++) {
            player.getInventory().setItem(x, createTrackingCompass(runners[x].getUniqueId()));
        }
    }

}
