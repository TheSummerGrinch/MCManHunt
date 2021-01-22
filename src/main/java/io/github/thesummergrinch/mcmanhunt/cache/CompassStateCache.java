package io.github.thesummergrinch.mcmanhunt.cache;

import io.github.thesummergrinch.mcmanhunt.game.players.compasses.CompassState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CompassStateCache {

    private static volatile CompassStateCache instance;

    private final HashMap<UUID, CompassState> cacheMap;

    private CompassStateCache() {
        this.cacheMap = new HashMap<>();
    }

    public static CompassStateCache getInstance() {
        CompassStateCache compassStateCache = instance;
        if (compassStateCache != null) return compassStateCache;
        synchronized (CompassStateCache.class) {
            if (instance == null) instance = new CompassStateCache();
            return instance;
        }
    }

    public void cacheCompassState(final UUID playerTrackedUUID, final CompassState compassState) {
        this.cacheMap.put(playerTrackedUUID, compassState);
    }

    public @Nullable CompassState getCompassState(final UUID playerTrackedUUID) {
        return this.cacheMap.get(playerTrackedUUID);
    }

}
