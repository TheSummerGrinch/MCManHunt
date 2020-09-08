package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class ManHuntUtilities {


    private static final Server SERVER = Bukkit.getServer();
    public static final Plugin MANHUNT_PLUGIN = SERVER.getPluginManager().getPlugin("MCManHunt");
    private static final Map<String, Player> HUNTER_MAP;
    private static final Map<String, Player> RUNNER_MAP;

    private static int maxRunners;
    private static int maxHunters;

    static {
        HUNTER_MAP = new HashMap<>();
        RUNNER_MAP = new HashMap<>();
    }

    public static synchronized Player getPlayer(String playerName) {
        return SERVER.getPlayer(playerName);
    }

    public static synchronized boolean isHunterTeamFull() {
        return maxHunters <= ManHuntUtilities.HUNTER_MAP.size();
    }

    public static synchronized boolean isRunnerTeamFull() {
        return maxRunners <= ManHuntUtilities.RUNNER_MAP.size();
    }

    public static synchronized boolean isRunnerTeamOverCapacity() {
        return maxRunners < ManHuntUtilities.RUNNER_MAP.size();
    }

    public static synchronized boolean isHunterTeamOverCapacity() {
        return maxHunters < ManHuntUtilities.HUNTER_MAP.size();
    }

    public static synchronized void broadcastMessage(final String message) {
        ManHuntUtilities.SERVER.broadcastMessage(message);
    }

    public static synchronized void setMaxRunners(final int maxRunners) {
        ManHuntUtilities.maxRunners = maxRunners;
    }

    public static synchronized void setMaxHunters(final int maxHunters) {
        ManHuntUtilities.maxHunters = maxHunters;
    }

    public static FileConfiguration getConfig() {
        FileConfiguration config;
        try {
            config = ManHuntUtilities.MANHUNT_PLUGIN.getConfig();
        } catch (NullPointerException exception) {
            ManHuntUtilities.SERVER.getLogger().log(Level.CONFIG, "No config.yml detected. Default config.yml has been created.");
            ManHuntUtilities.MANHUNT_PLUGIN.saveDefaultConfig();
            config = ManHuntUtilities.MANHUNT_PLUGIN.getConfig();
        }
        return config;
    }

    public static synchronized boolean addHunter(String playerName) {
        final Player player = ManHuntUtilities.SERVER.getPlayer(playerName);
        if (player != null && player.isOnline() && !ManHuntUtilities.HUNTER_MAP.containsKey(playerName) && !ManHuntUtilities.RUNNER_MAP.containsKey(playerName)) {
            ManHuntUtilities.HUNTER_MAP.put(playerName, player);
            return true;
        }
        return false;
    }

    public static synchronized boolean addRunner(String playerName) {
        final Player player = ManHuntUtilities.SERVER.getPlayer(playerName);
        if (player != null && player.isOnline() && !ManHuntUtilities.HUNTER_MAP.containsKey(playerName) && !ManHuntUtilities.RUNNER_MAP.containsKey(playerName)) {
            ManHuntUtilities.RUNNER_MAP.put(playerName, player);
            return true;
        }
        return false;
    }

    public static synchronized boolean addHunter(final Player player) {
        if (player != null && player.isOnline() && !ManHuntUtilities.isHunter(player) && !ManHuntUtilities.isRunner(player)) {
            ManHuntUtilities.HUNTER_MAP.put(player.getName(), player);
            return true;
        }
        return false;
    }

    public static synchronized boolean addRunner(final Player player) {
        if (player != null && player.isOnline() && !ManHuntUtilities.isHunter(player) && !ManHuntUtilities.isRunner(player)) {
            ManHuntUtilities.RUNNER_MAP.put(player.getName(), player);
            return true;
        }
        return false;
    }

    public static synchronized void resetplayerroles() {
        ManHuntUtilities.HUNTER_MAP.clear();
        ManHuntUtilities.RUNNER_MAP.clear();
    }

    public static synchronized boolean isHunter(final Player player) {
        return ManHuntUtilities.HUNTER_MAP.containsValue(player);
    }

    public static synchronized boolean isHunter(final String playerName) {
        return ManHuntUtilities.HUNTER_MAP.containsKey(playerName);
    }

    public static synchronized boolean isHunterMapEmpty() {
        return ManHuntUtilities.HUNTER_MAP.isEmpty();
    }

    public static synchronized boolean isRunnerMapEmpty() {
        return ManHuntUtilities.RUNNER_MAP.isEmpty();
    }

    public static synchronized Collection<Player> getHunters() {
        return ManHuntUtilities.HUNTER_MAP.values();
    }

    public static synchronized Collection<Player> getRunners() {
        return ManHuntUtilities.RUNNER_MAP.values();
    }

    public static synchronized void removeHunter(final String playerName) {
        ManHuntUtilities.HUNTER_MAP.remove(playerName);
    }

    public static synchronized void removeRunner(final String playerName) {
        ManHuntUtilities.RUNNER_MAP.remove(playerName);
    }

    public static synchronized boolean isRunner(final Player player) {
        return ManHuntUtilities.RUNNER_MAP.containsValue(player);
    }

    public static synchronized boolean isRunner(final String playerName) {
        return ManHuntUtilities.RUNNER_MAP.containsKey(playerName);
    }

    public static synchronized boolean removePlayerFromHunters(final Player player) {
        if (ManHuntUtilities.HUNTER_MAP.containsValue(player)) {
            ManHuntUtilities.HUNTER_MAP.remove(player.getName());
            return true;
        }
        return false;
    }

    public static synchronized boolean removePlayerFromRunners(final Player player) {
        if (ManHuntUtilities.RUNNER_MAP.containsValue(player)) {
            ManHuntUtilities.RUNNER_MAP.remove(player.getName());
            return true;
        }
        return false;
    }

    public static boolean hasLetters(String argument) {
        for (int x = 0; x < argument.length(); x++) {
            if (Character.isLetter(argument.charAt(x))) return true;
        }
        return false;
    }

}
