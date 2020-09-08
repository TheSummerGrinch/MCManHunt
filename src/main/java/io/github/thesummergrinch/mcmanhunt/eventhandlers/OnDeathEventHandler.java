package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnDeathEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(final PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (GameFlowUtilities.isGameInProgress() && ManHuntUtilities.isRunner(player)) {
            ManHuntUtilities.removeRunner(player.getName());
            player.setGameMode(GameMode.SPECTATOR);
            if (ManHuntUtilities.isRunnerMapEmpty()) {
                ManHuntUtilities.broadcastMessage("The Hunters win!");
                GameFlowUtilities.stopGame();
            }
        }
    }

}
