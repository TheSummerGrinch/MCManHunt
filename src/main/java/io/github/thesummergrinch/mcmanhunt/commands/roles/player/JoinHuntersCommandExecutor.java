package io.github.thesummergrinch.mcmanhunt.commands.roles.player;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinHuntersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && ((Player) sender).isOnline() && !GameFlowUtilities.isGameInProgress()) {
            final Player player = ((Player) sender).getPlayer();
            if(GameFlowUtilities.areTeamsRandomized()) {
                player.sendMessage("The game is in random-mode! Use /joinrandomteam to participate in this game!");
                return true;
            } else if (ManHuntUtilities.isHunter(player)) {
                player.sendMessage("You are already a member of the Hunter-team!");
            } else if (ManHuntUtilities.isRunner(player)) {
                player.sendMessage("You are a member of the Runner-team. To leave the Runner-team and join the Hunter-team, use /leaverunners and /joinhunters.");
            } else if (ManHuntUtilities.isHunterTeamFull()) {
                player.sendMessage("The Hunter-team has reached maximum capacity. Please join another team.");
            } else if (ManHuntUtilities.addHunter(player)) {
                ManHuntUtilities.broadcastMessage(player.getName() + " has joined the Hunter-team!");
            } else {
                player.sendMessage("Something went wrong. The server could not add you to the Hunter-team. Please contact the plugin-author.");
            }
            return true;
        }
        return false;
    }

}
