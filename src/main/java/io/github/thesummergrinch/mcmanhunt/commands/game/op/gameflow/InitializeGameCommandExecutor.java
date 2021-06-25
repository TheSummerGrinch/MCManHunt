package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.UniverseCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InitializeGameCommandExecutor implements TabExecutor {

    private final MCManHunt manhuntPlugin;

    public InitializeGameCommandExecutor(final MCManHunt manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    // The suggested names for ManHunt-games. May move to config.
    private static final List<String> SUGGESTED_PARAMETERS = new ArrayList<String>() {
        {

            add("manhunt");
            add("world");

        }
    };

    private static final List<String> DELETE_ON_STOP_PARAMETERS = new ArrayList<String>() {
        {
            add("true");
            add("false");
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

                if (args.length == 2) {
                    universe = new Universe(args[0], Boolean.parseBoolean(args[1]));
                } else {
                    universe = new Universe(args[0]);
                }
                sender.sendMessage(LanguageFileLoader.getInstance()
                        .getString("worlds-ready"));
            }

            if (GameCache.getInstance().getGameFromCache(args[0]) == null) {

                sender.sendMessage(LanguageFileLoader.getInstance()
                        .getString("init-game"));

                new Game(this.manhuntPlugin, universe);

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

        if (args.length == 1) {
            return InitializeGameCommandExecutor.SUGGESTED_PARAMETERS;
        } else if (args.length == 2) {
            return InitializeGameCommandExecutor.DELETE_ON_STOP_PARAMETERS;
        }
        return new ArrayList<String>();
    }
}
