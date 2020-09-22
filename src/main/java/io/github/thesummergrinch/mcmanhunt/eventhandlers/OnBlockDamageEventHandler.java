package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.game.GameController;
import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class OnBlockDamageEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamageEvent(final BlockDamageEvent event) {
        final PlayerState playerState = UserCache.getInstance().getPlayerState(event.getPlayer().getUniqueId());
        if (!playerState.getPlayerRole().equals(PlayerRole.DEFAULT)
                && (playerState.isMovementRestricted()
                || GameController.getInstance().getGameState().equals(GameController.GameState.PAUSED))) {
            event.setCancelled(true);
        }
    }

}
