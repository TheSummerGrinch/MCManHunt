package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.PlayerInventoryUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class OnPlayerLoginEventHandler implements Listener {

    /**
     * When a player logs in, check whether or not a game is ongoing. If not, and the player is currently in a team,
     * reset the player-roles and clear the player's inventory.
     * @param event - The PlayerLoginEvent passed by the server.
     */
    @EventHandler
    public void onPlayerLoginEvent(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        if (!GameFlowUtilities.isGameInProgress()) {
            if (ManHuntUtilities.isRunner(player) || ManHuntUtilities.isHunter(player)) {
                ManHuntUtilities.resetplayerroles();
                PlayerInventoryUtilities.clearPlayerInventory(player);
            }
        }
    }

}
