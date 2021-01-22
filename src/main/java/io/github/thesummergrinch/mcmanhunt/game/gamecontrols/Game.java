package io.github.thesummergrinch.mcmanhunt.game.gamecontrols;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.CompassStateCache;
import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.game.players.compasses.CompassMetaBuilder;
import io.github.thesummergrinch.mcmanhunt.game.players.compasses.CompassState;
import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Game implements ConfigurationSerializable {

    @NotNull
    private final GameState gameState;

    public Game(@NotNull final Universe gameUniverse) {
        this.gameState = new GameState(gameUniverse);
        GameCache.getInstance().cacheGame(gameState.getGameName(), this);
    }

    public Game(@NotNull final Universe gameUniverse, @NotNull final Difficulty defaultGameDifficulty) {
        this.gameState = new GameState(gameUniverse, defaultGameDifficulty);
        GameCache.getInstance().cacheGame(gameState.getGameName(), this);
    }

    private Game(@NotNull GameState gameState) {
        this.gameState = gameState;
        this.gameState.linkPlayerStatesToGameObject(this);
        GameCache.getInstance().cacheGame(gameState.getGameName(), this);
    }

    /**
     * Reconstructs a {@link Game}-object from a yml-representation, as specified by {@link #serialize()}.
     * @param objects - {@link Map} constructed from the yml-representation of the {@link Game}-object.
     * @return the reconstructed {@link Game}-object.
     */
    @SuppressWarnings("unused")
    public static @NotNull Game deserialize(final Map<String, Object> objects) {
        return new Game((GameState) objects.get("game-state"));
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> objects = new HashMap<>();
        objects.put("game-state", this.gameState);
        return objects;
    }

    public void addPlayerToGame(@NotNull final UUID playerUUID) {
        this.gameState.addPlayerToGame(this, playerUUID);
    }

    public void removePlayerFromGame(@NotNull final UUID playerUUID) {
        this.gameState.removePlayerFromGame(playerUUID);
    }

    public @NotNull String getName() {
        return gameState.getGameName();
    }

    /**
     * Starts a ManHunt-game in the following sequence:
     * 1. Set the {@link GameFlowState} within the {@link GameState} to RUNNING.
     * 2. Reset the time of the Gameworld, to ensure a decent start.
     * 3. Teleport players to ManHunt-overworld.
     * 4. Restrict player movement and damage.
     * 5. Allow runners to move, and give them a 30 second headstart.
     * 6. Allow hunters to move.
     */
    public void start() {
        gameState.setGameFlowState(GameFlowState.RUNNING);
        gameState.setGameUniverseTime(1000L);
        gameState.getPlayersInGame().forEach((uuid, playerState) -> {
            if (playerState.isOnline()) {
                final Player player = Bukkit.getPlayer(playerState.getPlayerUUID());
                player.setBedSpawnLocation(gameState.getWorldSpawn(), true);
                player.teleport(gameState.getWorldSpawn(), PlayerTeleportEvent.TeleportCause.COMMAND);
            }
        });
        broadcastToPlayers(MCManHuntStringCache.getInstance().getStringFromCache(ChatColor.GREEN + "game-start-intro"));
        new BukkitRunnable() {
            @Override
            public void run() {
                gameState.getPlayersInGame().forEach((uuid, playerState) -> playerState.setIsMovementRestricted(true));
            }
        }.runTaskAsynchronously(MCManHunt.getPlugin(MCManHunt.class));
        getHunters().forEach(hunter -> giveHunterCompasses(hunter.getPlayerUUID()));
        new BukkitRunnable() {
            @Override
            public void run() {
                gameState.getPlayersInGame().values().forEach(playerState -> {
                    if (playerState.getPlayerRole().equals(PlayerRole.RUNNER)) {
                        playerState.setIsMovementRestricted(false);
                    }
                });
            }
        }.runTaskLaterAsynchronously(MCManHunt.getPlugin(MCManHunt.class), 100);
        new BukkitRunnable() {
            @Override
            public void run() {
                broadcastToPlayers(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("runners-started"));
            }
        }.runTaskLater(MCManHunt.getPlugin(MCManHunt.class), 100);
        new BukkitRunnable() {
            @Override
            public void run() {
                gameState.getPlayersInGame().values().forEach(playerState -> {
                    if (!playerState.getPlayerRole().equals(PlayerRole.RUNNER)) {
                        playerState.setIsMovementRestricted(false);
                    }
                });
            }
        }.runTaskLaterAsynchronously(MCManHunt.getPlugin(MCManHunt.class), 700);
        new BukkitRunnable() {
            @Override
            public void run() {
                broadcastToPlayers(ChatColor.RED + MCManHuntStringCache.getInstance().getStringFromCache("hunters-started"));
            }
        }.runTaskLater(MCManHunt.getPlugin(MCManHunt.class), 700);
    }

    public void broadcastToPlayers(@NotNull final String message) {
        gameState.getPlayersInGame().forEach((uuid, playerState) -> {
            if (playerState.isOnline()) {
                Bukkit.getPlayer(uuid).sendMessage(message);
            }
        });
    }

    public @NotNull GameState getGameState() {
        return this.gameState;
    }

    public @NotNull GameFlowState getGameFlowState() {
        return this.gameState.getGameFlowState();
    }

    public synchronized Set<PlayerState> getRunners() {
        return gameState.getRunners();
    }

    public synchronized Set<PlayerState> getHunters() {
        return gameState.getHunters();
    }

    /**
     * Gives each hunter one compass per runner. Each compass can be updated by right-clicking the air with the compass
     * in hand.
     * @param hunterUUID
     */
    public void giveHunterCompasses(@NotNull final UUID hunterUUID) {
        final PlayerState[] runners = this.getRunners().toArray(new PlayerState[0]);
        Player player = Bukkit.getPlayer(hunterUUID);
        for (int x = 0; x < runners.length; x++) {
            CompassState compassState = CompassStateCache.getInstance().getCompassState(runners[x].getPlayerUUID());
            ItemStack compass = new ItemStack(Material.COMPASS);
            if (compassState != null) {
                compassState.updatePlayerLocation();
                CompassMeta compassMeta = compassState.getCompassMeta();
                compass.setItemMeta(compassMeta);
            } else {
                CompassState newCompassState =
                        new CompassState(runners[x].getPlayerUUID(), CompassMetaBuilder.getInstance()
                                .addEnchant(Enchantment.VANISHING_CURSE, 1, false)
                                .setLodestone(runners[x].getLastKnownLocation()).setLodestoneTracked(false)
                                .setName(runners[x].getPlayerName() + " Tracker").create());
                CompassMeta compassMeta = newCompassState.getCompassMeta();
                compass.setItemMeta(compassMeta);
            }
            player.getInventory().setItem(x, compass);
        }
    }

    /**
     * Pauses the game, sets the difficulty of each game-world to peaceful and stops the daylight-cycle, to prevent any
     * unfair advantages. Players are notified when the game is paused.
     */
    public void pause() {
        this.gameState.setGameFlowState(GameFlowState.PAUSED);
        this.gameState.setUniverseDifficulty(Difficulty.PEACEFUL);
        this.gameState.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        broadcastToPlayers(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("game-paused"));
    }

    /**
     * Informs the players that the game will resume in 5 seconds.
     * Resumes a paused game, sets the difficulty of each game-world to the value it was at before pausing, and starts
     * the daylight-cycle again.
     */
    public void resume() {
        broadcastToPlayers(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("game-resuming"));
        new BukkitRunnable() {
            @Override
            public void run() {
                gameState.setGameFlowState(GameFlowState.RUNNING);
                gameState.setUniverseDifficulty(gameState.getDefaultGameDifficulty());
                gameState.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                broadcastToPlayers(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("game-has-resumed"));
            }
        }.runTaskLater(MCManHunt.getPlugin(MCManHunt.class), 100L);
    }

    /**
     * Stops an ongoing game. If {@link Universe#getDestroyWhenGameIsStopped()} returns true, the {@link Universe} will
     * be marked for destruction. All players are moved to the default world, and will be informed of the game being
     * stopped.
     */
    public void stop() {
        if (this.gameState.getGameUniverse().getDestroyWhenGameIsStopped()) {
            this.gameState.markUniverseForDestruction(true);
        }
        teleportPlayersToDefaultWorld();
        broadcastToPlayers(ChatColor.RED + MCManHuntStringCache.getInstance().getStringFromCache("game-has-stopped"));
        this.removeAllPlayersFromGame();
        GameCache.getInstance().removeGame(this.getName());
    }

    private void removeAllPlayersFromGame() {
        this.gameState.removeAllPlayersFromGame();
    }

    public void teleportPlayersToDefaultWorld() {
        this.gameState.getPlayersInGame().keySet().forEach(uuid -> {
            final Player player = Bukkit.getPlayer(uuid);
            player.setBedSpawnLocation(Bukkit.getWorld("world").getSpawnLocation(), true);
            player.teleport(Bukkit.getWorld("world")
                    .getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        });
    }

    public synchronized long getNumberOfRunners() {
        return this.gameState.getNumberOfRunners();
    }

    public synchronized long getNumberOfHunters() {
        return this.gameState.getNumberOfHunters();
    }

    public HashSet<UUID> getHunterUUIDs() {
        return this.gameState.getHunterUUIDs();
    }

    public HashSet<UUID> getRunnerUUIDs() {
        return this.gameState.getRunnerUUIDs();
    }

    public Location getWorldSpawn() {
        return this.gameState.getWorldSpawn();
    }

    public boolean isCompassEnabledInNether() {
        return this.gameState.isCompassEnabledInNether();
    }

    public void setManHuntRule(final String key, final String value) {
        this.gameState.setManHuntRule(key, value);
    }

}
