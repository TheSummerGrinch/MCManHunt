package io.github.thesummergrinch.mcmanhunt.commands.info;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListRunnersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (ManHuntUtilities.isRunnerMapEmpty()) {
            ManHuntUtilities.broadcastMessage("The runner-team has no members!");
        } else {
            StringBuilder builder = new StringBuilder("The runner team consists of: ");
            ManHuntUtilities.getRunners().forEach((player) -> builder.append(player.getName()).append(", "));
            sender.sendMessage(builder.substring(0, builder.length() - 2));
        }
        return true;
    }
}
