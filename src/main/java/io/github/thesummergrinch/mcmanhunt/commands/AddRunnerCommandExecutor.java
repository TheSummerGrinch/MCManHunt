package io.github.thesummergrinch.mcmanhunt.commands;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AddRunnerCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (sender instanceof Player) {
            if (sender.isOp()) {
                if (!ManHuntUtilities.GAME_IN_PROGRESS.get()) {
                    if (!ManHuntUtilities.addRunner(args[0])) return false;
                    ManHuntUtilities.SERVER.broadcastMessage(args[0] + " was added to the Runners!");
                } else {
                    ManHuntUtilities.SERVER.broadcastMessage("New players cannot be added while the game is in progress. " +
                            "To stop the game, use /stopgame and /resetplayerroles.");
                }
            }
            return true;
        }
        return false;
    }

}
