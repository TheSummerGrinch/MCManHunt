package io.github.thesummergrinch.mcmanhunt.commands.game.op.universe;

import io.github.thesummergrinch.mcmanhunt.cache.UniverseCache;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SetDestroyUniverseOnStopCommandExecutor implements CommandExecutor, TabCompleter {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender.isOp() && args.length >= 2 && UniverseCache.getInstance().getUniverse(args[0]) != null
                && (args[1].equalsIgnoreCase(LanguageFileLoader.getInstance().getString("true"))
                || args[1].equalsIgnoreCase(LanguageFileLoader.getInstance().getString("false")))) {

            UniverseCache.getInstance().getUniverse(args[0]).setDestroyWhenGameIsStopped(Boolean.parseBoolean(args[1]));

            return true;

        } else return false;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) return UniverseCache.getInstance().getUniverseNamesAsList();
        return new ArrayList<>();
    }

}
