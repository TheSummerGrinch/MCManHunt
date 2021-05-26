package io.github.thesummergrinch.mcmanhunt.commands.game.op.universe;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.UniverseCache;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DestroyUniverseCommandExecutor implements TabExecutor {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {

        if (sender.isOp() && args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) == null
                && UniverseCache.getInstance().getUniverse(args[0]) != null) {

            UniverseCache.getInstance().getUniverse(args[0]).setMarkedForDestruction(true);

        } else {

            sender.sendMessage(LanguageFileLoader.getInstance().getString("universe-destroy-failed"));

        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) return UniverseCache.getInstance().getUniverseNamesAsList();
        return new ArrayList<String>();
    }
}
