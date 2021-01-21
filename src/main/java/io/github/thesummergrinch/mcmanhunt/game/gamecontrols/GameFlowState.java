package io.github.thesummergrinch.mcmanhunt.game.gamecontrols;

import org.jetbrains.annotations.NotNull;

public enum GameFlowState {

    RUNNING,
    PAUSED,
    DEFAULT;

    public static @NotNull GameFlowState fromString(@NotNull final String gameStateString) {
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
