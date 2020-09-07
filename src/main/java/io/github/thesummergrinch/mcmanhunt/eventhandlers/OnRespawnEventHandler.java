package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OnRespawnEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(final PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (ManHuntUtilities.isHunter(player) && ManHuntUtilities.GAME_IN_PROGRESS.get()) {
            ManHuntUtilities.givePlayerHunterCompasses(player);
        }
    }

}
