package io.github.thesummergrinch.mcmanhunt.commands.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SayGlobalCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player && args.length > 0) {

            final StringBuilder messageBuilder =
                    new StringBuilder("<" +sender.getName() + "> ");

            for (String word : args) {
                messageBuilder.append(word);
                messageBuilder.append(" ");
            }

            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(messageBuilder.toString()));

            return true;
        }

        return false;
    }
}