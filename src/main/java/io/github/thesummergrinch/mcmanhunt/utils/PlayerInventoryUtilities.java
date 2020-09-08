package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public final class PlayerInventoryUtilities {

    public static void clearRunnersInventory() {
        ManHuntUtilities.getRunners().forEach((player) -> {
            if (player.isOnline()) player.getInventory().clear();
        });
    }

    public static void clearHuntersInventory() {
        ManHuntUtilities.getHunters().forEach((player) -> {
            if (player.isOnline()) player.getInventory().clear();
        });
    }

    public static synchronized CompassMeta updateCompassMeta(final CompassMeta compassMeta) {
        final String playerName = compassMeta.getDisplayName().split(" ")[0];
        final Player player = ManHuntUtilities.getPlayer(playerName);
        if (player != null && player.isOnline()) {
            compassMeta.setLodestone(ManHuntUtilities.getPlayer(playerName).getLocation());
        }
        return compassMeta;
    }

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

    public static synchronized void distributeTrackers() {
        final Player[] hunters = ManHuntUtilities.getHunters().toArray(new Player[0]);
        final Player[] runners = ManHuntUtilities.getRunners().toArray(new Player[0]);
        for (int x = 0; x < runners.length; x++) {
            for (Player hunter : hunters) {
                hunter.getInventory().setItem(x, createTrackingCompass(runners[x].getName()));
            }
        }
    }

    public static void givePlayerHunterCompasses(final Player player) {
        final Player[] runners = ManHuntUtilities.getRunners().toArray(new Player[0]);
        for (int x = 0; x < runners.length; x++) {
            player.getInventory().setItem(x, createTrackingCompass(runners[x].getName()));
        }
    }

}
