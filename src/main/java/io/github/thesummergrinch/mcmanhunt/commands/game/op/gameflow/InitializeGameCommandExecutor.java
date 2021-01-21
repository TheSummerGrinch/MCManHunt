package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.game.Game;
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
            sender.sendMessage(ChatColor.GREEN + "Initializing worlds...");
            Universe universe = new Universe(args[0]);
            sender.sendMessage(ChatColor.GREEN + "World initialized.\nInitializing Game...");
            Game game = new Game(universe);
            sender.sendMessage(ChatColor.GREEN + "Game initialized.");
            return true;
        }
        return false;
    }

}
