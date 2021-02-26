package io.github.thesummergrinch.mcmanhunt.cache;

import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a structure containing the data of all {@link PlayerState}
 * -objects created in that session, as well as the {@link PlayerState}
 * -objects loaded from disk.
 */
public final class PlayerStateCache implements ConfigurationSerializable {

    private static volatile PlayerStateCache instance;

    // The internal cache
    private final HashMap<UUID, PlayerState> playerStateHashMap;

    private PlayerStateCache() {
        this.playerStateHashMap = new HashMap<>();
    }

    public static PlayerStateCache getInstance() {
        PlayerStateCache playerStateCache = instance;
        if (playerStateCache != null) return playerStateCache;
        synchronized (PlayerStateCache.class) {
            if (instance == null) instance = new PlayerStateCache();
            return instance;
        }
    }

    /**
     * Maps a {@link UUID} to a {@link PlayerState}, and stores them in
     * {@link #playerStateHashMap}.
     * @param playerUUID {@link UUID}
     * @param playerState {@link PlayerState}
     */
    public void cachePlayerState(final UUID playerUUID, final PlayerState playerState) {
        this.playerStateHashMap.put(playerUUID, playerState);
    }

    /**
     * Retrieves a PlayerState from the {@link #playerStateHashMap}, using
     * the given {@link UUID} as the key.
     * @param playerUUID {@link UUID}
     * @return a {@link PlayerState}
     */
    @Nullable
    public PlayerState getPlayerState(final UUID playerUUID) {
        return this.playerStateHashMap.get(playerUUID);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return null;
    }

}
