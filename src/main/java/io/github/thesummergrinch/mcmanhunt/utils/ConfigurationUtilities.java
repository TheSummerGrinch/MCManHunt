package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public final class ConfigurationUtilities {

    // A cached version of the config.yml file.
    private static FileConfiguration fileConfiguration;

    /**
     * Gets the int-value associated with the given path in the config.yml file.
     *
     * @param path - Path of the requested Integer.
     * @return int - The int associated with the given path.
     */
    static int getInt(Plugin plugin, final String path) {
        if (fileConfiguration == null) loadConfig(plugin);
        return fileConfiguration.getInt(path);
    }

    /**
     * Gets the boolean-value associated with the given path in the config.yml file.
     *
     * @param path - Path of the requested boolean.
     * @return int - The Integer associated with the given path.
     */
    public static boolean getBoolean(Plugin plugin, final String path) {
        if (fileConfiguration == null) loadConfig(plugin);
        return fileConfiguration.getBoolean(path);
    }

    private static void saveOngoingGame() {
        final ArrayList<Player> hunters = new ArrayList<>(ManHuntUtilities.getHunters());
        final ArrayList<Player> runners = new ArrayList<>(ManHuntUtilities.getRunners());
        final ArrayList<String> hunterUUIDs = new ArrayList<>();
        final ArrayList<String> runnerUUIDs = new ArrayList<>();
        for (Player hunter : hunters) {
            hunterUUIDs.add(hunter.getUniqueId().toString());
        }
        for (Player runner : runners) {
            runnerUUIDs.add(runner.getUniqueId().toString());
        }
        fileConfiguration.set("game-ongoing", true);
        fileConfiguration.set("current-hunters", hunterUUIDs);
        fileConfiguration.set("current-runners", runnerUUIDs);
    }

    public static void saveConfig(final Plugin plugin) {
        fileConfiguration.set("max-runners", ManHuntUtilities.getMaxRunners());
        fileConfiguration.set("max-hunters", ManHuntUtilities.getMaxHunters());
        if (GameFlowUtilities.isGameInProgress()) saveOngoingGame();
        plugin.saveConfig();
    }

    public static void loadConfig(final Plugin plugin) {
        if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
            plugin.saveDefaultConfig();
            plugin.getLogger().log(Level.CONFIG, "No config.yml detected. Automatically generated the default " +
                    "config.yml.");
            ManHuntUtilities.setFirstRun();
        }
        fileConfiguration = plugin.getConfig();
        ManHuntUtilities.setMaxRunners(fileConfiguration.getInt("max-runners"));
        ManHuntUtilities.setMaxHunters(fileConfiguration.getInt("max-hunters"));
        if (fileConfiguration.getBoolean("game-ongoing")) {
            GameFlowUtilities.setGameInProgress(true);
            GameFlowUtilities.pauseGame();
            final ArrayList<String> hunterUUIDStrings = (ArrayList<String>) fileConfiguration
                    .getObject("current-hunters", ArrayList.class);
            final ArrayList<String> runnerUUIDStrings = (ArrayList<String>) fileConfiguration
                    .getObject("current-runners", ArrayList.class);
            for (String hunterUUIDString : hunterUUIDStrings) {
                ManHuntUtilities.addSavedGameHunter(UUID.fromString(hunterUUIDString));
            }
            for (String runnerUUIDString : runnerUUIDStrings) {
                ManHuntUtilities.addSavedGameRunner(UUID.fromString(runnerUUIDString));
            }
        }
    }

}
