package io.github.thesummergrinch.mcmanhunt.io;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.game.GameController;
import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;

public class FileConfigurationLoader {

    private static volatile FileConfigurationLoader instance;

    private FileConfiguration fileConfiguration;

    private FileConfigurationLoader() {
        this.fileConfiguration = getFileConfiguration();
    }

    public static FileConfigurationLoader getInstance() {
        FileConfigurationLoader fileConfigurationLoader = instance;
        if (fileConfigurationLoader != null) return fileConfigurationLoader;
        synchronized (FileConfigurationLoader.class) {
            if (instance == null) {
                instance = new FileConfigurationLoader();
            }
            return instance;
        }
    }

    private synchronized FileConfiguration getFileConfiguration() {
        Plugin plugin = MCManHunt.getPlugin(MCManHunt.class);
        if (!new File(plugin.getDataFolder(), "config.yml").exists()) plugin.saveDefaultConfig();
        return plugin.getConfig();
    }

    public synchronized int getInt(final String path) {
        return fileConfiguration.getInt(path);
    }

    public synchronized boolean getBoolean(final String path) {
        return fileConfiguration.getBoolean(path);
    }

    public synchronized <T> T getObject(final String path, final Class<T> type) {
        return fileConfiguration.getObject(path, type);
    }

    public synchronized void saveConfig() {
        GameController gameController = GameController.getInstance();
        fileConfiguration.set("max-hunters", gameController.getMaxHunters());
        fileConfiguration.set("max-runners", gameController.getMaxRunners());
        if (!gameController.getGameState().equals(GameController.GameState.DEFAULT)) {
            saveOngoingGame();
        } else {
            fileConfiguration.set("game-ongoing", false);
            fileConfiguration.set("current-hunters", new ArrayList<String>());
            fileConfiguration.set("current-runners", new ArrayList<String>());
        }
        MCManHunt.getPlugin(MCManHunt.class).saveConfig();
    }

    private void saveOngoingGame() {
        final ArrayList<String> hunterUUIDs = new ArrayList<>();
        final ArrayList<String> runnerUUIDs = new ArrayList<>();
        UserCache.getInstance().getHunters().forEach(playerState -> hunterUUIDs.add(playerState.getUUID().toString()));
        UserCache.getInstance().getRunners().forEach(playerState -> runnerUUIDs.add(playerState.getUUID().toString()));
        fileConfiguration.set("game-ongoing", true);
        fileConfiguration.set("current-hunters", hunterUUIDs);
        fileConfiguration.set("current-runners", runnerUUIDs);
    }

}
