package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class OnLogoutEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onLogoutEvent(final PlayerConnectionCloseEvent event) {
        final String playerName = event.getPlayerName();
        if (GameFlowUtilities.isGameInProgress() && !GameFlowUtilities.isGamePaused()) {
            if (ManHuntUtilities.isRunner(playerName)) {
                ManHuntUtilities.removeRunner(playerName);
                ManHuntUtilities.broadcastMessage(playerName + " left the game, and was removed from the runner-team.");
            } else if (ManHuntUtilities.isHunter(playerName)) {
                ManHuntUtilities.removeHunter(playerName);
                ManHuntUtilities.broadcastMessage(playerName + " left the game, and was removed from the hunter-team.");
            }
        }
    }

}
