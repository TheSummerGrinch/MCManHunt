package io.github.thesummergrinch.mcmanhunt.io.settings;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public final class FileConfigurationLoader {

    private static volatile FileConfigurationLoader instance;

    private final FileConfiguration fileConfiguration;

    private FileConfigurationLoader() {

        fileConfiguration = MCManHunt.getPlugin(MCManHunt.class).getConfig();

    }

    public static FileConfigurationLoader getInstance() {

        FileConfigurationLoader loader = instance;

        if (loader != null) return loader;

        synchronized (FileConfigurationLoader.class) {

            if (instance == null) instance = new FileConfigurationLoader();

            return instance;

        }
    }

    public void saveItemToConfig(final String key, final Object value) {

        FileConfigurationLoader.getInstance().fileConfiguration.set(key, value);

    }

    public GameCache loadGames(final String key) {

        return fileConfiguration.getObject(key, GameCache.class);

    }

    public DefaultSettingsContainer loadDefaultSettings(final String key) {

        DefaultSettingsContainer defaultSettingsContainer = fileConfiguration.getObject(key, DefaultSettingsContainer.class);

        if (defaultSettingsContainer != null) return DefaultSettingsContainer.getInstance();

        DefaultSettingsContainer.getInstance().setSettings(getDefaultSettings());
        this.fileConfiguration.set("settings", DefaultSettingsContainer.getInstance());

        return DefaultSettingsContainer.getInstance();

    }

    public HashMap<String, String> getDefaultSettings() {

        return new HashMap<String, String>() {
            {
                put("first-run", "true");
                put("allow-metrics", "true");
                put("compass-enabled-in-nether", "false");
                put("player-roles-randomized", "false");
                put("default-headstart", "30");
                put("enable-update-checking", "true");
                put("locale", "enGB");
                put("bungeecord-enabled", "false");
                put("bungeecord-hub-name", "hub");
                put("clear-advancements-after-game", "false");
            }
        };

    }
}
