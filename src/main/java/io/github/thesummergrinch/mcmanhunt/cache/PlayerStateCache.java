package io.github.thesummergrinch.mcmanhunt.cache;

import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.configuration.file.YamlConfiguration;
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

    private final YamlConfiguration yamlConfiguration = new YamlConfiguration();

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
     * @param playerUUID - {@link UUID}
     * @param playerState - {@link PlayerState}
     */
    public void cachePlayerState(final UUID playerUUID, final PlayerState playerState) {

        this.playerStateHashMap.put(playerUUID, playerState);

    }

    /**
     * Retrieves a PlayerState from the {@link #playerStateHashMap}, using
     * the given {@link UUID} as the key.
     * @param playerUUID - {@link UUID}
     * @return a {@link PlayerState}
     */
    @Nullable
    public PlayerState getPlayerState(final UUID playerUUID) {

        return this.playerStateHashMap.get(playerUUID);

    }

    /**
     * Serialized the {@link PlayerStateCache} conforming to the norms set by
     * {@link ConfigurationSerializable}.
     * @return A Map containing the serialized PlayerStateCache.
     */
    @Override
    public @NotNull Map<String, Object> serialize() {

        final Map<String, Object> playerStateObjects = new HashMap<>();

        this.playerStateHashMap.forEach((uuid, playerState) -> {

            playerStateObjects.put(uuid.toString(), playerState);

        });

        return playerStateObjects;
    }

    /**
     * Deserializes the given objects to a UUID,PlayerState pair.
     * @param objects - serialized objects
     * @return {@link PlayerStateCache} with the deserialized entries.
     */
    public static PlayerStateCache deserialize(final Map<String, Object> objects) {

        PlayerStateCache playerStateCache = PlayerStateCache.getInstance();

        objects.keySet().forEach(uuidString -> {

            playerStateCache.cachePlayerState(UUID.fromString(uuidString),
                    (PlayerState) objects.get(uuidString));

        });

        return PlayerStateCache.getInstance();
    }

}
