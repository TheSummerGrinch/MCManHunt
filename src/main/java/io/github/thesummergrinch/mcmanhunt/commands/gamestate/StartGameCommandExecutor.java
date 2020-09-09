package io.github.thesummergrinch.mcmanhunt.commands.gamestate;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class StartGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            if (ManHuntUtilities.isHunterMapEmpty()) {
                ManHuntUtilities.broadcastMessage(ChatColor.RED + "There are no hunters assigned to the hunter-team. " +
                        "Assign players to the hunter-team using /addhunter [playername]. The game will not start.");
                return true;
            } else if (ManHuntUtilities.isRunnerMapEmpty()) {
                ManHuntUtilities.broadcastMessage(ChatColor.RED + "There are no runners assigned to the runner-team. " +
                        "Assign players to the runner-team using /addrunner [playername]. The game will not start.");
                return true;
            } else if (ManHuntUtilities.isHunterTeamOverCapacity() || ManHuntUtilities.isRunnerTeamOverCapacity()) {
                ManHuntUtilities.broadcastMessage(ChatColor.RED + "The teams may be over the maximum capacity. " +
                        "Please use /maxrunners [number] or /maxhunters [number] to adjust the team-capacity.");
                return true;
            }
            if (!GameFlowUtilities.isGameInProgress()) {
                if(!GameFlowUtilities.areTeamsRandomized()) {
                    GameFlowUtilities.startGame();
                } else {
                    GameFlowUtilities.startRandomizedGame();
                }
            } else {
                ManHuntUtilities.broadcastMessage(ChatColor.RED + "A game is already in progress! You can stop this game using /stopgame.");
            }
            return true;
        }
        return false;
    }

}
