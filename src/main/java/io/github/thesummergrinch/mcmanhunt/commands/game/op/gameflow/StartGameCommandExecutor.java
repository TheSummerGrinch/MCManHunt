package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StartGameCommandExecutor implements TabExecutor {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender.isOp()) {

            if (args.length >= 1) {

                Game game = GameCache.getInstance().getGameFromCache(args[0]);

                if (game.isEligibleForStart()) {

                    game.start();

                } else {

                    sender.sendMessage(LanguageFileLoader.getInstance().getString("not-enough-players"));

                }

                return true;

            } else if (sender instanceof Player) {

                PlayerState player = PlayerStateCache.getInstance().getPlayerState(((Player) sender).getUniqueId());

                if (player.isInGame()) {

                    Game game = GameCache.getInstance().getGameFromCache(player.getGameName());

                    if (game.getGameFlowState().equals(GameFlowState.DEFAULT)) {

                        if (game.isEligibleForStart()) {

                            game.start();

                        } else {

                            sender.sendMessage(LanguageFileLoader.getInstance().getString("not-enough-players"));

                        }

                        return true;

                    } else if (game.getGameFlowState().equals(GameFlowState.PAUSED)) {

                        game.resume();

                        return true;

                    }

                } else {

                    return false;

                }
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) return GameCache.getInstance().getGameNamesAsList();
        return new ArrayList<String>();
    }
}
