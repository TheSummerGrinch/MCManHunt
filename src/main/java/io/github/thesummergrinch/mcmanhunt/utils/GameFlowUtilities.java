package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicBoolean;

public final class GameFlowUtilities {

    private static final AtomicBoolean GAME_IN_PROGRESS = new AtomicBoolean(false);
    private static final AtomicBoolean GAME_PAUSED = new AtomicBoolean(false);

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

}
