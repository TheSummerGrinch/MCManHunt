package io.github.thesummergrinch.mcmanhunt.commands.roles;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetPlayerRolesCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            if (GameFlowUtilities.isGameInProgress()) {
                sender.sendMessage(ChatColor.RED + "A game is currently in progress. To stop the game and reset roles" +
                        ", use /stopgame.");
                return true;
            }
            ManHuntUtilities.resetplayerroles();
            return true;
        }
        return false;
    }

}
