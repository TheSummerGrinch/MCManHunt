package io.github.thesummergrinch.mcmanhunt.commands.roles.player;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JoinRandomTeamCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final UUID playerUUID = player.getUniqueId();
            if (ManHuntUtilities.isRunner(playerUUID) || ManHuntUtilities.isHunter(playerUUID)) {
                player.sendMessage("You have already joined a team. Please leave your team and try again.");
                return true;
            } else if (GameFlowUtilities.isGameInProgress()) {
                player.sendMessage("The game is already in progress. Please try again when the current game ends.");
                return true;
            } else {
                if (!GameFlowUtilities.areTeamsRandomized()) {
                    if (Math.random() > 0.5) {
                        player.sendMessage("You have been placed in the Runner-team!");
                        return ManHuntUtilities.addRunner(player);
                    } else {
                        player.sendMessage("You have been placed in the Hunter-team");
                        return ManHuntUtilities.addHunter(player);
                    }
                } else {
                    if (ManHuntUtilities.addPlayerToRandomQueue(player)) {
                        player.sendMessage("You will be randomly placed in a team when the game starts!");
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
