package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.PlayerInventoryUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class OnPlayerLoginEventHandler implements Listener {

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
