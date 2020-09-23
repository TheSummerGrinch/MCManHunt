package io.github.thesummergrinch.mcmanhunt.game;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.FileConfigurationLoader;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class GameController {

    private static volatile GameController instance;
    private final Difficulty defaultGameDifficulty;
    private GameState gameState;
    private GameMode gameMode;
    private int maxRunners;
    private int maxHunters;

    private GameController() {
        FileConfigurationLoader fileConfigurationLoader = FileConfigurationLoader.getInstance();
        if (fileConfigurationLoader.getBoolean("game-ongoing")) {
            this.gameState = GameState.PAUSED;
            final HashMap<PlayerRole, HashSet<String>> playersInPreviousGame = new HashMap<>();
            playersInPreviousGame.put(PlayerRole.RUNNER, fileConfigurationLoader.getObject("current-runners", HashSet.class));
            playersInPreviousGame.put(PlayerRole.HUNTER, fileConfigurationLoader.getObject("current-hunters", HashSet.class));
            playersInPreviousGame.put(PlayerRole.SPECTATOR, fileConfigurationLoader.getObject("current-spectators", HashSet.class));
            intializeOngoingGamePlayerRoles(playersInPreviousGame);
        } else this.gameState = GameState.DEFAULT;
        this.maxHunters = fileConfigurationLoader.getInt("max-hunters");
        this.maxRunners = fileConfigurationLoader.getInt("max-runners");
        this.defaultGameDifficulty = MCManHunt.getPlugin(MCManHunt.class).getServer().getWorlds().get(0).getDifficulty();
        this.gameMode = GameMode.NORMAL;
    }

    public static GameController getInstance() {
        GameController gameController = instance;
        if (instance != null) return gameController;
        synchronized (GameController.class) {
            if (gameController == null) {
                instance = new GameController();
            }
            return instance;
        }
    }

    public void startGame() {
        this.setGameState(GameState.RUNNING);
        final ArrayList<PlayerState> playersToAllocate = (ArrayList<PlayerState>) UserCache.getInstance().getPlayersToRandomlyAllocate();
        if (!playersToAllocate.isEmpty()) {
            randomlyAllocatePlayersToTeam(playersToAllocate);
        }
        restrictPlayerMovement();
        distributeCompasses();
        MCManHunt.getPlugin(MCManHunt.class).getServer().broadcastMessage("The Game will start in 10 seconds! The Runners will get a 30 second head-start!");
        new BukkitRunnable() {
            @Override
            public void run() {
                allowRunnerMovement();
            }
        }.runTaskLaterAsynchronously(MCManHunt.getPlugin(MCManHunt.class), 200);
        new BukkitRunnable() {
            @Override
            public void run() {
                allowHunterMovement();
            }
        }.runTaskLaterAsynchronously(MCManHunt.getPlugin(MCManHunt.class), 800);

    }

    private void restrictPlayerMovement() {
        UserCache.getInstance().getAllPlayers().forEach(playerState -> {
            if (!playerState.getPlayerRole().equals(PlayerRole.DEFAULT)) {
                playerState.setIsMovementRestricted(true);
            }
        });
    }

    private void allowRunnerMovement() {
        UserCache.getInstance().getRunners().forEach(playerState -> playerState.setIsMovementRestricted(false));
    }

    private void allowHunterMovement() {
        UserCache.getInstance().getHunters().forEach(playerState -> playerState.setIsMovementRestricted(false));
    }

    private void distributeCompasses() {
        final ArrayList<CompassMeta> compassMetas = new ArrayList<>();
        UserCache.getInstance().getRunners().forEach(playerState -> compassMetas.add(playerState.getCompassMeta()));
        UserCache.getInstance().getHunters().forEach(playerState -> {
            if (playerState.isOnline()) {
                Player player = (Player) playerState.getPlayerObject();
                for (int x = 0; x < compassMetas.size(); x++) {
                    ItemStack compass = new ItemStack(Material.COMPASS);
                    compass.setItemMeta(compassMetas.get(x));
                    player.getInventory().setItem(x, compass);
                }
            }
        });
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(final GameState gameState) {
        this.gameState = gameState;
    }

    public int getMaxRunners() {
        return this.maxRunners;
    }

    public void setMaxRunners(final int maxRunners) {
        this.maxRunners = maxRunners;
    }

    public int getMaxHunters() {
        return this.maxHunters;
    }

    public void setMaxHunters(final int maxHunters) {
        this.maxHunters = maxHunters;
    }

    public void stopGame() {
        this.setGameState(GameState.DEFAULT);
        UserCache.getInstance().getAllPlayers().forEach(playerState -> playerState.setPlayerRole(PlayerRole.DEFAULT));
    }

    public void pauseGame() {
        this.setGameState(GameState.PAUSED);
        MCManHunt.getPlugin(MCManHunt.class).getServer().getWorlds()
                .forEach(world -> world.setDifficulty(Difficulty.PEACEFUL));
    }

    public void resumeGame() {
        this.setGameState(GameState.RUNNING);
        MCManHunt.getPlugin(MCManHunt.class).getServer().getWorlds()
                .forEach(world -> world.setDifficulty(this.defaultGameDifficulty));
        MCManHunt.getPlugin(MCManHunt.class).getServer().broadcastMessage(ChatColor.GREEN + "The Game has resumed!");
    }

    public enum GameState {
        RUNNING, PAUSED, DEFAULT
    }

    public enum GameMode {
        NORMAL, RANDOM
    }

    private void intializeOngoingGamePlayerRoles(final HashMap<PlayerRole, HashSet<String>> playersInPreviousGame) {
        playersInPreviousGame.forEach((playerrole, playerSet) -> {
            playerSet.forEach(playerUUIDString -> {
                PlayerState playerState = new PlayerState(UUID.fromString(playerUUIDString));
                playerState.setPlayerRole(playerrole);
                UserCache.getInstance().addPlayer(playerState);
            });
        });
    }

    public GameMode getManHuntGameMode() {
        return this.gameMode;
    }

    public void setManHuntGameMode(final GameMode gameMode) {
        this.gameMode = gameMode;
    }

    private void randomlyAllocatePlayersToTeam(final ArrayList<PlayerState> players) {
        long maxNumberOfHunters = this.maxHunters - UserCache.getInstance().getNumberOfHunters();
        long maxNumberOfRunners = this.maxRunners - UserCache.getInstance().getNumberOfRunners();
        for (PlayerState playerState : players) {
            if (Math.random() < 0.5) {
                if (maxNumberOfRunners > 0) {
                    playerState.setPlayerRole(PlayerRole.RUNNER);
                    maxNumberOfRunners -= 1;
                } else if (maxNumberOfHunters > 0){
                    playerState.setPlayerRole(PlayerRole.HUNTER);
                    maxNumberOfHunters -= 1;
                } else {
                    playerState.setPlayerRole(PlayerRole.RUNNER);
                }
            } else if (maxNumberOfHunters != 0){
                if (maxNumberOfHunters > 0) {
                    playerState.setPlayerRole(PlayerRole.HUNTER);
                    maxNumberOfHunters -= 1;
                } else if (maxNumberOfRunners > 0){
                    playerState.setPlayerRole(PlayerRole.RUNNER);
                    maxNumberOfRunners -= 1;
                } else {
                    playerState.setPlayerRole(PlayerRole.HUNTER);
                }
            }
        }
    }

}
