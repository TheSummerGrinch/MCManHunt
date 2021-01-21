package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.GameState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class OnPlayerDamagedEventHandler implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDamagedEvent(@NotNull final EntityDamageEvent event) {
        final PlayerState playerState;
        if (event.getEntity() instanceof Player) {
            playerState = PlayerStateCache.getInstance().getPlayerState(event.getEntity().getUniqueId());
            if ((playerState.isInGame() && !GameCache.getInstance()
                    .getGameFromCache(playerState.getGameName()).getGameState().equals(GameState.RUNNING))
                    || playerState.isMovementRestricted()) {
                event.setCancelled(true);
            }
        }
    }

}