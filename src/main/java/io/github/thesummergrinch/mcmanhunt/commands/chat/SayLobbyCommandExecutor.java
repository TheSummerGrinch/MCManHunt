package io.github.thesummergrinch.mcmanhunt.commands.chat;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.Bukkit;
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
        // Checking that the CommandSender is a Player, and the message is
        // not empty.
        if (sender instanceof Player && args.length > 0) {

            // Getting the Player's corresponding PlayerState
            final PlayerState playerState =
                    PlayerStateCache.getInstance()
                            .getPlayerState(((Player) sender).getUniqueId());

            // If the Player is not in a Game, the command will fail.
            // TODO add meaningful response to player.
            if (!playerState.isInGame()) return false;

            // Get the Game-object
            final Game game = GameCache.getInstance()
                    .getGameFromCache(playerState.getGameName());

            // If Player is not in a Game, or the Game has not started yet,
            // the command fails.
            // TODO add meaningful response to player.
            if (game == null || game.getGameFlowState() == GameFlowState.DEFAULT)
                return false;

            //Prepare StringBuilder for the formatted message.
            final StringBuilder messageBuilder = new StringBuilder();

            // Appending each word to the StringBuilder
            for (String word : args) {
                messageBuilder.append(word);
                messageBuilder.append(" ");
            }

            String formattedString;

            // If the Player is a Runner, we give the Player a green
            // [Runner]-tag. If the Player is a Hunter, we give the Player a
            // red [Hunter]-tag. If the Player is neither Runner, not Hunter,
            // we just format the message as normal.
            formattedString =
                    MessageFormat.format(playerState.getPlayerRole().getRolePrefix() + "<{0" +
                                    "}> {1}",
                            playerState.getPlayerName(), messageBuilder.toString());
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
        // Just making sure Bukkit does not suggest random usernames etc.
        return new ArrayList<String>();
    }
}
