package io.github.thesummergrinch.mcmanhunt.game.players.compasses;

import io.github.thesummergrinch.mcmanhunt.cache.CompassStateCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import org.bukkit.inventory.meta.CompassMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class CompassState {

    private final CompassMeta compassMeta;
    private final UUID playerUUID;

    public CompassState(@NotNull UUID playerUUID, @NotNull final CompassMeta compassMeta) {
        this.playerUUID = playerUUID;
        this.compassMeta = compassMeta;
        CompassStateCache.getInstance().cacheCompassState(playerUUID, this);
    }

    public CompassMeta getCompassMeta() {
        updatePlayerLocation();
        return this.compassMeta;
    }

    public CompassMeta updatePlayerLocation() {
        if (PlayerStateCache.getInstance().getPlayerState(playerUUID).getLastKnownLocation() != null) {
            compassMeta.setLodestone(PlayerStateCache.getInstance().getPlayerState(playerUUID).getLastKnownLocation());
        }
        return this.compassMeta;
    }

}
