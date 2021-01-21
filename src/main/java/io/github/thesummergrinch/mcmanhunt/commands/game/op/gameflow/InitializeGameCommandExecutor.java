package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class InitializeGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp() && args.length >= 1) {
            sender.sendMessage(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("init-worlds"));
            Universe universe = new Universe(args[0]);
            sender.sendMessage(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("worlds-ready"));
            sender.sendMessage(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("init-game"));
            Game game = new Game(universe);
            sender.sendMessage(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("game-ready"));
            return true;
        }
        return false;
    }

}
