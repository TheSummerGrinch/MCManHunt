package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StopGameCommandExecutor implements CommandExecutor, TabCompleter {

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
                        return true;
                    }
                } else {
                    sender.sendMessage(LanguageFileLoader.getInstance().getString("specified-game-not-exist"));
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
        return GameCache.getInstance().getGameNamesAsList();
    }
}
