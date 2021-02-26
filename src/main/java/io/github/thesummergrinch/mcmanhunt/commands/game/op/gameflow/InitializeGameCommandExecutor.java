package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.UniverseCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InitializeGameCommandExecutor implements CommandExecutor, TabCompleter {

    // The suggested names for ManHunt-games. May move to config.
    private static final List<String> SUGGESTED_PARAMETERS = new ArrayList<String>() {
        {
            add("manhunt");
            add("world");
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // If the sender is OP, and specified the name of the game, they will be allowed to initialize a game.
        if (sender.isOp() && args.length >= 1) {
            Universe universe =
                    UniverseCache.getInstance().getUniverse(args[0]);

            // If there is no Universe with the given name, we create it.
            if (universe == null) {

                sender.sendMessage(LanguageFileLoader.getInstance()
                        .getString("init-worlds"));

                universe = new Universe(args[0]);

                sender.sendMessage(LanguageFileLoader.getInstance()
                        .getString("worlds-ready"));
            }

            if (GameCache.getInstance().getGameFromCache(args[0]) == null) {

                sender.sendMessage(LanguageFileLoader.getInstance()
                        .getString("init-game"));

                new Game(universe);

            }

            sender.sendMessage(LanguageFileLoader.getInstance()
                    .getString("game-ready"));

            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return InitializeGameCommandExecutor.SUGGESTED_PARAMETERS;
    }
}
