package io.github.thesummergrinch.mcmanhunt.commands.chat;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SayLobbyCommandExecutor implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player && args.length > 0) {

            final PlayerState playerState =
                    PlayerStateCache.getInstance()
                            .getPlayerState(((Player) sender).getUniqueId());

            if (!playerState.isInGame()) return false;

            final Game game = GameCache.getInstance()
                    .getGameFromCache(playerState.getGameName());

            if (game == null || game.getGameFlowState() == GameFlowState.DEFAULT)
                return false;

            final StringBuilder messageBuilder = new StringBuilder();

            for (String word : args) {
                messageBuilder.append(word);
                messageBuilder.append(" ");
            }

            String formattedString;

            if (playerState.getPlayerRole() == PlayerRole.RUNNER) {
                formattedString =
                        MessageFormat.format(ChatColor.DARK_GREEN + "[" + LanguageFileLoader
                                        .getInstance().getString("runner") + "]"
                                        + ChatColor.RESET + " <{0}> {1}",
                                playerState.getPlayerName(),
                                messageBuilder.toString());
            } else if (playerState.getPlayerRole() == PlayerRole.HUNTER) {
                formattedString =
                        MessageFormat.format(ChatColor.DARK_RED+ "[" + LanguageFileLoader
                                .getInstance().getString("hunter") + "]"
                                + ChatColor.RESET + " <{0}> {1}",
                        playerState.getPlayerName(),
                        messageBuilder.toString());
            } else {
                formattedString = MessageFormat.format("<{0}> {1}",
                        playerState.getPlayerName(), messageBuilder.toString());
            }
            game.getAllPlayers().forEach(playerStateObject -> {
                Bukkit.getPlayer(playerStateObject.getPlayerUUID())
                        .sendMessage(formattedString);
            });

            return true;

        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<String>();
    }
}
