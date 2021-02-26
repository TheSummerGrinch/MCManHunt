package io.github.thesummergrinch.mcmanhunt.game.gamecontrols;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.events.ManHuntWinEvent;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.settings.DefaultSettingsContainer;
import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Provides a layer of information for each {@link Game}-object.
 */
public final class GameState implements ConfigurationSerializable {

    @NotNull
    private final Map<UUID, PlayerState> playersInGame;
    @NotNull
    private final Universe gameUniverse;
    @NotNull
    private final String gameName;
    @NotNull
    private final Location worldSpawn;
    private boolean isCompassEnabledInNether;
    @NotNull
    private GameFlowState gameFlowState;
    @NotNull
    private Difficulty defaultGameDifficulty;
    private boolean playerRolesRandomized;
    private long headstart;

    protected GameState(final Universe gameUniverse) {
        this.gameUniverse = gameUniverse;
        this.gameFlowState = GameFlowState.DEFAULT;
        this.gameName = this.gameUniverse.getName();
        this.playersInGame = new HashMap<>();
        this.isCompassEnabledInNether = Boolean.parseBoolean(DefaultSettingsContainer.getInstance()
                .getSetting("compass-enabled-in-nether"));
        this.defaultGameDifficulty = gameUniverse.getWorld(gameName).getDifficulty();
        this.worldSpawn = gameUniverse.getWorld(gameName).getSpawnLocation();
        this.playerRolesRandomized = Boolean.parseBoolean((DefaultSettingsContainer.getInstance()
                .getSetting("player-roles-randomized")));
        this.headstart = Long.parseLong(DefaultSettingsContainer.getInstance().getSetting("default-headstart"));
    }

    protected GameState(final Universe universe, final @NotNull Difficulty defaultGameDifficulty) {
        this(universe);
        this.defaultGameDifficulty = defaultGameDifficulty;
    }

    /**
     * Converts a String representing a {@link Difficulty}, to a
     * {@link Difficulty}.
     * @param difficultyString the String that should be converted to
     * {@link Difficulty}.
     * @return the resulting {@link Difficulty}
     */
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

    public static @NotNull GameState deserialize(final Map<String, Object> objects) {
        final Difficulty defaultGameDifficulty = getDifficultyFromString((String) objects.get("difficulty"));
        final Universe universe = (Universe) objects.get("universe");
        universe.setDifficulty(defaultGameDifficulty);
        universe.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        universe.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        GameState gameState = new GameState(universe, defaultGameDifficulty);
        Map<String, PlayerState> playerStateMap = (Map<String, PlayerState>) objects.get("players");
        playerStateMap.forEach((uuidString, playerState) -> gameState.addPlayerToGame(UUID.fromString(uuidString)));
        gameState.gameFlowState = GameFlowState.fromString((String) objects.get("game-flow-state"));
        return gameState;
    }

    /**
     * Sets the {@link GameFlowState} of the {@link GameState} to
     * {@link GameFlowState#DEFAULT}, in order to prevent the other team from
     * also fulfilling their win-condition, while the {@link Game} is being
     * ended.
     */
    public void initializeTeamWin() {
        setGameFlowState(GameFlowState.DEFAULT);
    }

    /**
     * Retrieved the name of the game this {@link GameState}-object is
     * related to.
     * @return the name of the game.
     */
    protected @NotNull String getGameName() {
        return this.gameName;
    }

    /**
     * Prepares the {@link PlayerState}-object and adds it to the specified
     * {@link Game}.
     * @param game the specified {@link Game}
     * @param playerUUID the {@link UUID} of the {@link Player}.
     */
    protected void addPlayerToGame(final Game game, @NotNull final UUID playerUUID) {
        PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(playerUUID);
        if (playerState.getGameName() != null) {
            GameCache.getInstance().getGameFromCache(playerState.getGameName()).removePlayerFromGame(playerUUID);
        }
        playerState.setGame(game);
        this.playersInGame.put(playerUUID, playerState);
    }

    /**
     * Used when the {@link GameState}-object is being deserialized. Adds the
     * {@link PlayerState} mapped to the specified {@link UUID}, to the
     * {@link #playersInGame}
     * @param playerUUID the {@link UUID} of the {@link Player} that was in
     *                   the {@link Game} when the server stopped.
     */
    private void addPlayerToGame(final UUID playerUUID) {
        final PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(playerUUID);
        this.playersInGame.put(playerUUID, playerState);
    }

    /**
     * Prepares the {@link PlayerState} mapped to the given {@link UUID}, and
     * removes the {@link PlayerState} from the {@link Game}. If the
     * {@link Game} was already ongoing, and the {@link Player} was the last
     * in their team, the opposing team wins by forfeit.
     * @param playerUUID the {@link UUID} of the leaving {@link Player}.
     */
    protected void removePlayerFromGame(@NotNull final UUID playerUUID) {
        PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(playerUUID);
        playerState.setPlayerRole(PlayerRole.DEFAULT);
        playerState.setGame(null);
        this.playersInGame.remove(playerUUID);
        if (!this.gameFlowState.equals(GameFlowState.DEFAULT)) { //Win by forfeit
            if (this.getNumberOfRunners() == 0) {
                Bukkit.getServer().getPluginManager()
                        .callEvent(new ManHuntWinEvent(gameName, getHunterUUIDs()));
            } else if (this.getNumberOfHunters() == 0) {
                Bukkit.getServer().getPluginManager()
                        .callEvent(new ManHuntWinEvent(gameName, getRunnerUUIDs()));
            }
        }
    }

    /**
     * Gets the {@link GameFlowState} of this {@link GameState}.
     * @return the {@link GameFlowState}.
     */
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

    protected synchronized @NotNull Set<PlayerState> getRunners() {
        return playersInGame.values().stream()
                .filter(playerState -> playerState.getPlayerRole().equals(PlayerRole.RUNNER))
                .collect(Collectors.toSet());
    }

    protected synchronized @NotNull Set<PlayerState> getHunters() {
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

    /**
     * Removes all {@link PlayerState}-objects from the {@link Game}.
     */
    protected void removeAllPlayersFromGame() {
        this.playersInGame.values().forEach(playerState -> {
            playerState.setGame(null);
            if (!playerState.isOnline()) return;
            Player player = Bukkit.getPlayer(playerState.getPlayerUUID());
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
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

    protected boolean isCompassEnabledInNether() {
        return this.isCompassEnabledInNether;
    }

    protected @NotNull HashSet<UUID> getHunterUUIDs() {
        final HashSet<UUID> hunterUUIDs = new HashSet<>();
        this.playersInGame.values().stream().filter(playerState -> playerState.getPlayerRole()
                .equals(PlayerRole.HUNTER)).forEach(playerState -> hunterUUIDs.add(playerState.getPlayerUUID()));
        return hunterUUIDs;
    }

    protected @NotNull HashSet<UUID> getRunnerUUIDs() {
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

    public void setManHuntRule(String key, String value) {
        switch (key) {
            case "compass-enabled-in-nether":
                this.isCompassEnabledInNether = Boolean.parseBoolean(value);
                break;
            case "player-roles-randomized":
                this.playerRolesRandomized = Boolean.parseBoolean(value);
                break;
            case "headstart":
                this.headstart = Long.parseLong(value);
                break;
            case "difficulty":
                if (value.equalsIgnoreCase("peaceful")
                        || value.equalsIgnoreCase("easy")
                        || value.equalsIgnoreCase("normal")
                        || value.equalsIgnoreCase("hard")
                ) {
                    this.getGameUniverse().setDifficulty(Difficulty.valueOf(value.toUpperCase()));
                }
                break;
            default:
                break;
        }
    }

    protected boolean arePlayerRolesRandomized() {
        return this.playerRolesRandomized;
    }

    protected long getHeadstart() {
        return this.headstart;
    }

    protected boolean isEligibleForStart() {
        return ((getNumberOfHunters() >= 1 && getNumberOfRunners() >= 1) || (playerRolesRandomized && playersInGame.size() >= 2));
    }

}
