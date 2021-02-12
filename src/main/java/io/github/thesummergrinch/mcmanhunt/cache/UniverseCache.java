package io.github.thesummergrinch.mcmanhunt.cache;

import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UniverseCache implements ConfigurationSerializable {

    private static volatile UniverseCache instance;

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

    public void cacheUniverse(final String universeName, final Universe universe) {

        this.universeCache.put(universeName, universe);

    }

    public @Nullable Universe getUniverse(final String universeName) {

        return this.universeCache.get(universeName);

    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return null;
    }

    public void removeUniverse(@NotNull final String universeName) {

        this.universeCache.remove(universeName);

    }

    @Deprecated
    public void onDisable() {

        universeCache.values().forEach(universe -> {

            if (universe.getMarkedForDestruction()) {

                universe.destroyUniverse();

            }

        });
    }

    public List<String> getUniverseNamesAsList() {

        return new ArrayList<>(universeCache.keySet());

    }

}
