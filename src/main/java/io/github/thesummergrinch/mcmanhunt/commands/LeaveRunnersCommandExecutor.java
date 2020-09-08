package io.github.thesummergrinch.mcmanhunt.commands;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveRunnersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && ((Player) sender).isOnline()) {
            final Player player = ((Player) sender).getPlayer();
            if (ManHuntUtilities.removePlayerFromRunners(player)) {
                ManHuntUtilities.broadcastMessage(player.getName() + " left the Runner-team!");
            } else {
                player.sendMessage("You could not be removed from the Runner-team. Check whether you are a member using /listrunners");
            }
            return true;
        }
        return false;
    }

}
