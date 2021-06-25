package io.github.thesummergrinch.mcmanhunt.cache;

import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a structure containing the data off all {@link Universe}
 * -objects created during a session, as well as, all {@link Universe}
 * -objects loaded from the config.
 */
public final class UniverseCache implements ConfigurationSerializable {

    private static volatile UniverseCache instance;

    // Internal cache
    private final HashMap<String, Universe> universeCache;

    private UniverseCache() {
        this.universeCache = new HashMap<>();
    }

    public static UniverseCache getInstance() {

        UniverseCache universeCache = instance;

        if (universeCache != null) return universeCache;

        synchronized (UniverseCache.class) {

            if (instance == null) instance = new UniverseCache();

            return instance;

        }
    }

    /**
     * Maps a String to a {@link Universe}-object, and stores them in
     * {@link #universeCache}.
     * @param universeName String
     * @param universe {@link Universe}
     */
    public void cacheUniverse(final String universeName, final Universe universe) {

        this.universeCache.put(universeName, universe);

    }

    /**
     * Retrieves a {@link Universe}, using the given String as the key.
     * @param universeName String
     * @return a {@link Universe}
     */
    public @Nullable Universe getUniverse(final String universeName) {

        return this.universeCache.get(universeName);

    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return null;
    }

    /**
     * Removes a {@link Universe} from the {@link #universeCache}, using the
     * given String as the key.
     * @param universeName String
     */
    public void removeUniverse(@NotNull final String universeName) {

        this.universeCache.remove(universeName);

    }

    /**
     * Destroys {@link Universe}-objects and deletes their corresponding
     * world-files, if the {@link Universe} in question has been marked for
     * destruction.
     */
    public void onDisable() {
        universeCache.values().forEach(Universe::unloadAndDestroy);
    }

    /**
     * Retrieves all the keys of the {@link #universeCache}, and returns them
     * as a {@link List}
     * @return a {@link List} containing all keys stored in the
     * {@link #universeCache}
     */
    public List<String> getUniverseNamesAsList() {

        return new ArrayList<>(universeCache.keySet());

    }

}
