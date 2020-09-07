package io.github.thesummergrinch.mcmanhunt.commands;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StopGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (sender.isOp()) {
            if (ManHuntUtilities.GAME_IN_PROGRESS.get()) {
                ManHuntUtilities.stopGame();
                ManHuntUtilities.allowHunterMovement();
                ManHuntUtilities.allowRunnerMovement();
            } else {
                ManHuntUtilities.SERVER.broadcastMessage("There is no game ongoing. You can reset the teams using /resetplayerroles.");
            }
            return true;
        }
        return false;
    }
}
