package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class InitializeGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // If the sender is OP, and specified the name of the game, they will be allowed to initialize a game.
        if (sender.isOp() && args.length >= 1) {
            sender.sendMessage(LanguageFileLoader.getInstance().getString("init-worlds"));
            Universe universe = new Universe(args[0]);
            sender.sendMessage(LanguageFileLoader.getInstance().getString("worlds-ready"));
            sender.sendMessage(LanguageFileLoader.getInstance().getString("init-game"));
            new Game(universe);
            sender.sendMessage(LanguageFileLoader.getInstance().getString("game-ready"));
            return true;
        }
        return false;
    }

}
