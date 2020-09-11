package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class OnDeathEventHandler implements Listener {

    /**
     * If a player on the Runner-team dies, they will be placed into spectator-mode and removed from the Runner-team.
     * If a player on the Hunter-team dies, nothing happens. Only works when the game is in progress.
     *
     * @param event - The PlayerDeathEvent passed by the Server.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final UUID playerUUID = player.getUniqueId();
        if (GameFlowUtilities.isGameInProgress() && ManHuntUtilities.isRunner(playerUUID)) {
            ManHuntUtilities.removeRunner(playerUUID);
            player.setGameMode(GameMode.SPECTATOR);
            if (ManHuntUtilities.isRunnerMapEmpty()) {
                ManHuntUtilities.broadcastMessage("The Hunters win!");
                GameFlowUtilities.stopGame();
            }
        }
    }

}
