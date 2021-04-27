package io.github.thesummergrinch.mcmanhunt.commands.game.op.settings;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ManHuntRuleCommandExecutor implements CommandExecutor, TabCompleter {

    private static List<String> AVAILABLE_MANHUNT_RULES = new ArrayList<String>() {
        {

            add("compass-enabled-in-nether");
            add("player-roles-randomized");
            add("headstart");
            add("difficulty");

        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender.isOp() && args.length >= 3) {

            final Game game = GameCache.getInstance().getGameFromCache(args[0]);

            if (game != null) {

                game.setManHuntRule(args[1], args[2]);

            } else {

                sender.sendMessage(LanguageFileLoader.getInstance().getString("rule-change-failed"));

            }

            return true;

        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> options = new ArrayList<>();
        if (args.length <= 1) {
            return GameCache.getInstance().getGameNamesAsList();
        } else if (args.length == 2) {
            return ManHuntRuleCommandExecutor.AVAILABLE_MANHUNT_RULES;
        } else if (args.length == 3) {
            switch (args[1].toLowerCase()) {
                case "compass-enabled-in-nether":
                case "player-roles-randomized":
                    options.add("true");
                    options.add("false");
                    return options;
                case "headstart":
                    options.add("0");
                    options.add("15");
                    options.add("30");
                    return options;
                case "difficulty":
                    options.add("peaceful");
                    options.add("easy");
                    options.add("normal");
                    options.add("hard");
                    return options;
                default:
                    return options;
            }
        } else {
            return options;
        }
    }
}
