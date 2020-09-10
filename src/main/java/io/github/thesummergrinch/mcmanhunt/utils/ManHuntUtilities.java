package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ManHuntUtilities {


    private static final AtomicBoolean IS_FIRST_RUN =  new AtomicBoolean(false);
    private static final Server SERVER = Bukkit.getServer();
    private static final Plugin MANHUNT_PLUGIN = SERVER.getPluginManager().getPlugin("MCManHunt");
    private static final Map<String, Player> HUNTER_MAP;
    private static final Map<String, Player> RUNNER_MAP;
    private static final Set<Player> RANDOM_TEAM_QUEUE;

    private static int maxRunners;
    private static int maxHunters;

    static {
        HUNTER_MAP = new HashMap<>();
        RUNNER_MAP = new HashMap<>();
        RANDOM_TEAM_QUEUE = new HashSet<>();
    }

    public static boolean addPlayerToRandomQueue(final Player player) {
        return RANDOM_TEAM_QUEUE.add(player);
    }

    public static Set<Player> getPlayersInRandomQueue() {
        return RANDOM_TEAM_QUEUE;
    }

    public static void clearRandomTeamQueue() {
        RANDOM_TEAM_QUEUE.clear();
    }

    public static Plugin getManHuntPlugin() {
        return ManHuntUtilities.MANHUNT_PLUGIN;
    }

    public static boolean isFirstRun() {
        return ManHuntUtilities.IS_FIRST_RUN.get();
    }

    /**
     * Gets the Player-object of the specified player.
     *
     * @param playerName - The username of the player associated with the required Player-object.
     * @return player - The Player-object associated with the given username.
     */
    public static synchronized Player getPlayer(String playerName) {
        return SERVER.getPlayer(playerName);
    }

    /**
     * Checks whether or not the number of players in the Hunter-team is equal to, or exceeds, the maximum value set by
     * the maxHunters-field.
     *
     * @return boolean - True if full, false if not.
     */
    public static synchronized boolean isHunterTeamFull() {
        return maxHunters <= ManHuntUtilities.HUNTER_MAP.size();
    }

    /**
     * Checks whether or not the number of players in the Runner-team is equal to, or exceeds, the maximum value set by
     * the maxRunners-field.
     *
     * @return boolean - True if full, false if not.
     */
    public static synchronized boolean isRunnerTeamFull() {
        return maxRunners <= ManHuntUtilities.RUNNER_MAP.size();
    }

    /**
     * Checks whether or not the number of players in the Runner-team exceeds the maximum value set by the maxRunners-
     * field.
     *
     * @return boolean - True if the value exceeds the maximum, false if not.
     */
    public static synchronized boolean isRunnerTeamOverCapacity() {
        return maxRunners < ManHuntUtilities.RUNNER_MAP.size();
    }

    /**
     * Check whether or not the number of players in the Hunter-team exceeds the maximum value set by the maxHunters-
     * field.
     *
     * @return boolean - True if the value exceeds the maximum, false if not.
     */
    public static synchronized boolean isHunterTeamOverCapacity() {
        return maxHunters < ManHuntUtilities.HUNTER_MAP.size();
    }

    /**
     * Wrapper used to broadcast a message to the Server, without exposing the Server-object to the rest of the project.
     *
     * @param message - The message to be broadcast to the Server.
     */
    public static synchronized void broadcastMessage(final String message) {
        ManHuntUtilities.SERVER.broadcastMessage(message);
    }

    /**
     * Sets the maximum number of players allowed in the Runner-team. Only effective when set before the start of the
     * game.
     *
     * @param maxRunners - The maximum value.
     */
    public static synchronized void setMaxRunners(final int maxRunners) {
        ManHuntUtilities.maxRunners = maxRunners;
    }

    /**
     * Sets the maximum number of players allowed in the Hunter-team. Only effective when set before the start of the
     * game.
     *
     * @param maxHunters - The maximum value.
     */
    public static synchronized void setMaxHunters(final int maxHunters) {
        ManHuntUtilities.maxHunters = maxHunters;
    }

    /**
     * Reads the config.yml, or saves the default included with the JAR. Returns the FileConfiguration of the
     * config.yml.
     *
     * @return fileConfiguration - The FileConfiguration read from the config.yml.
     */
    @Deprecated
    public static FileConfiguration getConfig() {
        if (!MANHUNT_PLUGIN.getDataFolder().exists()) {
            MANHUNT_PLUGIN.saveDefaultConfig();
            ManHuntUtilities.IS_FIRST_RUN.set(true);
        }
        return MANHUNT_PLUGIN.getConfig();
    }

    /**
     * Updates the configurable settings read from the config.yml.
     */
    public static void updateFromConfig() {
        maxHunters = ConfigurationUtilities.getInt("max-hunters");
        maxRunners = ConfigurationUtilities.getInt("max-runners");
    }

    /**
     * Adds the specified player to the Hunter-team.
     *
     * @param playerName - Username of the player that needs to be added to the Hunter-team.
     * @return boolean - True if successfully added to the Hunter-team, false otherwise.
     */
    public static synchronized boolean addHunter(String playerName) {
        final Player player = ManHuntUtilities.SERVER.getPlayer(playerName);
        if (player != null && player.isOnline() && !ManHuntUtilities.HUNTER_MAP.containsKey(playerName)
                && !ManHuntUtilities.RUNNER_MAP.containsKey(playerName) && !ManHuntUtilities.isHunterTeamOverCapacity()) {
            ManHuntUtilities.HUNTER_MAP.put(playerName, player);
            return true;
        }
        return false;
    }

    /**
     * Adds the specified player to the Runner-team.
     *
     * @param playerName - Username of the player that needs to be added to the Runner-team.
     * @return boolean - True if successfully added to the Runner-team.
     */
    public static synchronized boolean addRunner(String playerName) {
        final Player player = ManHuntUtilities.SERVER.getPlayer(playerName);
        if (player != null && player.isOnline() && !ManHuntUtilities.HUNTER_MAP.containsKey(playerName)
                && !ManHuntUtilities.RUNNER_MAP.containsKey(playerName) && !ManHuntUtilities.isRunnerTeamFull()) {
            ManHuntUtilities.RUNNER_MAP.put(playerName, player);
            return true;
        }
        return false;
    }

    /**
     * Adds the specified player to the Hunter-team.
     *
     * @param player - Player-object of the player that needs to be added to the Hunter-team.
     * @return boolean - True if successfully added to the Hunter-team, false otherwise.
     */
    public static synchronized boolean addHunter(final Player player) {
        if (player != null && player.isOnline() && !ManHuntUtilities.isHunter(player)
                && !ManHuntUtilities.isRunner(player) && !ManHuntUtilities.isHunterTeamOverCapacity()) {
            ManHuntUtilities.HUNTER_MAP.put(player.getName(), player);
            return true;
        }
        return false;
    }

    /**
     * Adds the specified player to the Runner-team.
     *
     * @param player - Player-object of the player that needs to be added to the Runner-team.
     * @return boolean - True if successfully added to the Runner-team.
     */
    public static synchronized boolean addRunner(final Player player) {
        if (player != null && player.isOnline() && !ManHuntUtilities.isHunter(player)
                && !ManHuntUtilities.isRunner(player) && !ManHuntUtilities.isRunnerTeamFull()) {
            ManHuntUtilities.RUNNER_MAP.put(player.getName(), player);
            return true;
        }
        return false;
    }

    /**
     * Clears the HashMaps containing the Hunter-team and Runner-team data. Effectively clears the teams.
     */
    public static synchronized void resetplayerroles() {
        ManHuntUtilities.HUNTER_MAP.clear();
        ManHuntUtilities.RUNNER_MAP.clear();
    }

    /**
     * Checks if the specified player is in the Hunter-team.
     *
     * @param player - The Player-object of the player that is being checked.
     * @return boolean - True if the player is in the Hunter-team, false otherwise.
     */
    public static synchronized boolean isHunter(final Player player) {
        return ManHuntUtilities.HUNTER_MAP.containsValue(player);
    }

    /**
     * Checks if the specified player is in the Runner-team.
     *
     * @param playerName - The username of the player that is being checked.
     * @return boolean - True if the player is in the Hunter-team, false otherwise.
     */
    public static synchronized boolean isHunter(final String playerName) {
        return ManHuntUtilities.HUNTER_MAP.containsKey(playerName);
    }

    /**
     * Checks that the Hunter-team is empty.
     *
     * @return boolean - True if the Hunter-team is empty, false otherwise.
     */
    public static synchronized boolean isHunterMapEmpty() {
        return ManHuntUtilities.HUNTER_MAP.isEmpty();
    }

    /**
     * Checks that the Runner-team is empty.
     *
     * @return boolean - True if the Runner-team is empty, false otherwise.
     */
    public static synchronized boolean isRunnerMapEmpty() {
        return ManHuntUtilities.RUNNER_MAP.isEmpty();
    }

    /**
     * Returns a Collection containing all the Player-objects in the HUNTER_MAP.
     *
     * @return hunters - Collection of all the Player-objects in the HUNTER_MAP.
     */
    public static synchronized Collection<Player> getHunters() {
        return ManHuntUtilities.HUNTER_MAP.values();
    }

    /**
     * Returns a Collection containing all the Player-objects in the RUNNER_MAP.
     *
     * @return runners - Collection of all the Player-objects in the RUNNER_MAP.
     */
    public static synchronized Collection<Player> getRunners() {
        return ManHuntUtilities.RUNNER_MAP.values();
    }

    /**
     * Removes the specified player from the Hunter-team.
     *
     * @param playerName - Username of the player that is to be removed from the Hunter-team.
     */
    public static synchronized void removeHunter(final String playerName) {
        ManHuntUtilities.HUNTER_MAP.remove(playerName);
    }

    /**
     * Removes the specified player from the Runner-team.
     *
     * @param playerName - Username of the player that is to be removed from the Runner-team.
     */
    public static synchronized void removeRunner(final String playerName) {
        ManHuntUtilities.RUNNER_MAP.remove(playerName);
    }

    /**
     * Checks whether or not the specified player is in the Runner-team.
     *
     * @param player - The Player-object of the player that is to be checked.
     * @return boolean - True if the specified player is in the Runner-team, false otherwise.
     */
    public static synchronized boolean isRunner(final Player player) {
        return ManHuntUtilities.RUNNER_MAP.containsValue(player);
    }

    /**
     * Checks whether or not the specified player is in the Hunter-team.
     *
     * @param playerName - The Player-object of the player that is to be checked.
     * @return - True if the specified player is in the Hunter-team, false otherwise.
     */
    public static synchronized boolean isRunner(final String playerName) {
        return ManHuntUtilities.RUNNER_MAP.containsKey(playerName);
    }

    /**
     * Removes the specified player from the Hunter-team.
     *
     * @param player - The Player-object of the player to be removed from the Hunter-team.
     * @return boolean - True if successfully removed from the Hunter-team, false otherwise.
     */
    public static synchronized boolean removePlayerFromHunters(final Player player) {
        if (ManHuntUtilities.HUNTER_MAP.containsValue(player)) {
            ManHuntUtilities.HUNTER_MAP.remove(player.getName());
            return true;
        }
        return false;
    }

    /**
     * Removes the specified player from the Runner-team.
     *
     * @param player - The Player-object of the player to be removed from the Runner-team.
     * @return boolean - True if successfully removed from the Runner-team, false otherwise.
     */
    public static synchronized boolean removePlayerFromRunners(final Player player) {
        if (ManHuntUtilities.RUNNER_MAP.containsValue(player)) {
            ManHuntUtilities.RUNNER_MAP.remove(player.getName());
            return true;
        }
        return false;
    }

    /**
     * Checks if the specified String contains letters.
     *
     * @param argument - The String to be checked.
     * @return boolean - True if the String contains letters, false otherwise.
     */
    public static boolean hasLetters(String argument) {
        for (int x = 0; x < argument.length(); x++) {
            if (Character.isLetter(argument.charAt(x))) return true;
        }
        return false;
    }

    public static void setFirstRun(final boolean isFirstRun) {
        ManHuntUtilities.IS_FIRST_RUN.set(true);
    }

}
