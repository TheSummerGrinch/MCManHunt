package io.github.thesummergrinch.mcmanhunt.commands.roles.player;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveHuntersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && ((Player) sender).isOnline()) {
            final Player player = ((Player) sender).getPlayer();
            if (ManHuntUtilities.removeHunter(player.getUniqueId())) {
                ManHuntUtilities.broadcastMessage(player.getName() + " left the Hunter-team!");
            } else {
                player.sendMessage("You could not be removed from the Hunter-team. Check whether you are a member using /listhunters");
                return true;
            }
            return true;
        }
        return false;
    }
}
