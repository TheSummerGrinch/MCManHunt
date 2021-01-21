package io.github.thesummergrinch.mcmanhunt.game.players;

import org.jetbrains.annotations.NotNull;

public enum PlayerRole {

    HUNTER, RUNNER, SPECTATOR, DEFAULT;

    public static @NotNull PlayerRole fromString(final String playerRoleString) {
        switch (playerRoleString.toLowerCase()) {
            case "hunter":
                return HUNTER;
            case "runner":
                return RUNNER;
            case "spectator":
                return SPECTATOR;
            default:
                return DEFAULT;
        }
    }

    @Override
    public @NotNull String toString() {
        switch (this) {
            case HUNTER:
                return "hunter";
            case RUNNER:
                return "runner";
            case SPECTATOR:
                return "spectator";
            default:
                return "default";
        }
    }

}
