package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class OnPlayerMoveEventHandler implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMoveEvent(@NotNull final PlayerMoveEvent event) {
        PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(event.getPlayer().getUniqueId());
        if (!playerState.isInGame()) return;
        Game game = GameCache.getInstance().getGameFromCache(playerState.getGameName());
        if (event.getFrom().getY() > event.getTo().getY() || game == null) return;
        if (playerState.isMovementRestricted() || (!playerState.getPlayerRole().equals(PlayerRole.DEFAULT)
                && game.getGameFlowState().equals(GameFlowState.PAUSED))) event.setCancelled(true);
    }

}
