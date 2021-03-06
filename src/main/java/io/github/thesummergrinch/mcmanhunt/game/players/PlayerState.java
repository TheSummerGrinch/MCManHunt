package io.github.thesummergrinch.mcmanhunt.game.players;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PlayerState implements ConfigurationSerializable {

    private final UUID playerUUID;
    private final AtomicBoolean isMovementRestricted;

    private PlayerRole playerRole;

    @Nullable
    private Game game;
    @Nullable
    private Location lastKnownLocation;

    public PlayerState(@NotNull final UUID playerUUID) {

        this.playerUUID = playerUUID;
        this.isMovementRestricted = new AtomicBoolean(false);
        this.playerRole = PlayerRole.DEFAULT;
        this.game = null;

        PlayerStateCache.getInstance().cachePlayerState(this.playerUUID, this);

    }

    public static @NotNull PlayerState deserialize(@NotNull final Map<String, Object> objects) {

        final PlayerState playerState = new PlayerState(UUID.fromString((String) objects.get("uuid")));

        playerState.setPlayerRole(PlayerRole.fromString((String) objects.get("player-role")));
        playerState.setIsMovementRestricted((Boolean) objects.get("is-movement-restricted"));

        return playerState;

    }

    public UUID getPlayerUUID() {

        return this.playerUUID;

    }

    public PlayerRole getPlayerRole() {

        return this.playerRole;

    }

    public void setPlayerRole(final PlayerRole playerRole) {

        this.playerRole = playerRole;

        if (this.playerRole.equals(PlayerRole.DEFAULT)) return;

        if (isOnline()) {

            Bukkit.getPlayer(this.playerUUID).sendMessage(
                    (this.playerRole.equals(PlayerRole.RUNNER))
                            ? LanguageFileLoader.getInstance().getString("added-to-runners")
                            : LanguageFileLoader.getInstance().getString("added-to-hunters")
            );

        }
    }

    public boolean isOnline() {

        return Bukkit.getOnlinePlayers().stream().anyMatch(player -> player.getUniqueId().equals(playerUUID));

    }

    public Location getLastKnownLocation() {

        if (this.isOnline()) {

            final Location currentLocation = Bukkit.getPlayer(playerUUID).getLocation();

            if (this.game.isCompassEnabledInNether()) {

                this.lastKnownLocation = currentLocation;

            } else {

                this.lastKnownLocation = (currentLocation.getWorld().getEnvironment().equals(World.Environment.NORMAL)
                        || currentLocation.getWorld().getEnvironment().equals(World.Environment.THE_END))
                        ? currentLocation : this.lastKnownLocation;

            }
        }

        return this.lastKnownLocation;

    }

    public boolean isMovementRestricted() {

        return this.isMovementRestricted.get();

    }

    public void setIsMovementRestricted(final boolean isMovementRestricted) {

        this.isMovementRestricted.set(isMovementRestricted);

    }

    public boolean isInGame() {

        return (this.game != null);

    }

    public void setGame(@Nullable final Game game) {

        this.game = game;

    }

    @Nullable
    public String getGameName() {

        return (this.isInGame()) ? this.game.getName() : null;

    }

    @Override
    public @NotNull Map<String, Object> serialize() {

        final HashMap<String, Object> objects = new HashMap<>();

        objects.put("uuid", this.playerUUID.toString());
        objects.put("is-movement-restricted", this.isMovementRestricted.get());
        objects.put("player-role", this.playerRole.toString());

        return objects;

    }

    public String getPlayerName() {

        return Bukkit.getOfflinePlayer(this.playerUUID).getName();

    }

    public void leaveGame() {

        if (!this.isInGame()) return;

        if (this.isOnline()) {

            Player player = Bukkit.getPlayer(this.playerUUID);

            player.setGameMode(GameMode.SURVIVAL);
            player.setFoodLevel(20);
            player.setHealth(20);
            player.getInventory().clear();

            this.setPlayerRole(PlayerRole.DEFAULT);

            if (!GameCache.getInstance().getGameFromCache(this.getGameName()).getGameFlowState().equals(GameFlowState.DEFAULT)) {

                player.teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);

            }

            this.isMovementRestricted.set(false);

            GameCache.getInstance().getGameFromCache(this.getGameName()).removePlayerFromGame(this.getPlayerUUID());

        }
    }
}
