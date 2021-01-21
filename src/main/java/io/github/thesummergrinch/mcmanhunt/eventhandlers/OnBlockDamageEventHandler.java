package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.Game;
import io.github.thesummergrinch.mcmanhunt.game.GameState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.jetbrains.annotations.NotNull;

public class OnBlockDamageEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockDamageEventHandler(@NotNull final BlockDamageEvent event) {
        final PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(event.getPlayer().getUniqueId());
        final Game game = GameCache.getInstance().getGameFromCache(playerState.getGameName());
        if (game == null) return;
        if ((!game.getGameState().equals(GameState.RUNNING) && playerState.isInGame()) || playerState.isMovementRestricted())
            event.setCancelled(true);
    }

}
