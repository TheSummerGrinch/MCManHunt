package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public final class ConfigurationUtilities {

    // A cached version of the config.yml file.
    private static FileConfiguration fileConfiguration;

    /**
     * Loads - or reload - FileConfiguration from the config.yml file in the plugin's datafolder.
     * @param forcedUpdate - If true, the config.yml will be reloaded and re-cached. If false, the current cached version will be returned.
     * @return The FileConfiguration stored in the fileConfiguration-variable or loaded from the config.yml file.
     */
    public static FileConfiguration getConfiguration(boolean forcedUpdate) {
        if (!ManHuntUtilities.getManHuntPlugin().getDataFolder().exists()) {
            generateDefaultConfiguration();
            ManHuntUtilities.setFirstRun(true);
            ManHuntUtilities.getManHuntPlugin().getLogger().log(Level.INFO, "A config.yml was automatically " +
                    "generated.");
        }
        if (fileConfiguration == null || forcedUpdate) {
            fileConfiguration = ManHuntUtilities.getManHuntPlugin().getConfig();
        }
        return fileConfiguration;
    }

    /**
     * Generates the default config.yml file, in the plugin's datafolder.
     */
    private static void generateDefaultConfiguration() {
        ManHuntUtilities.getManHuntPlugin().saveDefaultConfig();
    }

    /**
     * Gets the int-value associated with the given path in the config.yml file.
     * @param path - Path of the requested Integer.
     * @return int - The int associated with the given path.
     */
    public static int getInt(final String path) {
        if (fileConfiguration == null) getConfiguration(false);
        return getConfiguration(false).getInt(path);
    }

    /**
     * Gets the boolean-value associated with the given path in the config.yml file.
     * @param path - Path of the requested boolean.
     * @return int - The Integer associated with the given path.
     */
    public static boolean getBoolean(final String path) {
        if (fileConfiguration == null) getConfiguration(false);
        return getConfiguration(false).getBoolean(path);
    }

}
