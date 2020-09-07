package io.github.thesummergrinch.mcmanhunt.commands;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StartGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (sender.isOp()) {
            if (ManHuntUtilities.isHunterMapEmpty()) {
                ManHuntUtilities.SERVER.broadcastMessage("There are no hunters assigned to the hunter-team. " +
                        "Assign players to the hunter-team using /addhunter [playername]. The game will not start.");
                //return true;
            } else if (ManHuntUtilities.isRunnerMapEmpty()) {
                ManHuntUtilities.SERVER.broadcastMessage("There are no runners assigned to the runner-team. " +
                        "Assign players to the runner-team using /addrunner [playername]. The game will not start.");
                //return true;
            }
            if (!ManHuntUtilities.GAME_IN_PROGRESS.get()) {
                ManHuntUtilities.startGame();
            } else {
                ManHuntUtilities.SERVER.broadcastMessage("A game is already in progress! You can stop this game using /stopgame.");
            }
            return true;
        }
        return false;
    }

}
