package io.github.thesummergrinch.mcmanhunt.commands.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SayGlobalCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Check to see if the CommandSender is a player, and the message is
        // not empty.
        if (sender instanceof Player && args.length > 0) {

            // Prepare a StringBuilder to contain the formatted message.
            final StringBuilder messageBuilder =
                    new StringBuilder("<" +sender.getName() + "> ");

            // Append all words to the message.
            for (String word : args) {
                messageBuilder.append(word);
                messageBuilder.append(" ");
            }

            // Send the message to every individual player.
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(messageBuilder.toString()));

            return true;
        }

        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        // Just making sure that Bukkit does not suggest random usernames etc.
        return new ArrayList<String>();
    }
}
