package io.github.thesummergrinch.mcmanhunt.commands;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ResumeGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (sender.isOp()) {
            if (ManHuntUtilities.GAME_IN_PROGRESS.get()) {
                if (ManHuntUtilities.GAME_PAUSED.get()) {
                    ManHuntUtilities.resumeGame();
                } else {
                    ManHuntUtilities.SERVER.broadcastMessage("The game is not currently paused. To pause the game, use /pausegame.");
                }
            } else {
                ManHuntUtilities.SERVER.broadcastMessage("There is no ongoing game. To start a game, use /startgame.");
            }
            return true;
        }
        return false;
    }

}
