package io.github.thesummergrinch.mcmanhunt.commands.game.info;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ListGamesCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Set<String> gameNames = GameCache.getGameNames();
        if (gameNames.isEmpty()) { // If no games have been initialized, we simply tell the user as much.
            sender.sendMessage(LanguageFileLoader.getInstance().getString("no-games-initialized"));
        } else { // Otherwise, we list the initialized games by name, in order of initialization.
            final StringBuilder stringBuilder = new StringBuilder(LanguageFileLoader.getInstance().getString("list-initialized-games"));
            gameNames.forEach(gameName -> stringBuilder.append(gameName).append(", "));
            sender.sendMessage(stringBuilder.substring(0, stringBuilder.length() - 2));
        }
        return true;
    }
}
