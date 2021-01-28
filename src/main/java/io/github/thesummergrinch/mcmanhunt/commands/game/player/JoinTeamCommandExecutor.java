package io.github.thesummergrinch.mcmanhunt.commands.game.player;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinTeamCommandExecutor implements CommandExecutor {
    //TODO rewrite this to clean it.
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        final PlayerState playerState;
        if (sender instanceof Player) {
            playerState = PlayerStateCache.getInstance().getPlayerState(((Player) sender).getUniqueId());
            if (!playerState.isInGame()) {
                sender.sendMessage(MCManHuntStringCache.getInstance().getStringFromCache("join-team-failed"));
                return true;
            }
            if (label.equals(MCManHuntStringCache.getInstance().getStringFromCache("join-hunters"))
                    || args.length >= 1 && args[0].equals(MCManHuntStringCache.getInstance().getStringFromCache("hunters"))) {
                playerState.setPlayerRole(PlayerRole.HUNTER);
                GameCache.getInstance().getGameFromCache(playerState.getGameName())
                        .broadcastToPlayers(/*ChatColor.RED + */sender.getName() + MCManHuntStringCache.getInstance()
                                .getStringFromCache("joined-hunters-message"));
            } else if (label.equals(MCManHuntStringCache.getInstance().getStringFromCache("join-runners"))
                    || args.length >= 1 && args[0].equals(MCManHuntStringCache.getInstance().getStringFromCache("runners"))) {
                playerState.setPlayerRole(PlayerRole.RUNNER);
                GameCache.getInstance().getGameFromCache(playerState.getGameName())
                        .broadcastToPlayers(/*ChatColor.GREEN + */sender.getName() + MCManHuntStringCache.getInstance()
                                .getStringFromCache("joined-runners-message"));
            } else if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("runners")) {
                    playerState.setPlayerRole(PlayerRole.RUNNER);
                    GameCache.getInstance().getGameFromCache(playerState.getGameName())
                            .broadcastToPlayers(/*ChatColor.GREEN + */sender.getName() + MCManHuntStringCache.getInstance()
                                    .getStringFromCache("joined-runners-message"));
                } else if (args[0].equalsIgnoreCase("hunters")) {
                    playerState.setPlayerRole(PlayerRole.HUNTER);
                    GameCache.getInstance().getGameFromCache(playerState.getGameName())
                            .broadcastToPlayers(/*ChatColor.RED + */sender.getName() + MCManHuntStringCache.getInstance()
                                    .getStringFromCache("joined-hunters-message"));
                } else {
                    return false;
                }
                return true;
            } else {
                sender.sendMessage(/*ChatColor.RED + */MCManHuntStringCache.getInstance().getStringFromCache("join-team-incorrect-argument"));
            }
            return true;
        }
        return false;
    }
}
