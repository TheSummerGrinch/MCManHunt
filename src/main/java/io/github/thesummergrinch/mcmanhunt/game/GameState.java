package io.github.thesummergrinch.mcmanhunt.game;

import org.jetbrains.annotations.NotNull;

public enum GameState {
    RUNNING,
    PAUSED,
    DEFAULT;

    public static @NotNull GameState fromString(@NotNull final String gameStateString) {
        switch (gameStateString) {
            case "paused":
                return PAUSED;
            case "running":
                return RUNNING;
            default:
                return DEFAULT;
        }
    }

    @Override
    public @NotNull String toString() {
        switch (this) {
            case PAUSED:
                return "paused";
            case RUNNING:
                return "running";
            default:
                return "default";
        }
    }

}
