package io.github.thesummergrinch.mcmanhunt.commands.game.info;

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

import java.util.HashSet;

public class ListRunnersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final Game game;
        if (sender instanceof Player) {
            final PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(((Player) sender).getUniqueId());
            game = (args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null)
                    ? GameCache.getInstance().getGameFromCache(args[0])
                    : GameCache.getInstance().getGameFromCache(playerState.getGameName());
            if (game == null) {
                sender.sendMessage(ChatColor.RED + MCManHuntStringCache.getInstance().getStringFromCache("not-in-game-no-game-specified"));
            } else {
                final StringBuilder stringBuilder = new StringBuilder(MCManHuntStringCache.getInstance().getStringFromCache("list-runners"));
                HashSet<PlayerState> runners = (HashSet<PlayerState>) game.getRunners();
                runners.forEach(runner -> stringBuilder.append(runner.getPlayerName()).append(", "));
                stringBuilder.substring(0, stringBuilder.length() - 2);
                sender.sendMessage(stringBuilder.toString());
            }
            return true;
        } else {
            if (args.length >= 1) {
                game = GameCache.getInstance().getGameFromCache(args[0]);
                if (game != null) sender.sendMessage(""); //TODO gamelist
            }
        }
        return false;
    }
}
