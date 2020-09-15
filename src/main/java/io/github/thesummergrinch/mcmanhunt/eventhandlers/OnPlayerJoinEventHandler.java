package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class OnPlayerJoinEventHandler implements Listener {

    /**
     * When a player logs in, check whether or not a game is ongoing. If not, and the player is currently in a team,
     * reset the player-roles.
     *
     * @param event - The PlayerLoginEvent passed by the server.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoinEvent(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();
        if (!GameFlowUtilities.isGameInProgress()) {
            if (ManHuntUtilities.isRunner(playerUUID) || ManHuntUtilities.isHunter(playerUUID)) {
                ManHuntUtilities.resetplayerroles();
            }
        } else {
            if (ManHuntUtilities.isPlayerInSavedGame(playerUUID)) {
                if (ManHuntUtilities.isPlayerSavedRunner(playerUUID)) {
                    ManHuntUtilities.addRunner(player);
                } else if (ManHuntUtilities.isPlayerSavedHunter(playerUUID)) {
                    ManHuntUtilities.addRunner(player);
                }
                ManHuntUtilities.removePlayerFromSavedGameData(playerUUID);
            }
        }
    }

}
