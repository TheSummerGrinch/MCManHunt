package io.github.thesummergrinch.mcmanhunt.cache;

import io.github.thesummergrinch.mcmanhunt.game.Game;
import io.github.thesummergrinch.mcmanhunt.io.FileConfigurationLoader;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class GameCache implements ConfigurationSerializable {

    private static final HashMap<String, Game> gameCache = new HashMap<>();
    private static volatile GameCache instance;

    private GameCache() {
    }

    public static GameCache getInstance() {
        GameCache gameCache = instance;
        if (gameCache != null) return gameCache;
        synchronized (GameCache.class) {
            if (instance == null) instance = new GameCache();
        }
        return instance;
    }

    public static GameCache deserialize(Map<String, Object> objects) {
        instance = getInstance();
        HashMap<String, Game> savedGames = (HashMap<String, Game>) objects.get("game-cache");
        gameCache.putAll(savedGames);
        return getInstance();
    }

    public static Set<String> getGameNames() {
        return gameCache.keySet();
    }

    public void cacheGame(final String gameName, final Game game) {
        this.gameCache.put(gameName, game);
    }

    @Nullable
    public Game getGameFromCache(final String gameName) {
        return this.gameCache.get(gameName);
    }

    public Set<Map.Entry<String, Game>> getAllGames() {
        return this.gameCache.entrySet();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> gameCache = new HashMap<>();
        gameCache.put("game-cache", this.gameCache);
        return gameCache;
    }

    public void getGameCacheFromSave() {
        instance = FileConfigurationLoader.getInstance().loadGames();
        GameCache.getInstance().getAllGames().forEach(entry -> {
            gameCache.put(entry.getKey(), entry.getValue());
        });
    }

    public void removeGame(@NotNull final String gameName) {
        gameCache.remove(gameName);
    }

}