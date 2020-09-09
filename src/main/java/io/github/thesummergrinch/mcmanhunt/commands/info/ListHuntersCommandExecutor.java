package io.github.thesummergrinch.mcmanhunt.commands.info;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListHuntersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (ManHuntUtilities.isHunterMapEmpty()) {
            ManHuntUtilities.broadcastMessage("The hunter-team has no members!");
        } else {
            StringBuilder builder = new StringBuilder("The hunter team consists of: ");
            ManHuntUtilities.getHunters().forEach((player) -> builder.append(player.getName()).append(", "));
            sender.sendMessage(builder.substring(0, builder.length() - 2));
        }
        return true;
    }

}
