package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.events.ManHuntWinEvent;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class OnPlayerLeaveEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(final PlayerQuitEvent event) {

        final PlayerState playerState =
                PlayerStateCache.getInstance()
                        .getPlayerState(event.getPlayer().getUniqueId());

        if (playerState.isInGame()) {
            final Game game = GameCache.getInstance()
                    .getGameFromCache(playerState.getGameName());
            if (game.getGameFlowState() == GameFlowState.RUNNING) {
                if (playerState.getPlayerRole() == PlayerRole.HUNTER) {
                    AtomicBoolean quit = new AtomicBoolean(true);
                    game.getHunters().forEach((playerState1) -> {
                        if (playerState1.isOnline() && playerState1.getPlayerUUID()
                                != playerState.getPlayerUUID()) {
                            quit.set(false);
                            return;
                        }
                    });
                    if (quit.get()) {
                        Bukkit.getPluginManager().callEvent(
                                new ManHuntWinEvent
                                        (
                                                game.getName(),
                                                game.getRunnerUUIDs()
                                        )
                        );
                    }
                } else if (playerState.getPlayerRole() == PlayerRole.RUNNER) {
                    AtomicBoolean quit = new AtomicBoolean(true);
                    game.getRunners().forEach((playerState1) -> {
                        if (playerState1.isOnline() && playerState1.getPlayerUUID()
                                != playerState.getPlayerUUID()) {
                            quit.set(false);
                            return;
                        }
                    });
                    if (quit.get()) {
                        Bukkit.getPluginManager().callEvent(
                                new ManHuntWinEvent
                                        (
                                                game.getName(),
                                                game.getHunterUUIDs()
                                        )
                        );
                    }
                }
            }
        }
    } // TODO Test this

}
