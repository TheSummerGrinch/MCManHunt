package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartGameCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            if (args.length >= 1) {
                GameCache.getInstance().getGameFromCache(args[0]).start();
                return true;
            } else if (sender instanceof Player) {
                PlayerState player = PlayerStateCache.getInstance().getPlayerState(((Player) sender).getUniqueId());
                if (player.isInGame()) {
                    Game game = GameCache.getInstance().getGameFromCache(player.getGameName());
                    if (game.getGameFlowState().equals(GameFlowState.DEFAULT)) {
                        game.start();
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
}
