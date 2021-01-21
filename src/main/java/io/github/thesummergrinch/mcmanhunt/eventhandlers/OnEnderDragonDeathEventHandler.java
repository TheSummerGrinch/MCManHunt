package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.events.ManHuntWinEvent;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameFlowState;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OnEnderDragonDeathEventHandler implements @NotNull Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnderDragonDeathEvent(@NotNull final EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            @Nullable final Game game = GameCache.getInstance()
                    .getGameFromCache(event.getEntity().getLocation().getWorld().getName().split("_")[0]);
            if (game != null && game.getGameFlowState().equals(GameFlowState.RUNNING)) {
                Bukkit.getServer().getPluginManager()
                        .callEvent(new ManHuntWinEvent(game.getName(), game.getRunnerUUIDs()));
            }
        }
    }

}
