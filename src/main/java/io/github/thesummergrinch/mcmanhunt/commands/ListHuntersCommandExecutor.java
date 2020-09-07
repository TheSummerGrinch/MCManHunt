package io.github.thesummergrinch.mcmanhunt.commands;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ListHuntersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (ManHuntUtilities.isHunterMapEmpty()) {
            ManHuntUtilities.SERVER.broadcastMessage("The hunter-team has no members!");
        } else {
            StringBuilder builder = new StringBuilder("The hunter team consists of: ");
            ManHuntUtilities.getHunters().forEach((player) -> builder.append(player.getName()).append(", "));
            sender.sendMessage(builder.substring(0, builder.length() - 2));
        }
        return true;
    }

}
