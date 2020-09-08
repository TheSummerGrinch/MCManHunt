package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.PlayerMovementUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class OnPlayerLoginEventHandler implements Listener {

    /**
     * This EventHandler is deprecated and will be removed after the next snapshot-release.
     * @param event
     */
    @Deprecated
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLoginEvent(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        if (GameFlowUtilities.isGameInProgress()) {
            if (ManHuntUtilities.isRunner(player)) {
                if (GameFlowUtilities.isGamePaused()) {
                    PlayerMovementUtilities.restrictRunnerMovement();
                } else PlayerMovementUtilities.allowRunnerMovement();
            } else if (ManHuntUtilities.isHunter(player)) {
                if (GameFlowUtilities.isGamePaused()) {
                    PlayerMovementUtilities.restrictHunterMovement();
                } else PlayerMovementUtilities.allowHunterMovement();
            }
        }
    }

}
