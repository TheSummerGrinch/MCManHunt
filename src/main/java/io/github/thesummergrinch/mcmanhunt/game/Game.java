package io.github.thesummergrinch.mcmanhunt.game;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.CompassStateCache;
import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
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
import java.util.stream.Collectors;

public final class Game implements ConfigurationSerializable {

    @NotNull
    private final HashMap<UUID, PlayerState> playersInGame;
    @NotNull
    private final Universe gameUniverse;
    @NotNull
    private final String gameName;
    @NotNull
    private final Difficulty defaultGameDifficulty;

    @NotNull
    private GameState gameState;
    @NotNull
    private final Location worldSpawn;

    public Game(@NotNull final Universe gameUniverse) {
        this.playersInGame = new HashMap<>();
        this.gameUniverse = gameUniverse;
        this.gameName = this.gameUniverse.getName();
        this.gameState = GameState.DEFAULT;
        this.defaultGameDifficulty = gameUniverse.getWorld(gameName).getDifficulty();
        GameCache.getInstance().cacheGame(gameName, this);
        this.worldSpawn = gameUniverse.getWorld(gameName).getSpawnLocation();
    }

    public Game(@NotNull final Universe gameUniverse, @NotNull final Difficulty defaultGameDifficulty) {
        this.playersInGame = new HashMap<>();
        this.gameUniverse = gameUniverse;
        this.gameName = this.gameUniverse.getName();
        this.gameState = GameState.DEFAULT;
        this.defaultGameDifficulty = defaultGameDifficulty;
        this.gameUniverse.setDifficulty(this.defaultGameDifficulty);
        GameCache.getInstance().cacheGame(gameName, this);
        this.worldSpawn = gameUniverse.getWorld(gameName).getSpawnLocation();
    }

    @SuppressWarnings("unused")
    public static @NotNull Game deserialize(final Map<String, Object> objects) {
        Difficulty defaultGameDifficulty = getDifficultyFromString((String) objects.get("difficulty"));
        Game game = new Game((Universe) objects.get("universe"), defaultGameDifficulty);
        ((Map<String, PlayerState>) objects.get("players")).forEach((uuidString, playerState) -> game.addPlayerToGame(UUID.fromString(uuidString)));
        game.gameState = GameState.fromString((String) objects.get("game-state"));
        return game;
    }

    private static Difficulty getDifficultyFromString(@NotNull final String difficultyString) {
        switch (difficultyString.toLowerCase()) {
            case "peaceful":
                return Difficulty.PEACEFUL;
            case "easy":
                return Difficulty.EASY;
            case "normal":
                return Difficulty.NORMAL;
            case "hard":
                return Difficulty.HARD;
            default:
                return null;
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> objects = new HashMap<>();
        HashMap<String, PlayerState> players = new HashMap<>();
        playersInGame.forEach(((uuid, playerState) -> players.put(uuid.toString(), playerState)));
        objects.put("universe", gameUniverse);
        objects.put("players", players);
        objects.put("difficulty", this.defaultGameDifficulty.toString());
        if (gameState.equals(GameState.DEFAULT)) {
            objects.put("game-state", gameState.toString());
        } else {
            objects.put("game-state", GameState.PAUSED.toString());
        }
        return objects;
    }

    public void addPlayerToGame(@NotNull final UUID playerUUID) {
        PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(playerUUID);
        if (playerState.getGameName() != null) {
            GameCache.getInstance().getGameFromCache(playerState.getGameName()).removePlayerFromGame(playerUUID);
        }
        playerState.setGame(this);
        this.playersInGame.put(playerUUID, playerState);
    }

    public void removePlayerFromGame(@NotNull final UUID playerUUID) {
        PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(playerUUID);
        playerState.setGame(null);
        this.playersInGame.remove(playerUUID);
    }

    public @NotNull String getName() {
        return this.gameName;
    }

    public void start() {
        this.setGameState(GameState.RUNNING);
        gameUniverse.getWorld(gameUniverse.getName()).setTime(1000L);
        playersInGame.forEach((uuid, playerState) -> {
            if (playerState.isOnline()) {
                final Player player = Bukkit.getPlayer(playerState.getPlayerUUID());
                player.setBedSpawnLocation(gameUniverse.getWorld(gameName).getSpawnLocation(), true);
                player.teleport(gameUniverse.getWorld(gameName)
                        .getSpawnLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
                player.sendMessage(MCManHuntStringCache.getInstance().getStringFromCache(ChatColor.GREEN + "game-start-intro"));
            }
        });
        new BukkitRunnable() {
            @Override
            public void run() {
                playersInGame.forEach((uuid, playerState) -> playerState.setIsMovementRestricted(true));
            }
        }.runTaskAsynchronously(MCManHunt.getPlugin(MCManHunt.class));
        getHunters().forEach(hunter -> giveHunterCompasses(hunter.getPlayerUUID()));
        new BukkitRunnable() {
            @Override
            public void run() {
                playersInGame.values().forEach(playerState -> {
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
                playersInGame.values().forEach(playerState -> {
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
        this.playersInGame.forEach((uuid, playerState) -> {
            if (playerState.isOnline()) {
                Bukkit.getPlayer(uuid).sendMessage(message);
            }
        });
    }

    public @NotNull GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(@NotNull final GameState gameState) {
        this.gameState = gameState;
    }

    public synchronized Set<PlayerState> getRunners() {
        return playersInGame.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.RUNNER))
                .collect(Collectors.toSet());
    }

    public synchronized Set<PlayerState> getHunters() {
        return playersInGame.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.HUNTER))
                .collect(Collectors.toSet());
    }

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

    public void pause() {
        this.setGameState(GameState.PAUSED);
        this.gameUniverse.setDifficulty(Difficulty.PEACEFUL);
        this.gameUniverse.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        broadcastToPlayers(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("game-paused"));
    }

    public void resume() {
        broadcastToPlayers(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("game-resuming"));
        new BukkitRunnable() {
            @Override
            public void run() {
                setGameState(GameState.RUNNING);
                gameUniverse.setDifficulty(defaultGameDifficulty);
                gameUniverse.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
                broadcastToPlayers(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("game-has-resumed"));
            }
        }.runTaskLater(MCManHunt.getPlugin(MCManHunt.class), 100L);
    }

    public void stop() {
        if (this.gameUniverse.getDestroyWhenGameIsStopped()) {
            this.gameUniverse.setMarkedForDestruction(true);
        }
        teleportPlayersToDefaultWorld();
        broadcastToPlayers(ChatColor.RED + MCManHuntStringCache.getInstance().getStringFromCache("game-has-stopped"));
        this.removeAllPlayersFromGame();
        GameCache.getInstance().removeGame(this.getName());
    }

    private void removeAllPlayersFromGame() {
        this.playersInGame.values().forEach(playerState -> {
            playerState.setGame(null);
            Bukkit.getPlayer(playerState.getPlayerUUID()).getInventory().clear();
        });
        this.playersInGame.clear();
    }

    public void teleportPlayersToDefaultWorld() {
        this.playersInGame.keySet().forEach(uuid -> {
            final Player player = Bukkit.getPlayer(uuid);
            player.setBedSpawnLocation(Bukkit.getWorld("world").getSpawnLocation(), true);
            player.teleport(Bukkit.getWorld("world")
                    .getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        });
    }

    public long getNumberOfRunners() {
        return this.playersInGame.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.RUNNER)).count();
    }

    public long getNumberOfHunters() {
        return this.playersInGame.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.HUNTER)).count();
    }

    public HashSet<UUID> getHunterUUIDs() {
        final HashSet<UUID> hunterUUIDs = new HashSet<>();
        this.playersInGame.values().stream().filter(playerState -> playerState.getPlayerRole()
                .equals(PlayerRole.HUNTER)).forEach(playerState -> hunterUUIDs.add(playerState.getPlayerUUID()));
        return hunterUUIDs;
    }

    public HashSet<UUID> getRunnerUUIDs() {
        final HashSet<UUID> runnerUUIDs = new HashSet<>();
        this.playersInGame.values().stream().filter(playerState -> playerState.getPlayerRole()
                .equals(PlayerRole.RUNNER)).forEach(playerState -> runnerUUIDs.add(playerState.getPlayerUUID()));
        return runnerUUIDs;
    }

    public Location getWorldSpawn() {
        return this.worldSpawn;
    }

}
