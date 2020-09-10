package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GameFlowUtilities {

    private static final AtomicBoolean GAME_IN_PROGRESS = new AtomicBoolean(false);
    private static final AtomicBoolean GAME_PAUSED = new AtomicBoolean(false);
    private static final AtomicBoolean TEAMS_ARE_RANDOMIZED = new AtomicBoolean(false);

    /**
     * Restricts player-movement, distributes tracker compasses to the Hunters and starts the game.
     */
    public static synchronized void startGame() {
        GAME_IN_PROGRESS.set(true);
        PlayerMovementUtilities.restrictRunnerMovement();
        PlayerMovementUtilities.restrictHunterMovement();
        PlayerInventoryUtilities.distributeTrackers();
        ManHuntUtilities.broadcastMessage("The Game will start in 10 seconds! The Runner(s) will have a 30 second head-start!");
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerMovementUtilities.allowRunnerMovement();
                ManHuntUtilities.broadcastMessage("The Runners can now start!");
            }
        }.runTaskLater(ManHuntUtilities.getManHuntPlugin(), 200);
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerMovementUtilities.allowHunterMovement();
                ManHuntUtilities.broadcastMessage("The Hunters are coming!");
            }
        }.runTaskLater(ManHuntUtilities.getManHuntPlugin(), 800);
    }

    /**
     * Sets the relevant gameflow-flags, allows player-movement, clears the inventories of the participating players,
     * and resets the teams.
     */
    public static synchronized void stopGame() {
        GAME_IN_PROGRESS.set(false);
        GAME_PAUSED.set(false);
        PlayerMovementUtilities.allowHunterMovement();
        PlayerMovementUtilities.allowRunnerMovement();
        PlayerInventoryUtilities.clearHuntersInventory();
        PlayerInventoryUtilities.clearRunnersInventory();
        ManHuntUtilities.resetplayerroles();
    }

    /**
     * Restricts the movement of all participating players, effectively pausing the game.
     *
     * @param player - Player-object of the player who issued the pausegame-command.
     */
    public static synchronized void pauseGame(final Player player) {
        GAME_PAUSED.set(true);
        PlayerMovementUtilities.restrictHunterMovement();
        PlayerMovementUtilities.restrictRunnerMovement();
        ManHuntUtilities.broadcastMessage("The game was paused by " + player.getName() + ".");
    }

    /**
     * Resumes the game, after a 5-second delay.
     */
    public static synchronized void resumeGame() {
        GAME_PAUSED.set(false);
        ManHuntUtilities.broadcastMessage("Game will resume in 5 seconds!");
        new BukkitRunnable() {
            public void run() {
                PlayerMovementUtilities.allowRunnerMovement();
                PlayerMovementUtilities.allowHunterMovement();
                ManHuntUtilities.broadcastMessage("Game has resumed!");
            }
        }.runTaskLater(ManHuntUtilities.getManHuntPlugin(), 100);
    }

    /**
     * Checks if the game is in progress.
     *
     * @return boolean - True if the game is in progress, false otherwise.
     */
    public static synchronized boolean isGameInProgress() {
        return GAME_IN_PROGRESS.get();
    }

    /**
     * Checks if the game is paused.
     *
     * @return boolean - True if the game is paused, false otherwise.
     */
    public static synchronized boolean isGamePaused() {
        return GAME_PAUSED.get();
    }

    /**
     * Returns whether or not the teams are to be randomized.
     * @return boolean - True if teams are to be randomized, false otherwise.
     */
    public static synchronized boolean areTeamsRandomized() {
        return TEAMS_ARE_RANDOMIZED.get();
    }

    /**
     * Randomizes the teams and starts the game. (Will modularize the randomization).
     */
    public static synchronized void startRandomizedGame() {
        ArrayList<Player> players = (ArrayList<Player>) Arrays.asList(ManHuntUtilities.getPlayersInRandomQueue().toArray(new Player[0]));
        Collections.shuffle(players);
        int playersOnRunnerTeam = players.size() / 2;
        for (int x = -playersOnRunnerTeam; x < 0; x++) {
            ManHuntUtilities.addRunner(players.get(x + playersOnRunnerTeam));
        }
        for (int y = playersOnRunnerTeam - 1; y < players.size(); y++) {
            ManHuntUtilities.addHunter(players.get(y));
        }
        ManHuntUtilities.clearRandomTeamQueue();
        TEAMS_ARE_RANDOMIZED.set(false);
        startGame();
    }

}
