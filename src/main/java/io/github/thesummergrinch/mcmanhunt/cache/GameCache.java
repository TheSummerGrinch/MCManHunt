package io.github.thesummergrinch.mcmanhunt.cache;

import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.io.settings.FileConfigurationLoader;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Cache for {@link Game}-objects, used to make {@link Game}-objects and their
 * corresponding
 * {@link io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameState}
 * -objects more accessible and reusable.
 */
public final class GameCache implements ConfigurationSerializable {

    // The internal cache
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

    /**
     * Used by the server to deserialize a stored {@link GameCache}
     * -representation back into a {@link GameCache}-object. The
     * {@link GameCache}-representation is generally stored in the config.yml.
     * @param objects - YAML objects
     * @return a GameCache-object that represents the data present in the
     * config.yml.
     */
    public static GameCache deserialize(Map<String, Object> objects) {

        instance = getInstance();
        HashMap<String, Game> savedGames = (HashMap<String, Game>) objects.get("game-cache");

        gameCache.putAll(savedGames);

        return getInstance();

    }

    /**
     * Gets all the keys stored in the {@link #gameCache}, and
     * returns them as a {@code Set<String>}.
     * @return a Set of Strings.
     */
    public static Set<String> getGameNames() {

        return gameCache.keySet();

    }

    /**
     * Maps a String representing the name of a {@link Game}, and stores them
     * in the {@link #gameCache}.
     * @param gameName - name of a game
     * @param game - {@link Game}-object
     */
    public void cacheGame(final String gameName, final Game game) {

        gameCache.put(gameName, game);

    }

    /**
     * Retrieves a {@link Game}-object from the {@link #gameCache},
     * using the given String as a key.
     * @param gameName - the key
     * @return - {@link Game}-object
     */
    @Nullable
    public Game getGameFromCache(final String gameName) {

        return gameCache.get(gameName);

    }

    /**
     * Retrieves all entries in the {@link #gameCache} and returns them as a
     * {@code Set<Map#Entry<String, Game>>}-object.
     * @return - a {@link Set} of {@link java.util.Map.Entry}-objects
     */
    public Set<Map.Entry<String, Game>> getAllGames() {

        return gameCache.entrySet();

    }

    /**
     * Creates a {@link HashMap}, adds an entry ("game-cache",
     * {@link #gameCache}), and returns the Map.
     * @return a {@link Map} containing the {@link #gameCache}
     */
    @Override
    public @NotNull Map<String, Object> serialize() {

        HashMap<String, Object> gameCache = new HashMap<>();

        gameCache.put("game-cache", GameCache.gameCache);

        return gameCache;

    }

    /**
     * Loads a {@link Game}-object into the {@link #gameCache}, using a value
     * stored in the {@link org.bukkit.configuration.file.FileConfiguration}.
     * This {@link Game} is retrieved using the given String as the key.
     * @param key - String key
     */
    public void getGameCacheFromSave(final String key) {

        instance = FileConfigurationLoader.getInstance().loadGames(key);
        GameCache.getInstance().getAllGames().forEach(entry -> gameCache.put(entry.getKey(), entry.getValue()));

    }

    /**
     * Removes a {@link Game} from the {@link #gameCache}.
     * @param gameName - String gameName
     */
    public void removeGame(@NotNull final String gameName) {

        gameCache.remove(gameName);

    }

    /**
     * Retrieves all the keys in the {@link #gameCache}, and returns them as
     * a {@link List}.
     * @return a {@link List} containing the keys of the {@link #gameCache}
     */
    public List<String> getGameNamesAsList() {

        return new ArrayList<>(gameCache.keySet());

    }

    /**
     * Retrieves of the game names of games that have not started yet.
     * @return List of standby game names.
     */
    public List<String> getStandbyGameNamesAsList() {

        final List<String> gameNames = new ArrayList<>();

        gameCache.forEach((name, game) -> {

            if (game.getGameFlowState().equals(GameFlowState.DEFAULT)) gameNames.add(name);

        });

        return gameNames;

    }

}
