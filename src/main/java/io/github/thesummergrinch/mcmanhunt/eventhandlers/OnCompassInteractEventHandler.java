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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEvent(final PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (ManHuntUtilities.isHunter(player) && GameFlowUtilities.isGameInProgress()) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)
                    && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().split(" ").length == 2) {
                if (!ManHuntUtilities.isRunner(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().split(" ")[0])) {
                    player.getInventory().setItemInMainHand(null);
                    return;
                }
                player.getInventory().getItemInMainHand()
                        .setItemMeta(PlayerInventoryUtilities.updateCompassMeta((CompassMeta) player.getInventory()
                                .getItemInMainHand().getItemMeta()));
            } else if (player.getInventory().getItemInOffHand().getType().equals(Material.COMPASS)
                    && player.getInventory().getItemInOffHand()
                    .getItemMeta().getDisplayName().split(" ").length == 2) {
                if (!ManHuntUtilities.isRunner(player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().split(" ")[0])) {
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
