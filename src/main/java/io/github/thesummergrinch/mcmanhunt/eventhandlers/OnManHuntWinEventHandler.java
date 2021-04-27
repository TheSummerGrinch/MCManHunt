package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.events.ManHuntWinEvent;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class OnManHuntWinEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onManHuntWinEvent(@NotNull final ManHuntWinEvent event) {

        if (event.isCancelled()) return;

        event.getWinners().forEach(uuid -> {

            Player player = Bukkit.getPlayer(uuid);

            if (player != null && player.isOnline()) {

                Bukkit.getPlayer(uuid).sendMessage(LanguageFileLoader.getInstance().getString("win-message"));

            }
        });

        GameCache.getInstance().getGameFromCache(event.getGameName())
                .getGameState().initializeTeamWin();

        new BukkitRunnable() {

            @Override
            public void run() {

                GameCache.getInstance().getGameFromCache(event.getGameName()).stop();

            }
        }.runTaskLater(MCManHunt.getPlugin(MCManHunt.class), 200L);
    }
}
