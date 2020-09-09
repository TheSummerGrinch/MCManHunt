package io.github.thesummergrinch.mcmanhunt.commands.roles.player;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinRandomTeamCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ManHuntUtilities.isRunner(player) || ManHuntUtilities.isHunter(player)) {
                player.sendMessage("You have already joined a team. Please leave your team and try again.");
                return true;
            } else if (GameFlowUtilities.isGameInProgress()) {
                player.sendMessage("The game is already in progress. Please try again when the current game ends.");
                return true;
            } else {
                if (Math.random() > 0.5) {
                    player.sendMessage("You have been placed in the Runner-team!");
                    return ManHuntUtilities.addRunner(player);
                } else {
                    player.sendMessage("You have been placed in the Hunter-team");
                    return ManHuntUtilities.addHunter(player);
                }
            }
        }
        return false;
    }

}
