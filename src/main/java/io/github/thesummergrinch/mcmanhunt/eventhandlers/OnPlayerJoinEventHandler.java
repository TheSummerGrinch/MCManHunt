package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class OnPlayerJoinEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinEvent(final PlayerJoinEvent event) {
        final UUID playerUUID = event.getPlayer().getUniqueId();
        if (PlayerStateCache.getInstance().getPlayerState(playerUUID) == null) {
            new PlayerState(playerUUID);
        }
    }

}
