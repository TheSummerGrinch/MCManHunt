package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (ManHuntUtilities.isRunner(player) && ManHuntUtilities.RUNNER_MOVEMENT_RESTRICTED.get()) {
            event.setCancelled(true);
        } else if (ManHuntUtilities.isHunter(player) && ManHuntUtilities.HUNTER_MOVEMENT_RESTRICTED.get()) {
            event.setCancelled(true);
        }
    }

}
