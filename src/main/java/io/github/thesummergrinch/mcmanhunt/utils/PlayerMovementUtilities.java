package io.github.thesummergrinch.mcmanhunt.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public final class PlayerMovementUtilities {

    private static final AtomicBoolean HUNTER_MOVEMENT_RESTRICTED = new AtomicBoolean(false);
    private static final AtomicBoolean RUNNER_MOVEMENT_RESTRICTED = new AtomicBoolean(false);

    /** Checks whether or not the HUNTER_MOVEMENT_RESTRICTED-flag is set to true and returns the result.
     *
     * @return boolean - true if Hunter-movement is set to be restricted. False otherwise.
     */
    public static synchronized boolean isHunterMovementRestricted() {
        return PlayerMovementUtilities.HUNTER_MOVEMENT_RESTRICTED.get();
    }

    /** Checks whether or not the RUNNER_MOVEMENT_RESTRICTED-flag is set to true and returns the result.
     *
     * @return boolean - true if Runner-movement is set to be restricted. False otherwise.
     */
    public static synchronized boolean isRunnerMovementRestricted() {
        return PlayerMovementUtilities.RUNNER_MOVEMENT_RESTRICTED.get();
    }

    /**
     * Sets the HUNTER_MOVEMENT_RESTRICTED-flag to true.
     */
    public static void restrictHunterMovement() {
        PlayerMovementUtilities.HUNTER_MOVEMENT_RESTRICTED.set(true);
    }

    /**
     * Sets the RUNNER_MOVEMENT_RESTRICTED-flag to true;
     */
    public static void restrictRunnerMovement() {
        PlayerMovementUtilities.RUNNER_MOVEMENT_RESTRICTED.set(true);
    }

    /**
     * Sets the RUNNER_MOVEMENT_RESTRICTED-flag to false.
     */
    public static void allowRunnerMovement() {
        PlayerMovementUtilities.RUNNER_MOVEMENT_RESTRICTED.set(false);
    }

    /**
     * Sets the HUNTER_MOVEMENT_RESTRICTED-flag to false.
     */
    public static void allowHunterMovement() {
        PlayerMovementUtilities.HUNTER_MOVEMENT_RESTRICTED.set(false);
    }

}
