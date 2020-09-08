package io.github.thesummergrinch.mcmanhunt.commands;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.PlayerMovementUtilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player && sender.isOp()) {
            if (GameFlowUtilities.isGameInProgress()) {
                GameFlowUtilities.stopGame();
                PlayerMovementUtilities.allowHunterMovement();
                PlayerMovementUtilities.allowRunnerMovement();
            } else {
                ManHuntUtilities.broadcastMessage(ChatColor.RED + "There is no game ongoing. You can reset the teams using /resetplayerroles.");
            }
            return true;
        }
        return false;
    }
}
