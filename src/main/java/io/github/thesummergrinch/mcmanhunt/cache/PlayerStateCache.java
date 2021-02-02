package io.github.thesummergrinch.mcmanhunt.cache;

import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerStateCache implements ConfigurationSerializable {

    private static volatile PlayerStateCache instance;

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

    public void cachePlayerState(final UUID playerUUID, final PlayerState playerState) {
        this.playerStateHashMap.put(playerUUID, playerState);
    }

    public PlayerState getPlayerState(final UUID playerUUID) {
        return this.playerStateHashMap.get(playerUUID);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return null;
    }

}
