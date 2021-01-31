package io.github.thesummergrinch.mcmanhunt.commands.game.player;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
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

public class JoinTeamCommandExecutor implements CommandExecutor, TabCompleter {
    //TODO rewrite this to clean it.
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        final PlayerState playerState;
        if (sender instanceof Player) {
            playerState = PlayerStateCache.getInstance().getPlayerState(((Player) sender).getUniqueId());
            if (!playerState.isInGame()) {
                sender.sendMessage(LanguageFileLoader.getInstance().getString("join-team-failed"));
                return true;
            }
            if (label.equals(LanguageFileLoader.getInstance().getString("join-hunters"))
                    || args.length >= 1 && args[0].equals(LanguageFileLoader.getInstance().getString("hunters"))) {
                playerState.setPlayerRole(PlayerRole.HUNTER);
                GameCache.getInstance().getGameFromCache(playerState.getGameName())
                        .broadcastToPlayers(sender.getName() + LanguageFileLoader.getInstance().getString("joined-hunters-message"));
                return true;
            } else if (label.equals(LanguageFileLoader.getInstance().getString("join-runners"))
                    || args.length >= 1 && args[0].equals(LanguageFileLoader.getInstance().getString("runners"))) {
                playerState.setPlayerRole(PlayerRole.RUNNER);
                GameCache.getInstance().getGameFromCache(playerState.getGameName())
                        .broadcastToPlayers(sender.getName() + LanguageFileLoader.getInstance().getString("joined-runners-message"));
                return true;
            } else if (args.length >= 1) {
                if (args[0].equalsIgnoreCase(LanguageFileLoader.getInstance().getString("runners"))) {
                    playerState.setPlayerRole(PlayerRole.RUNNER);
                    GameCache.getInstance().getGameFromCache(playerState.getGameName())
                            .broadcastToPlayers(sender.getName() + LanguageFileLoader.getInstance().getString("joined-runners-message"));
                    return true;
                } else if (args[0].equalsIgnoreCase(LanguageFileLoader.getInstance().getString("hunters"))) {
                    playerState.setPlayerRole(PlayerRole.HUNTER);
                    GameCache.getInstance().getGameFromCache(playerState.getGameName())
                            .broadcastToPlayers(sender.getName() + LanguageFileLoader.getInstance().getString("joined-hunters-message"));
                    return true;
                } else {
                    return false;
                }
            } else {
                sender.sendMessage(LanguageFileLoader.getInstance().getString("join-team-incorrect-argument"));
            }
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> suggestedArgument = new ArrayList<>();
        if (alias.equalsIgnoreCase("jointeam")) {
            suggestedArgument.add(LanguageFileLoader.getInstance().getString("hunters"));
            suggestedArgument.add(LanguageFileLoader.getInstance().getString("runners"));
        }
        return suggestedArgument;
    }
}
