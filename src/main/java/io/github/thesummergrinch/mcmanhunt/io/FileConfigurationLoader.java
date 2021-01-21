package io.github.thesummergrinch.mcmanhunt.io;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
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

    public MCManHuntStringCache loadStrings() {
        MCManHuntStringCache stringCache = fileConfiguration.getObject("string-cache", MCManHuntStringCache.class);
        if (stringCache != null) return stringCache;
        MCManHuntStringCache.getInstance().addStringsToCache(new HashMap<String, String>() {
            {
                put("no-games-initialized", "No games have been initialized.");
                put("list-initialized-games", "The following games have been initialized: ");
                put("not-in-game-no-game-specified", "You are not in a game, and the specified game does not exist.");
                put("list-hunters", "The Hunter-team consists of: ");
                put("list-runners", "The Runner-team consists of: ");
                put("init-worlds", "Initializing worlds...");
                put("worlds-ready", "World initialized.");
                put("init-game", "Initializing Game...");
                put("game-ready", "Game initialized.");
                put("specified-game-not-exist", "The specified game does not exist.");
                put("game-resumed", "The game has been resumed.");
                put("game-not-paused", "The specified game is not paused.");
                put("game-stopping", "The ManHunt-game will be stopped...");
                put("universe-destroy-failed", "Could not destroy the specified Universe. The Universe may still be in use.");
                put("true", "true");
                put("false", "false");
                put("joined-game", "You've been added to the game!");
                put("join-team-failed", "You are not in registered to a game. Please join a game before using this command!");
                put("join-hunters", "joinhunters");
                put("hunters", "hunters");
                put("joined-hunters-message", " has joined the Hunters!");
                put("join-runners", "joinrunners");
                put("runners", "runners");
                put("joined-runners-message", " has joined the Runners!");
                put("join-team-incorrect-argument", "The command-argument is incorrect. Please use \"/jointeam hunters\"" +
                        " or \"/jointeam runners\".");
                put("version-message-part-one", "[MCManHunt] You are running version ");
                put("version-message-part-two", " of the MCManHunt-plugin.");
                put("win-message", "Congratulations! You won the MCManHunt-game!");
                put("tracker", " Tracker");
                put("game-start-intro", "The game will start in 5 seconds! The Runner-team gets a 30-second head-start!");
                put("runners-started", "The Runner-team has started!");
                put("hunters-started", "The Hunter-team has started!");
                put("game-paused", "The game has been paused and the difficulty has been set to peaceful.");
                put("game-resuming", "The game will resume in 5 seconds!");
                put("game-has-resumed", "The game has resumed!");
                put("game-has-stopped", "The game has stopped!");
            }
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
