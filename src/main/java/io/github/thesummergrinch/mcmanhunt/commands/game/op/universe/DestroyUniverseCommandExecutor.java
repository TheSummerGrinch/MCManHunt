package io.github.thesummergrinch.mcmanhunt.commands.game.op.universe;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.UniverseCache;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DestroyUniverseCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (sender.isOp() && args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) == null
                && UniverseCache.getInstance().getUniverse(args[0]) != null) {
            UniverseCache.getInstance().getUniverse(args[0]).setMarkedForDestruction(true);
        } else {
            sender.sendMessage(ChatColor.RED + "Could not destroy the specified Universe. " +
                    "The Universe may still be in use.");
        }
        return true;
    }
}
