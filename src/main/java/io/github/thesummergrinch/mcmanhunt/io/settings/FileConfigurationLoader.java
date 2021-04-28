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

    /**
     * Saves the given object in the Plugin's {@link FileConfiguration}.
     * @param key - the path to the object
     * @param value - the object to be stored in the {@link FileConfiguration}
     */
    public void saveItemToConfig(final String key, final Object value) {

        FileConfigurationLoader.getInstance().fileConfiguration.set(key, value);

    }

    /**
     * Loads a {@link GameCache} from the config.yml.
     * @param key - path to the serialized {@link GameCache} object.
     * @return - the {@link GameCache}-object stored in the config.yml.
     */
    @Deprecated
    public GameCache loadGames(final String key) {

        return fileConfiguration.getObject(key, GameCache.class);

    }

    /**
     * Loads settings from the config.yml located in the Plugin's datafolder.
     * @param key
     * @return
     */
    public DefaultSettingsContainer loadDefaultSettings(final String key) {

        DefaultSettingsContainer defaultSettingsContainer = fileConfiguration.getObject(key, DefaultSettingsContainer.class);

        if (defaultSettingsContainer != null) {

            if (defaultSettingsContainer.getInteger("config-version") < (int) this.getDefaultSettings().get("config-version")) {
                this.getDefaultSettings().forEach((name, value) -> {
                    if (!defaultSettingsContainer.contains(name))
                        defaultSettingsContainer.setSetting(name, value);
                });
            }

            return DefaultSettingsContainer.getInstance();
        }

        DefaultSettingsContainer.getInstance().setSettings(getDefaultSettings());
        this.fileConfiguration.set("settings", DefaultSettingsContainer.getInstance());

        return DefaultSettingsContainer.getInstance();

    }

    /**
     * Invoked when no config.yml can be found in the Plugin's datafolder.
     * Returns a {@link HashMap} of default settings to be used until the next
     * launch.
     * @return - the {@link HashMap} containing default settings.
     */
    public HashMap<String, Object> getDefaultSettings() {

        return new HashMap<String, Object>() {
            {
                put("config-version", 1);
                put("first-run", true);
                put("allow-metrics", true);
                put("compass-enabled-in-nether", false);
                put("player-roles-randomized", false);
                put("default-headstart", 30);
                put("enable-update-checking", true);
                put("locale", "enGB");
                put("bungeecord-enabled", false);
                put("bungeecord-hub-name", "hub");
                put("clear-advancements-after-game", false);
            }
        };

    }
}
