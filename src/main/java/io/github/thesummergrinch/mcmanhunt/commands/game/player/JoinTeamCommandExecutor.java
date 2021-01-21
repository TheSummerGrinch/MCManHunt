package io.github.thesummergrinch.mcmanhunt.commands.game.player;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
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
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        final PlayerState playerState;
        if (sender instanceof Player) {
            playerState = PlayerStateCache.getInstance().getPlayerState(((Player) sender).getUniqueId());
            if (!playerState.isInGame()) {
                sender.sendMessage("You are not in registered to a game. Please join a game before using this command!");
                return true;
            }
            if (label.equals("joinhunters") || args.length >= 1 && args[0].equals("hunters")) {
                playerState.setPlayerRole(PlayerRole.HUNTER);
                GameCache.getInstance().getGameFromCache(playerState.getGameName())
                        .broadcastToPlayers(ChatColor.RED + sender.getName() + " has joined the Hunters!");
            } else if (label.equals("joinrunners") || args.length >= 1 && args[0].equals("runners")) {
                playerState.setPlayerRole(PlayerRole.RUNNER);
                GameCache.getInstance().getGameFromCache(playerState.getGameName())
                        .broadcastToPlayers(ChatColor.GREEN + sender.getName() + " has joined the Runners!");
            } else {
                sender.sendMessage(ChatColor.RED + "The command-argument is incorrect. " +
                        "Please use \"/jointeam hunters\" or \"/jointeam runners\".");
            }
            return true;
        }
        return false;
    }
}
