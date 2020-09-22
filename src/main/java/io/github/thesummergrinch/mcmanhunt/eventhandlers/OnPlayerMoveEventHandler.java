package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.game.GameController;
import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMoveEvent(final PlayerMoveEvent event) {
        final PlayerState playerState = UserCache.getInstance().getPlayerState(event.getPlayer().getUniqueId());
        if (event.getFrom().getY() < event.getTo().getY()) return;
        if (GameController.getInstance().getGameState().equals(GameController.GameState.PAUSED)
                && (playerState.getPlayerRole().equals(PlayerRole.RUNNER)
                || playerState.getPlayerRole().equals(PlayerRole.HUNTER))) {
            event.setCancelled(true);
        } else if (GameController.getInstance().getGameState().equals(GameController.GameState.RUNNING)) {
            if (playerState.isMovementRestricted()
                    && event.getFrom().getY() <= event.getTo().getY()) event.setCancelled(true);
        }
    }

}
