package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StopGameCommandExecutor implements CommandExecutor {

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
                        sender.sendMessage(ChatColor.RED + MCManHuntStringCache.getInstance().getStringFromCache("specified-game-not-exist"));
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + MCManHuntStringCache.getInstance().getStringFromCache("specified-game-not-exist"));
                    return true;
                }
            }
            game.broadcastToPlayers(ChatColor.RED + MCManHuntStringCache.getInstance().getStringFromCache("game-stopping"));
            game.stop();
            return true;
        }
        return false;
    }

}
