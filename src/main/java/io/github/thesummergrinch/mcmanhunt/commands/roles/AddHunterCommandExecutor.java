package io.github.thesummergrinch.mcmanhunt.commands.roles;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddHunterCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player && sender.isOp()) {
            if (!GameFlowUtilities.isGameInProgress()) {
                if (!ManHuntUtilities.addHunter(args[0])) return false;
                ManHuntUtilities.broadcastMessage(args[0] + " was added to the Hunters!");
            } else {
                ManHuntUtilities.broadcastMessage("New players cannot be added while the game is in progress. " +
                        "To stop the game, use /stopgame and /resetplayerroles.");
            }
            return true;
        }
        return false;
    }

}
