package io.github.thesummergrinch.mcmanhunt.commands.game.info;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ListGamesCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Set<String> gameNames = GameCache.getGameNames();
        if (gameNames.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No games have been initialized.");
        } else {
            final StringBuilder stringBuilder = new StringBuilder(ChatColor.GREEN + "The following games have been initialized: ");
            gameNames.forEach(gameName -> stringBuilder.append(gameName).append(", "));
            sender.sendMessage(stringBuilder.substring(0, stringBuilder.length() - 2));
        }
        return true;
    }
}
