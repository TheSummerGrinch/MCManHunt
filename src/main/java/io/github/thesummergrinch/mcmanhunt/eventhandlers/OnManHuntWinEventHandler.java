package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.events.ManHuntWinEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class OnManHuntWinEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onManHuntWinEvent(@NotNull final ManHuntWinEvent event) {
        event.getWinners().forEach(uuid -> {
            if (Bukkit.getPlayer(uuid) != null) {
                Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Congratulations! You won the MCManHunt-game!");
            }
        });
        GameCache.getInstance().getGameFromCache(event.getGameName()).stop();
    }

}
