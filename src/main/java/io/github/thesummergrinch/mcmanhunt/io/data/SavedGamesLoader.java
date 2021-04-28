package io.github.thesummergrinch.mcmanhunt.io.data;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class SavedGamesLoader {

    private static volatile SavedGamesLoader INSTANCE;

    private final FileConfiguration savedGamesFileConfiguration;
    private final Plugin plugin;

    private File dataFile;

    private SavedGamesLoader() {

        this.plugin = MCManHunt.getPlugin(MCManHunt.class);

        try {
            this.dataFile = createDataFile(this.plugin);
        } catch (IOException exception) {
            this.plugin.getLogger().severe("Could not create saved-game file." +
                    " Please contact developer: https://www.github" +
                    ".com/TheSummerGrinch");
        }

        this.savedGamesFileConfiguration =
                YamlConfiguration.loadConfiguration(this.dataFile);

    }

    public static SavedGamesLoader getInstance() {
        if (SavedGamesLoader.INSTANCE != null) return INSTANCE;
        synchronized (SavedGamesLoader.class) {
            if (SavedGamesLoader.INSTANCE == null) SavedGamesLoader.INSTANCE
                    = new SavedGamesLoader();
            return INSTANCE;
        }
    }

    private File createDataFile(final Plugin plugin) throws IOException {
        final File pluginDataFolder = plugin.getDataFolder();

        if (!pluginDataFolder.exists()) {
            pluginDataFolder.mkdir();
        }

        final File gameDataFolder =
                new File(pluginDataFolder + File.separator + "GameData");

        if (!gameDataFolder.exists()) {
            gameDataFolder.mkdir();
        }

        final File gameDataFile = new File(gameDataFolder + File.separator +
                "SavedGames.yml");

        if (!gameDataFile.exists() || !gameDataFile.isFile()) {
            gameDataFile.createNewFile();
        }
        return gameDataFile;
    }

    public void saveGameCache(final GameCache gameCache) {
        this.savedGamesFileConfiguration.set("saved-games", gameCache);
        try {
            this.savedGamesFileConfiguration.save(this.dataFile);
        } catch (IOException exception) {
            this.plugin.getLogger().severe("Could not save games... Please " +
                    "contact developer: https://www.github" +
                    ".com/TheSummerGrinch");
        }
    }

    public GameCache loadSavedGames(final String key) {
        return this.savedGamesFileConfiguration.getObject(key, GameCache.class);
    }

}
