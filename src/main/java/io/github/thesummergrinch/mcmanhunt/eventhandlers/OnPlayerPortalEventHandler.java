package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerState;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class OnPlayerPortalEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPortalEvent(final PlayerPortalEvent event) {
        PlayerState playerState = UserCache.getInstance().getPlayerState(event.getPlayer().getUniqueId());
        if (event.getFrom().getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
            playerState.getCompassMeta().setLodestone(event.getFrom());
        }
    }

}
