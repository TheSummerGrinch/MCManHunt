package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;

public final class ConfigurationUtilities {

    private static FileConfiguration fileConfiguration;

    public static FileConfiguration getConfiguration(boolean forcedUpdate) {
        if (!ManHuntUtilities.getManHuntPlugin().getDataFolder().exists()) {
            generateDefaultConfiguration();
            ManHuntUtilities.setFirstRun(true);
            ManHuntUtilities.getManHuntPlugin().getLogger().log(Level.INFO, "A config.yml was automatically " +
                    "generated.");
        }
        if(fileConfiguration == null || forcedUpdate) {
            fileConfiguration = ManHuntUtilities.getManHuntPlugin().getConfig();
        }
        return fileConfiguration;
    }

    private static void generateDefaultConfiguration() {
        ManHuntUtilities.getManHuntPlugin().saveDefaultConfig();
    }

    public static int getInt(final String path) {
        if (fileConfiguration == null) getConfiguration(false);
        return getConfiguration(false).getInt(path);
    }

    public static boolean getBoolean(final String path) {
        if (fileConfiguration == null) getConfiguration(false);
        return getConfiguration(false).getBoolean(path);
    }

}
