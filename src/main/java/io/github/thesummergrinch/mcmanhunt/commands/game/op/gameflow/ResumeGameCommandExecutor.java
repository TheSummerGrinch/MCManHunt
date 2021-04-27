package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ResumeGameCommandExecutor implements CommandExecutor, TabCompleter {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender.isOp()) {

            final Game game;

            // If the sender is a player and in an active game, they may forego the command argument.
            if (sender instanceof Player) {

                if (args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {

                    game = GameCache.getInstance().getGameFromCache(args[0]);

                    // If a game is found and it is currently paused, the game will be resumed and the players notified.
                    if (game.getGameFlowState().equals(GameFlowState.PAUSED)) {

                        game.resume();
                        game.broadcastToPlayers(LanguageFileLoader.getInstance().getString("game-resumed"));

                    } else {

                        sender.sendMessage(LanguageFileLoader.getInstance().getString("game-not-paused"));

                    }

                    return true;

                }

                final PlayerState playerState = PlayerStateCache.getInstance()
                        .getPlayerState(((Player) sender).getUniqueId());

                if (playerState.isInGame()) {

                    game = GameCache.getInstance().getGameFromCache(playerState.getGameName());

                    if (game.getGameFlowState().equals(GameFlowState.PAUSED)) {

                        game.resume();
                        game.broadcastToPlayers(LanguageFileLoader.getInstance().getString("game-resumed"));

                    } else {

                        sender.sendMessage(LanguageFileLoader.getInstance().getString("game-not-paused"));

                    }
                } else {

                    sender.sendMessage(LanguageFileLoader.getInstance().getString("not-in-game-no-game-specified"));

                }

                return true;

            } else if (args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {

                game = GameCache.getInstance().getGameFromCache(args[0]);

                if (game.getGameFlowState().equals(GameFlowState.PAUSED)) {

                    game.resume();
                    game.broadcastToPlayers(LanguageFileLoader.getInstance().getString("game-resumed"));

                }

            } else {

                sender.sendMessage(LanguageFileLoader.getInstance().getString("specified-game-not-exist"));

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

        if(args.length == 1) return GameCache.getInstance().getGameNamesAsList();
        return new ArrayList<>();
    }
}
