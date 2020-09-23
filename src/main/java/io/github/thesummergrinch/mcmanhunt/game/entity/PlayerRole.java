package io.github.thesummergrinch.mcmanhunt.game.entity;

public enum PlayerRole {

    HUNTER, RUNNER, SPECTATOR, DEFAULT, RANDOM;

    @Override
    public String toString() {
        switch (this) {
            case HUNTER:
                return "Hunter";
            case RUNNER:
                return "Runner";
            case RANDOM:
                return "Random";
            case SPECTATOR:
                return "Spectator";
            default:
                return "Default";
        }
    }

}
