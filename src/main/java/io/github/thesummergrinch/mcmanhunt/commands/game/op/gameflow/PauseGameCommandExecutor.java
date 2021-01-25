package io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PauseGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            // If the sender is a player and in an active game, they may forego specifying the game
            if (sender instanceof Player) { // However, specifying the game name is preferred.
                if (args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {
                    GameCache.getInstance().getGameFromCache(args[0]).pause();
                    return true;
                }
                final PlayerState playerState = PlayerStateCache.getInstance()
                        .getPlayerState(((Player) sender).getUniqueId());
                if (playerState.isInGame()) {
                    GameCache.getInstance().getGameFromCache(playerState.getGameName()).pause();
                } else {
                    // If neither options return a game, the sender will be notified of this.
                    sender.sendMessage(/*ChatColor.RED + */MCManHuntStringCache.getInstance()
                            .getStringFromCache("not-in-game-no-game-specified"));
                }
                return true;
            } else if (args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {
                // If the sender is not a player, the sender must specify the game name in the command argument.
                GameCache.getInstance().getGameFromCache(args[0]).pause();
                return true;
            } else {
                // If the game name does not link to an existing game, the sender is notified.
                sender.sendMessage(MCManHuntStringCache.getInstance().getStringFromCache("specified-game-not-exist"));
            }
        }
        return false;
    }
}
