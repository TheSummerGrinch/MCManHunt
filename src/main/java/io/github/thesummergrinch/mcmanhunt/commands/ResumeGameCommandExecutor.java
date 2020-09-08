package io.github.thesummergrinch.mcmanhunt.commands;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResumeGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player && sender.isOp()) {
            if (GameFlowUtilities.isGameInProgress()) {
                if (GameFlowUtilities.isGamePaused()) {
                    GameFlowUtilities.resumeGame();
                } else {
                    ManHuntUtilities.broadcastMessage(ChatColor.RED + "The game is not currently paused. To pause the game, use /pausegame.");
                }
            } else {
                ManHuntUtilities.broadcastMessage(ChatColor.RED + "There is no ongoing game. To start a game, use /startgame.");
            }
            return true;
        }
        return false;
    }

}
