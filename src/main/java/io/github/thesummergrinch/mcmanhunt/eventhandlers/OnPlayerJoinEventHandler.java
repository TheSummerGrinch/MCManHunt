package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoinEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinEvent(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (UserCache.getInstance().userHasEntry(player.getUniqueId())) return;
        UserCache.getInstance().addPlayer(new PlayerState(player.getUniqueId()));
    }

}
