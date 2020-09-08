package io.github.thesummergrinch.mcmanhunt.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public final class PlayerMovementUtilities {

    private static final AtomicBoolean HUNTER_MOVEMENT_RESTRICTED = new AtomicBoolean(false);
    private static final AtomicBoolean RUNNER_MOVEMENT_RESTRICTED = new AtomicBoolean(false);

    public static synchronized boolean isHunterMovementRestricted() {
        return PlayerMovementUtilities.HUNTER_MOVEMENT_RESTRICTED.get();
    }

    public static synchronized boolean isRunnerMovementRestricted() {
        return PlayerMovementUtilities.RUNNER_MOVEMENT_RESTRICTED.get();
    }

    public static void restrictHunterMovement() {
        PlayerMovementUtilities.HUNTER_MOVEMENT_RESTRICTED.set(true);
    }

    public static void restrictRunnerMovement() {
        PlayerMovementUtilities.RUNNER_MOVEMENT_RESTRICTED.set(true);
    }

    public static void allowRunnerMovement() {
        PlayerMovementUtilities.RUNNER_MOVEMENT_RESTRICTED.set(false);
    }

    public static void allowHunterMovement() {
        PlayerMovementUtilities.HUNTER_MOVEMENT_RESTRICTED.set(false);
    }

}
