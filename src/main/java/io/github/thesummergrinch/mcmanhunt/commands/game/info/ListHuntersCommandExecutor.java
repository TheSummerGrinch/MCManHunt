package io.github.thesummergrinch.mcmanhunt.commands.game.info;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.Game;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class ListHuntersCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final Game game;
        if (sender instanceof Player) {
            final PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(((Player) sender).getUniqueId());
            game = (args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null)
                    ? GameCache.getInstance().getGameFromCache(args[0])
                    : GameCache.getInstance().getGameFromCache(playerState.getGameName());
            if (game == null) {
                sender.sendMessage(ChatColor.RED + "You are not in a game, and the specified game does not exist.");
            } else {
                final StringBuilder stringBuilder = new StringBuilder("The Hunter-team consists of: ");
                HashSet<PlayerState> hunters = (HashSet<PlayerState>) game.getHunters();
                hunters.forEach(hunter -> {
                    stringBuilder.append(hunter.getPlayerName()).append(", ");
                });
                stringBuilder.substring(0, stringBuilder.length() - 2);
                sender.sendMessage(stringBuilder.toString());
            }
            return true;
        } else {
            if (args.length >= 1) {
                game = GameCache.getInstance().getGameFromCache(args[0]);
                if (game != null) {
                    sender.sendMessage(""); //TODO add gamelist
                }
            }
        }
        return false;
    }
}
