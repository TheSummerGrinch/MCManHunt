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
        }
    };

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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return ManHuntRuleCommandExecutor.AVAILABLE_MANHUNT_RULES;
    }
}
