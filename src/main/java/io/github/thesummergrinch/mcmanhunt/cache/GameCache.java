package io.github.thesummergrinch.mcmanhunt.cache;

import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.io.settings.FileConfigurationLoader;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Cache for {@link Game}-objects, used to make {@link Game}-objects and their corresponding
 * {@link io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameState}-objects more accessible and reusable.
 */
public final class GameCache implements ConfigurationSerializable {

    private static final HashMap<String, Game> gameCache = new HashMap<>();
    private static volatile GameCache instance;

    private GameCache() {
    }

    // Singleton-pattern
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
        gameCache.put(gameName, game);
    }

    @Nullable
    public Game getGameFromCache(final String gameName) {
        return gameCache.get(gameName);
    }

    public Set<Map.Entry<String, Game>> getAllGames() {
        return gameCache.entrySet();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> gameCache = new HashMap<>();
        gameCache.put("game-cache", GameCache.gameCache);
        return gameCache;
    }

    public void getGameCacheFromSave(final String key) {
        instance = FileConfigurationLoader.getInstance().loadGames(key);
        GameCache.getInstance().getAllGames().forEach(entry -> gameCache.put(entry.getKey(), entry.getValue()));
    }

    public void removeGame(@NotNull final String gameName) {
        gameCache.remove(gameName);
    }

}
