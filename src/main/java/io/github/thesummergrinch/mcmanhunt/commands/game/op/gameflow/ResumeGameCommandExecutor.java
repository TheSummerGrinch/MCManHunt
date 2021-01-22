package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
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
            // If the sender is a player and in an active game, they may forego the command argument.
            if (sender instanceof Player) {
                if (args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {
                    game = GameCache.getInstance().getGameFromCache(args[0]);
                    // If a game is found and it is currently paused, the game will be resumed and the players notified.
                    if (game.getGameFlowState().equals(GameFlowState.PAUSED)) {
                        game.resume();
                        game.broadcastToPlayers(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("game-resumed"));
                    } else {
                        sender.sendMessage(ChatColor.RED + MCManHuntStringCache.getInstance().getStringFromCache("game-not-paused"));
                    }
                    return true;
                }
                final PlayerState playerState = PlayerStateCache.getInstance()
                        .getPlayerState(((Player) sender).getUniqueId());
                if (playerState.isInGame()) {
                    game = GameCache.getInstance().getGameFromCache(playerState.getGameName());
                    if (game.getGameFlowState().equals(GameFlowState.PAUSED)) {
                        game.resume();
                        game.broadcastToPlayers(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("game-resumed"));
                    } else {
                        sender.sendMessage(ChatColor.RED + MCManHuntStringCache.getInstance().getStringFromCache("game-not-paused"));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + MCManHuntStringCache.getInstance().getStringFromCache("not-in-game-no-game-specified"));
                }
                return true;
            } else if (args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {
                game = GameCache.getInstance().getGameFromCache(args[0]);
                if (game.getGameFlowState().equals(GameFlowState.PAUSED)) {
                    game.resume();
                    game.broadcastToPlayers(ChatColor.GREEN + MCManHuntStringCache.getInstance().getStringFromCache("game-resumed"));
                }
            } else {
                sender.sendMessage(MCManHuntStringCache.getInstance().getStringFromCache("specified-game-not-exist"));
            }
            return true;
        }
        return false;
    }

}
