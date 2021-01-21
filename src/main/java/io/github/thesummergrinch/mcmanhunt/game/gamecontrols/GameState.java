package io.github.thesummergrinch.mcmanhunt.game.gamecontrols;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public final class GameState implements ConfigurationSerializable {

    @NotNull
    private final Map<UUID, PlayerState> playersInGame;
    @NotNull
    private final Universe gameUniverse;
    @NotNull
    private final String gameName;
    @NotNull
    private final Location worldSpawn;

    @NotNull
    private GameFlowState gameFlowState;
    @NotNull
    private Difficulty defaultGameDifficulty;

    protected GameState(final Universe gameUniverse) {
        this.gameUniverse = gameUniverse;
        this.gameFlowState = GameFlowState.DEFAULT;
        this.gameName = this.gameUniverse.getName();
        this.playersInGame = new HashMap<>();
        this.defaultGameDifficulty = gameUniverse.getWorld(gameName).getDifficulty();
        this.worldSpawn = gameUniverse.getWorld(gameName).getSpawnLocation();
    }

    protected GameState(final Universe universe, final @NotNull Difficulty defaultGameDifficulty) {
        this(universe);
        this.defaultGameDifficulty = defaultGameDifficulty;
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

    @SuppressWarnings("unused")
    public static @NotNull GameState deserialize(final Map<String, Object> objects) {
        final Difficulty defaultGameDifficulty = getDifficultyFromString((String) objects.get("difficulty"));
        final Universe universe = (Universe) objects.get("universe");
        GameState gameState = new GameState(universe, defaultGameDifficulty);
        Map<String, PlayerState> playerStateMap = (Map<String, PlayerState>) objects.get("players");
        playerStateMap.forEach((uuidString, playerState) -> gameState.addPlayerToGame(UUID.fromString(uuidString)));
        gameState.gameFlowState = GameFlowState.fromString((String) objects.get("game-flow-state"));
        return gameState;
    }

    protected @NotNull String getGameName() {
        return this.gameName;
    }

    protected void addPlayerToGame(final Game game, @NotNull final UUID playerUUID) {
        PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(playerUUID);
        if (playerState.getGameName() != null) {
            GameCache.getInstance().getGameFromCache(playerState.getGameName()).removePlayerFromGame(playerUUID);
        }
        playerState.setGame(game);
        this.playersInGame.put(playerUUID, playerState);
    }

    private void addPlayerToGame(final UUID playerUUID) {
        final PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(playerUUID);
        this.playersInGame.put(playerUUID, playerState);
    }

    protected void removePlayerFromGame(@NotNull final UUID playerUUID) {
        PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(playerUUID);
        playerState.setGame(null);
        this.playersInGame.remove(playerUUID);
    }

    protected synchronized @NotNull GameFlowState getGameFlowState() {
        return this.gameFlowState;
    }

    protected synchronized void setGameFlowState(final @NotNull GameFlowState gameFlowState) {
        this.gameFlowState = gameFlowState;
    }

    protected @NotNull Universe getGameUniverse() {
        return this.gameUniverse;
    }

    protected void setGameUniverseTime(final long time) {
        this.gameUniverse.getWorld(gameUniverse.getName()).setTime(time);
    }

    protected @NotNull Map<UUID, PlayerState> getPlayersInGame() {
        return this.playersInGame;
    }

    protected Location getWorldSpawn() {
        return this.worldSpawn;
    }

    protected synchronized Set<PlayerState> getRunners() {
        return playersInGame.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.RUNNER))
                .collect(Collectors.toSet());
    }

    protected synchronized Set<PlayerState> getHunters() {
        return playersInGame.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.HUNTER))
                .collect(Collectors.toSet());
    }

    protected @NotNull Difficulty getDefaultGameDifficulty() {
        return this.defaultGameDifficulty;
    }

    protected void markUniverseForDestruction(final boolean destroy) {
        this.gameUniverse.setMarkedForDestruction(destroy);
    }

    protected void setUniverseDifficulty(final Difficulty difficulty) {
        this.gameUniverse.setDifficulty(difficulty);
    }

    protected <T> void setGameRule(final GameRule<T> gameRule, final T value) {
        gameUniverse.setGameRule(gameRule, value);
    }

    protected void removeAllPlayersFromGame() {
        this.playersInGame.values().forEach(playerState -> {
            playerState.setGame(null);
            Bukkit.getPlayer(playerState.getPlayerUUID()).getInventory().clear();
        });
        this.playersInGame.clear();
    }

    protected long getNumberOfRunners() {
        return this.playersInGame.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.RUNNER)).count();
    }

    protected long getNumberOfHunters() {
        return this.playersInGame.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.HUNTER)).count();
    }

    protected HashSet<UUID> getHunterUUIDs() {
        final HashSet<UUID> hunterUUIDs = new HashSet<>();
        this.playersInGame.values().stream().filter(playerState -> playerState.getPlayerRole()
                .equals(PlayerRole.HUNTER)).forEach(playerState -> hunterUUIDs.add(playerState.getPlayerUUID()));
        return hunterUUIDs;
    }

    protected HashSet<UUID> getRunnerUUIDs() {
        final HashSet<UUID> runnerUUIDs = new HashSet<>();
        this.playersInGame.values().stream().filter(playerState -> playerState.getPlayerRole()
                .equals(PlayerRole.RUNNER)).forEach(playerState -> runnerUUIDs.add(playerState.getPlayerUUID()));
        return runnerUUIDs;
    }

    protected void linkPlayerStatesToGameObject(final Game game) {
        this.playersInGame.forEach((uuid, playerState) -> playerState.setGame(game));
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> objects = new HashMap<>();
        HashMap<String, PlayerState> players = new HashMap<>();
        playersInGame.forEach(((uuid, playerState) -> players.put(uuid.toString(), playerState)));
        objects.put("universe", gameUniverse);
        objects.put("players", players);
        objects.put("difficulty", this.defaultGameDifficulty.toString());
        if (gameFlowState.equals(GameFlowState.DEFAULT)) {
            objects.put("game-flow-state", gameFlowState.toString());
        } else {
            objects.put("game-flow-state", GameFlowState.PAUSED.toString());
        }
        return objects;
    }


}