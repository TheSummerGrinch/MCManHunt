package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

public class OnAsyncPlayerChatEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(final AsyncPlayerChatEvent event) {

        PlayerState playerState = PlayerStateCache.getInstance()
                .getPlayerState(event.getPlayer().getUniqueId());

        Game game = GameCache.getInstance()
                .getGameFromCache(playerState.getGameName());

        if (!playerState.isInGame()
                || game == null
                || game.getGameFlowState().equals(GameFlowState.DEFAULT)
                || playerState.getPlayerRole().equals(PlayerRole.DEFAULT))
            return;

        if (playerState.getPlayerRole().equals(PlayerRole.RUNNER)) {

            String format;

            format = MessageFormat.format(ChatColor.DARK_GREEN + "[" + LanguageFileLoader
                    .getInstance().getString("runner") + "]"
                    + ChatColor.RESET +" <{0}> {1}",
                    event.getPlayer().getName(),
                    event.getMessage());

            Set<Player> recipients = new HashSet<>();

            for (PlayerState runner : game.getRunners()) {
                recipients.add(Bukkit.getPlayer(runner.getPlayerUUID()));
            }

            sendMessageToTeamMates(recipients, format);

        } else if (playerState.getPlayerRole().equals(PlayerRole.HUNTER)) {

            String format;

            format = MessageFormat.format(ChatColor.DARK_RED+ "[" + LanguageFileLoader
                    .getInstance().getString("hunter") + "]"
                    + ChatColor.RESET + " <{0}> {1}",
                    event.getPlayer().getName(),
                    event.getMessage());

            Set<Player> recipients = new HashSet<>();

            for (PlayerState hunter : game.getHunters()) {
                recipients.add(Bukkit.getPlayer(hunter.getPlayerUUID()));
            }

            sendMessageToTeamMates(recipients, format);

        }

        event.setCancelled(true);

    }

    private void sendMessageToTeamMates(@NotNull final Set<Player> recipients,
                                        @NotNull final String message) {
        for (Player player : recipients) {
            player.sendMessage(message);
        }
    }

}
