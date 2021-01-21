package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.events.ManHuntWinEvent;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class OnPlayerDeathEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeathEvent(@NotNull final PlayerDeathEvent event) {
        final PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(event.getEntity().getUniqueId());
        if (playerState.isInGame() && playerState.getPlayerRole().equals(PlayerRole.RUNNER)) {
            final Player player = Bukkit.getPlayer(playerState.getPlayerUUID());
            final Game game = GameCache.getInstance().getGameFromCache(playerState.getGameName());
            if (game.getGameFlowState().equals(GameFlowState.RUNNING)) {
                playerState.setPlayerRole(PlayerRole.SPECTATOR);
                player.setGameMode(GameMode.SPECTATOR);
                if (game.getNumberOfRunners() == 0L) {
                    Bukkit.getPluginManager().callEvent(new ManHuntWinEvent(game.getName(), game.getHunterUUIDs()));
                }
            }
        }
    }

}
