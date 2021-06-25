package io.github.thesummergrinch.mcmanhunt.commands.game.player;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JoinGameCommandExecutor implements TabExecutor {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player && args.length >= 1) {

            final Game game = GameCache.getInstance().getGameFromCache(args[0]);

            if (game == null || !game.getGameFlowState().equals(GameFlowState.DEFAULT)) return false;

            UUID playerUUID = ((Player) sender).getUniqueId();

            game.addPlayerToGame(playerUUID);
            sender.sendMessage(LanguageFileLoader.getInstance().getString("joined-game"));

            return true;

        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) return GameCache.getInstance().getStandbyGameNamesAsList();
        return new ArrayList<String>();
    }
}
