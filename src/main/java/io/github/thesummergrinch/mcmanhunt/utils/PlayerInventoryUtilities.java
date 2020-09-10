package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public final class PlayerInventoryUtilities {

    /**
     * Clears the inverntory of the given Player.
     * @param player - Player whose inventory will be cleared.
     */
    public static void clearPlayerInventory(final Player player) {
        if (player.isOnline()) player.getInventory().clear();
    }

    /**
     * Clears the inventory of the Runners. Typically used when the game ends, or the game is terminated before its
     * natural conclusion.
     */
    public static void clearRunnersInventory() {
        ManHuntUtilities.getRunners().forEach((player) -> {
            clearPlayerInventory(player);
        });
    }

    /**
     * Clears the inventory of the Hunters. Typically used when the game ends, or the game is terminated before its
     * natural conclusion.
     */
    public static void clearHuntersInventory() {
        ManHuntUtilities.getHunters().forEach((player) -> {
            clearPlayerInventory(player);
        });
    }

    /**
     * Updates the Lodestone Location in the CompassMeta, to track the target player.
     * Used whenever a Hunter interacts with an Air-block while holding a Tracking Compass in either hand. Does not
     * the location if the target is in the Nether.
     *
     * @param compassMeta - The CompassMeta-object that will be updated.
     * @return CompassMeta - The updated CompassMeta-object.
     */
    public static synchronized CompassMeta updateCompassMeta(final CompassMeta compassMeta) {
        final String playerName = compassMeta.getDisplayName().split(" ")[0];
        final Player player = ManHuntUtilities.getPlayer(playerName);
        if (player != null && player.isOnline() && !player.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
            compassMeta.setLodestone(ManHuntUtilities.getPlayer(playerName).getLocation());
        }
        return compassMeta;
    }

    /**
     * Creates an ItemStack of a Compass, with a quantity of 1. Also creates a CompassMeta with the correct parameters
     * to follow a player, and assigns it to said Compass.
     *
     * @param playerTrackedCompassMeta - The username of the player that will be tracked by the compass.
     * @return compass - The compass made to track the specified player.
     */
    public static synchronized ItemStack createTrackingCompass(final String playerTrackedCompassMeta) {
        final CompassMeta compassMeta = (CompassMeta) new ItemStack(Material.COMPASS).getItemMeta();
        final Player player = ManHuntUtilities.getPlayer(playerTrackedCompassMeta);
        compassMeta.setDisplayName(playerTrackedCompassMeta + " Tracker");
        if (player != null && player.isOnline()) {
            compassMeta.setLodestone(ManHuntUtilities.getPlayer(playerTrackedCompassMeta).getLocation());
            compassMeta.setLodestoneTracked(false);
        }
        final ItemStack trackingCompass = new ItemStack(Material.COMPASS);
        trackingCompass.setItemMeta(compassMeta);
        return trackingCompass;
    }

    /**
     * Distributes one compass per Runner to every Hunter. Used at the start of a game.
     */
    public static synchronized void distributeTrackers() {
        final Player[] hunters = ManHuntUtilities.getHunters().toArray(new Player[0]);
        final Player[] runners = ManHuntUtilities.getRunners().toArray(new Player[0]);
        for (int x = 0; x < runners.length; x++) {
            for (Player hunter : hunters) {
                hunter.getInventory().setItem(x, createTrackingCompass(runners[x].getName()));
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
            player.getInventory().setItem(x, createTrackingCompass(runners[x].getName()));
        }
    }

}
