package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.PlayerInventoryUtilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.CompassMeta;

public class OnCompassInteractEventHandler implements Listener {

    /**
     * If a Hunter interacts with air, and they hold a tracking compass in either hand, the compass' ItemMeta will be
     * updated to track the location of the corresponding Runner.
     *
     * @param event - The PlayerInteractEvent passed by the Server.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEvent(final PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (ManHuntUtilities.isHunter(player.getUniqueId()) && GameFlowUtilities.isGameInProgress()) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)
                    && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().split(" ").length == 2) {
                if (!ManHuntUtilities.isRunner(ManHuntUtilities.getPlayerUUID(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().split(" ")[0]))) {
                    player.getInventory().setItemInMainHand(null);
                    return;
                }
                player.getInventory().getItemInMainHand()
                        .setItemMeta(PlayerInventoryUtilities.updateCompassMeta((CompassMeta) player.getInventory()
                                .getItemInMainHand().getItemMeta()));
            } else if (player.getInventory().getItemInOffHand().getType().equals(Material.COMPASS)
                    && player.getInventory().getItemInOffHand()
                    .getItemMeta().getDisplayName().split(" ").length == 2) {
                if (!ManHuntUtilities.isRunner(ManHuntUtilities.getPlayerUUID(player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().split(" ")[0]))) {
                    player.getInventory().setItemInOffHand(null);
                    return;
                }
                player.getInventory().getItemInOffHand()
                        .setItemMeta(PlayerInventoryUtilities.updateCompassMeta((CompassMeta) player.getInventory()
                                .getItemInOffHand().getItemMeta()));
            }
        }
    }

}
