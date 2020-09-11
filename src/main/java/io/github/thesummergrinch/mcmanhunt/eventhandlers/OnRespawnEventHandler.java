package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.PlayerInventoryUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OnRespawnEventHandler implements Listener {

    /**
     * Called when a player respawns. If the player is on the Hunter-team, the player will receive new tracking
     * compasses.
     *
     * @param event - The PlayerRespawnEvent passed by the server when a player respawns.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        if (ManHuntUtilities.isHunter(player.getUniqueId()) && GameFlowUtilities.isGameInProgress()) {
            PlayerInventoryUtilities.givePlayerHunterCompasses(player);
        }
    }

}
