package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class ManHuntUtilities {

    public static final Server SERVER = Bukkit.getServer();
    public static final Plugin MANHUNT_PLUGIN = SERVER.getPluginManager().getPlugin("MCManHunt");
    public static final AtomicBoolean GAME_IN_PROGRESS = new AtomicBoolean(false);
    public static final AtomicBoolean GAME_PAUSED = new AtomicBoolean(false);
    public static final AtomicBoolean HUNTER_MOVEMENT_RESTRICTED = new AtomicBoolean(false);
    public static final AtomicBoolean RUNNER_MOVEMENT_RESTRICTED = new AtomicBoolean(false);
    private static final Map<String, Player> HUNTER_MAP;
    private static final Map<String, Player> RUNNER_MAP;

    static {
        HUNTER_MAP = new HashMap<>();
        RUNNER_MAP = new HashMap<>();
    }

    public static CompassMeta updateCompassMeta(@NotNull final CompassMeta compassMeta) {
        final String playerName = compassMeta.getDisplayName().split(" ")[0];
        final Player player = ManHuntUtilities.SERVER.getPlayer(playerName);
        if (player != null && player.isOnline()) {
            compassMeta.setLodestone(ManHuntUtilities.RUNNER_MAP.get(playerName).getLocation());
        }
        return compassMeta;
    }

    public static ItemStack createTrackingCompass(@NotNull final String playerTrackedCompassMeta) {
        final CompassMeta compassMeta = (CompassMeta) new ItemStack(Material.COMPASS).getItemMeta();
        final Player player = ManHuntUtilities.SERVER.getPlayer(playerTrackedCompassMeta);
        compassMeta.setDisplayName(playerTrackedCompassMeta + " Tracker");
        if (player != null && player.isOnline()) {
            compassMeta.setLodestone(ManHuntUtilities.RUNNER_MAP.get(playerTrackedCompassMeta).getLocation());
            compassMeta.setLodestoneTracked(false);
        }
        final ItemStack trackingCompass = new ItemStack(Material.COMPASS);
        trackingCompass.setItemMeta(compassMeta);
        return trackingCompass;
    }

    public static void distributeTrackers() {
        final Player[] hunters = ManHuntUtilities.HUNTER_MAP.values().toArray(new Player[0]);
        final Player[] runners = ManHuntUtilities.RUNNER_MAP.values().toArray(new Player[0]);
        for (int x = 0; x < ManHuntUtilities.RUNNER_MAP.size(); x++) {
            for (Player hunter : hunters) {
                hunter.getInventory().setItem(x, createTrackingCompass(runners[x].getName()));
            }
        }
    }

    public static void givePlayerHunterCompasses(@NotNull final Player player) {
        final Player[] runners = ManHuntUtilities.RUNNER_MAP.values().toArray(new Player[0]);
        for (int x = 0; x < ManHuntUtilities.RUNNER_MAP.size(); x++) {
            player.getInventory().setItem(x, createTrackingCompass(runners[x].getName()));
        }
    }

    public static void stopGame() {
        ManHuntUtilities.GAME_IN_PROGRESS.set(false);
        ManHuntUtilities.GAME_PAUSED.set(false);
        ManHuntUtilities.HUNTER_MOVEMENT_RESTRICTED.set(false);
        ManHuntUtilities.RUNNER_MOVEMENT_RESTRICTED.set(false);
        clearHuntersInventory();
        clearRunnersInventory();
        ManHuntUtilities.RUNNER_MAP.clear();
        ManHuntUtilities.HUNTER_MAP.clear();
    }

    private static void clearRunnersInventory() {
        ManHuntUtilities.RUNNER_MAP.forEach((name, player) -> {
            if (player.isOnline()) player.getInventory().clear();
        });
    }

    private static void clearHuntersInventory() {
        ManHuntUtilities.HUNTER_MAP.forEach((name, player) -> {
            if (player.isOnline()) player.getInventory().clear();
        });
    }

    public static void startGame() {
        ManHuntUtilities.GAME_IN_PROGRESS.set(true);
        ManHuntUtilities.restrictRunnerMovement();
        ManHuntUtilities.restrictHunterMovement();
        ManHuntUtilities.distributeTrackers();
        ManHuntUtilities.SERVER.broadcastMessage("The Game will start in 10 seconds! The Runner(s) will have a 30 second head-start!");
        new BukkitRunnable() {
            @Override
            public void run() {
                ManHuntUtilities.allowRunnerMovement();
                ManHuntUtilities.SERVER.broadcastMessage("The Runners can now start!");
            }
        }.runTaskLater(ManHuntUtilities.MANHUNT_PLUGIN, 200);
        new BukkitRunnable() {
            @Override
            public void run() {
                ManHuntUtilities.allowHunterMovement();
                ManHuntUtilities.SERVER.broadcastMessage("The Hunters are coming!");
            }
        }.runTaskLater(ManHuntUtilities.MANHUNT_PLUGIN, 800);
    }

    public static void pauseGame(Player player) {
        ManHuntUtilities.GAME_PAUSED.set(true);
        ManHuntUtilities.restrictHunterMovement();
        ManHuntUtilities.restrictRunnerMovement();
        ManHuntUtilities.SERVER.broadcastMessage("The game was paused by " + player.getName() + ".");
    }

    public static void resumeGame() {
        ManHuntUtilities.GAME_PAUSED.set(false);
        ManHuntUtilities.SERVER.broadcastMessage("Game will resume in 5 seconds!");
        new BukkitRunnable() {
            public void run() {
                ManHuntUtilities.allowRunnerMovement();
                ManHuntUtilities.allowHunterMovement();
                ManHuntUtilities.SERVER.broadcastMessage("Game has resumed!");
            }
        }.runTaskLater(ManHuntUtilities.MANHUNT_PLUGIN, 100);
    }

    public static boolean addHunter(String playerName) {
        final Player player = ManHuntUtilities.SERVER.getPlayer(playerName);
        if (player != null && player.isOnline() && !ManHuntUtilities.HUNTER_MAP.containsKey(playerName) && !ManHuntUtilities.RUNNER_MAP.containsKey(playerName)) {
            ManHuntUtilities.HUNTER_MAP.put(playerName, player);
            return true;
        }
        return false;
    }

    public static boolean addRunner(String playerName) {
        final Player player = ManHuntUtilities.SERVER.getPlayer(playerName);
        if (player != null && player.isOnline() && !ManHuntUtilities.HUNTER_MAP.containsKey(playerName) && !ManHuntUtilities.RUNNER_MAP.containsKey(playerName)) {
            ManHuntUtilities.RUNNER_MAP.put(playerName, player);
            return true;
        }
        return false;
    }

    public static boolean addHunter(final Player player) {
        if (player != null && player.isOnline() && !ManHuntUtilities.isHunter(player) && !ManHuntUtilities.isRunner(player)) {
            ManHuntUtilities.HUNTER_MAP.put(player.getName(), player);
            return true;
        }
        return false;
    }

    public static boolean addRunner(final Player player) {
        if (player != null && player.isOnline() && !ManHuntUtilities.isHunter(player) && !ManHuntUtilities.isRunner(player)) {
            ManHuntUtilities.RUNNER_MAP.put(player.getName(), player);
            return true;
        }
        return false;
    }

    public static void resetplayerroles() {
        ManHuntUtilities.HUNTER_MAP.clear();
        ManHuntUtilities.RUNNER_MAP.clear();
    }

    public static boolean isHunter(final Player player) {
        return ManHuntUtilities.HUNTER_MAP.containsValue(player);
    }

    public static boolean isHunter(final String playerName) {
        return ManHuntUtilities.HUNTER_MAP.containsKey(playerName);
    }

    public static boolean isHunterMapEmpty() {
        return ManHuntUtilities.HUNTER_MAP.isEmpty();
    }

    public static boolean isRunnerMapEmpty() {
        return ManHuntUtilities.RUNNER_MAP.isEmpty();
    }

    public static Collection<Player> getHunters() {
        return ManHuntUtilities.HUNTER_MAP.values();
    }

    public static Collection<Player> getRunners() {
        return ManHuntUtilities.RUNNER_MAP.values();
    }

    public static void removeHunter(final String playerName) {
        ManHuntUtilities.HUNTER_MAP.remove(playerName);
    }

    public static void removeRunner(final String playerName) {
        ManHuntUtilities.RUNNER_MAP.remove(playerName);
    }

    public static boolean isRunner(final Player player) {
        return ManHuntUtilities.RUNNER_MAP.containsValue(player);
    }

    public static boolean isRunner(final String playerName) {
        return ManHuntUtilities.RUNNER_MAP.containsKey(playerName);
    }

    public static boolean removePlayerFromHunters(final Player player) {
        if (ManHuntUtilities.HUNTER_MAP.containsValue(player)) {
            ManHuntUtilities.HUNTER_MAP.remove(player.getName());
            return true;
        }
        return false;
    }

    public static boolean removePlayerFromRunners(final Player player) {
        if (ManHuntUtilities.RUNNER_MAP.containsValue(player)) {
            ManHuntUtilities.RUNNER_MAP.remove(player.getName());
            return true;
        }
        return false;
    }

    public static void restrictHunterMovement() {
        ManHuntUtilities.HUNTER_MOVEMENT_RESTRICTED.set(true);
    }

    public static void restrictRunnerMovement() {
        ManHuntUtilities.RUNNER_MOVEMENT_RESTRICTED.set(true);
    }

    public static void allowRunnerMovement() {
        ManHuntUtilities.RUNNER_MOVEMENT_RESTRICTED.set(false);
    }

    public static void allowHunterMovement() {
        ManHuntUtilities.HUNTER_MOVEMENT_RESTRICTED.set(false);
    }

}
