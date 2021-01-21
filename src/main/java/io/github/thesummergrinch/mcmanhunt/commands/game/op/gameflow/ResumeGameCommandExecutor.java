package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.Game;
import io.github.thesummergrinch.mcmanhunt.game.GameState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResumeGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            final Game game;
            if (sender instanceof Player) {
                if (args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {
                    game = GameCache.getInstance().getGameFromCache(args[0]);
                    if (game.getGameState().equals(GameState.PAUSED)) {
                        game.resume();
                        game.broadcastToPlayers(ChatColor.GREEN + "The game was resumed.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "The specified game is not paused.");
                    }
                    return true;
                }
                final PlayerState playerState = PlayerStateCache.getInstance()
                        .getPlayerState(((Player) sender).getUniqueId());
                if (playerState.isInGame()) {
                    game = GameCache.getInstance().getGameFromCache(playerState.getGameName());
                    if (game.getGameState().equals(GameState.PAUSED)) {
                        game.resume();
                        game.broadcastToPlayers(ChatColor.GREEN + "The game was resumed.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "The specified game is not paused.");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "The specified game does not exist, and you are not in a game.");
                }
                return true;
            } else if (args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {
                game = GameCache.getInstance().getGameFromCache(args[0]);
                if (game.getGameState().equals(GameState.PAUSED)) {
                    game.resume();
                    game.broadcastToPlayers(ChatColor.GREEN + "The game was resumed.");
                }
            } else {
                sender.sendMessage("The specified game does not exist.");
            }
            return true;
        }
        return false;
    }

}
