package io.github.thesummergrinch.mcmanhunt.commands;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PauseGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (sender instanceof Player && sender.isOp()) {
            if (ManHuntUtilities.GAME_IN_PROGRESS.get()) {
                if (!ManHuntUtilities.GAME_PAUSED.get()) {
                    ManHuntUtilities.pauseGame((Player) sender);
                } else {
                    ManHuntUtilities.SERVER.broadcastMessage("The game is already paused. To resume the game, use /resumegame.");
                }
            } else {
                ManHuntUtilities.SERVER.broadcastMessage("There is no ongoing game. To start a game, use /startgame.");
            }
            return true;
        }
        return false;
    }

}
