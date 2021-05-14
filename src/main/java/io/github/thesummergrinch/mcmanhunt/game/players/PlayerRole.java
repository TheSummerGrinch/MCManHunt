package io.github.thesummergrinch.mcmanhunt.game.players;

import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public enum PlayerRole {

    HUNTER(ChatColor.DARK_RED+ "[" + LanguageFileLoader.getInstance()
            .getString("hunter") + "]" + ChatColor.RESET),
    RUNNER(ChatColor.DARK_GREEN + "[" + LanguageFileLoader.getInstance()
            .getString("runner") + "]" + ChatColor.RESET),
    SPECTATOR(""),
    DEFAULT("");

    private final String rolePrefix;

    PlayerRole(final String rolePrefix) {
        this.rolePrefix = rolePrefix;
    }

    public String getRolePrefix() {
        return this.rolePrefix;
    }

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
