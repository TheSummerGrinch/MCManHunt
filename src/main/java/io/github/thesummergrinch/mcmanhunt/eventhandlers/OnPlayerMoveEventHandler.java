package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.PlayerMovementUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEventHandler implements Listener {

    /**
     * Called when a player moves. The movement of players is supposed to be restricted, the event will be cancelled.
     * Cancelling the event will prevent the players from moving and looking around, thus keeping them from gaining an
     * unfair advantage.
     *
     * @param event - The PlayerMoveEvent passed by the Server, when a player attempts to move.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (!GameFlowUtilities.isGameInProgress()) return;
        if (ManHuntUtilities.isRunner(player) && PlayerMovementUtilities.isRunnerMovementRestricted()) {
            event.setCancelled(true);
        } else if (ManHuntUtilities.isHunter(player) && PlayerMovementUtilities.isHunterMovementRestricted()) {
            event.setCancelled(true);
        }
    }

}
