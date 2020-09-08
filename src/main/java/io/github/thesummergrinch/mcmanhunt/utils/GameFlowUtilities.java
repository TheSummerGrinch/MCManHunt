package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicBoolean;

public final class GameFlowUtilities {

    private static final AtomicBoolean GAME_IN_PROGRESS = new AtomicBoolean(false);
    private static final AtomicBoolean GAME_PAUSED = new AtomicBoolean(false);

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
        }.runTaskLater(ManHuntUtilities.MANHUNT_PLUGIN, 200);
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerMovementUtilities.allowHunterMovement();
                ManHuntUtilities.broadcastMessage("The Hunters are coming!");
            }
        }.runTaskLater(ManHuntUtilities.MANHUNT_PLUGIN, 800);
    }

    public static synchronized void stopGame() {
        GAME_IN_PROGRESS.set(false);
        GAME_PAUSED.set(false);
        PlayerMovementUtilities.allowHunterMovement();
        PlayerMovementUtilities.allowRunnerMovement();
        PlayerInventoryUtilities.clearHuntersInventory();
        PlayerInventoryUtilities.clearRunnersInventory();
        ManHuntUtilities.resetplayerroles();
    }

    public static synchronized void pauseGame(Player player) {
        GAME_PAUSED.set(true);
        PlayerMovementUtilities.restrictHunterMovement();
        PlayerMovementUtilities.restrictRunnerMovement();
        ManHuntUtilities.broadcastMessage("The game was paused by " + player.getName() + ".");
    }

    public static synchronized void resumeGame() {
        GAME_PAUSED.set(false);
        ManHuntUtilities.broadcastMessage("Game will resume in 5 seconds!");
        new BukkitRunnable() {
            public void run() {
                PlayerMovementUtilities.allowRunnerMovement();
                PlayerMovementUtilities.allowHunterMovement();
                ManHuntUtilities.broadcastMessage("Game has resumed!");
            }
        }.runTaskLater(ManHuntUtilities.MANHUNT_PLUGIN, 100);
    }

    public static synchronized boolean isGameInProgress() {
        return GAME_IN_PROGRESS.get();
    }

    public static synchronized void setGameInProgress(final boolean gameInProgress) {
        GAME_IN_PROGRESS.set(gameInProgress);
    }

    public static synchronized boolean isGamePaused() {
        return GAME_PAUSED.get();
    }

    public static synchronized void setGamePaused(final boolean gamePaused) {
        GAME_PAUSED.set(gamePaused);
    }

}
