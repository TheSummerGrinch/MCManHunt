package io.github.thesummergrinch.mcmanhunt.io;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public final class FileConfigurationLoader {

    private static volatile FileConfigurationLoader instance;

    private FileConfiguration fileConfiguration;

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

    public MCManHuntStringCache loadStrings() {
        MCManHuntStringCache stringCache = fileConfiguration.getObject("string-cache", MCManHuntStringCache.class);
        if (stringCache != null) return stringCache;
        MCManHuntStringCache.getInstance().addStringsToCache(new HashMap<String, String>() {
            {}
        });
        fileConfiguration.set("string-cache", MCManHuntStringCache.getInstance());
        return MCManHuntStringCache.getInstance();
    }

    public void saveGames() {
        fileConfiguration.set("game-cache", GameCache.getInstance());
        MCManHunt.getPlugin(MCManHunt.class).saveConfig();
    }

    public FileConfiguration getFileConfiguration() {
        return this.fileConfiguration;
    }

    public GameCache loadGames() {
        GameCache gameCache = fileConfiguration.getObject("game-cache", GameCache.class);
        return gameCache;
    }

}
