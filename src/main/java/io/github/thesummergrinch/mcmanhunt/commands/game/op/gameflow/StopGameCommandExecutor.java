package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StopGameCommandExecutor implements TabExecutor {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender.isOp()) {

            final Game game;

            if (args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {
                game = GameCache.getInstance().getGameFromCache(args[0]);
            } else {

                if (sender instanceof Player) {

                    final PlayerState playerState = PlayerStateCache.getInstance()
                            .getPlayerState(((Player) sender).getUniqueId());

                    if (playerState.isInGame()) {

                        game = GameCache.getInstance().getGameFromCache(playerState.getGameName());

                    } else {

                        sender.sendMessage(LanguageFileLoader.getInstance().getString("specified-game-not-exist"));

                        Bukkit.getServer().dispatchCommand(sender, "listgames");

                        return true;

                    }

                } else {

                    sender.sendMessage(LanguageFileLoader.getInstance().getString("specified-game-not-exist"));

                    Bukkit.getServer().dispatchCommand(sender, "listgames");

                    return true;

                }
            }

            game.broadcastToPlayers(LanguageFileLoader.getInstance().getString("game-stopping"));
            game.stop();

            return true;

        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if(args.length == 1) return GameCache.getInstance().getGameNamesAsList();
        return new ArrayList<String>();
    }
}
