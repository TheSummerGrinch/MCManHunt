package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StartGameCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp() && args.length >= 1) {
            GameCache.getInstance().getGameFromCache(args[0]).start();
            return true;
        }
        return false;
    }
}
