package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

public class OnCompassInteractEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractEvent(final PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (ManHuntUtilities.isHunter(player) && ManHuntUtilities.GAME_IN_PROGRESS.get()) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)
                    && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().split(" ").length == 2) {
                player.getInventory().getItemInMainHand()
                        .setItemMeta(ManHuntUtilities.updateCompassMeta((CompassMeta) player.getInventory()
                                .getItemInMainHand().getItemMeta()));
            } else if (player.getInventory().getItemInOffHand().getType().equals(Material.COMPASS)
                    && player.getInventory().getItemInOffHand()
                    .getItemMeta().getDisplayName().split(" ").length == 2) {
                player.getInventory().getItemInOffHand()
                        .setItemMeta(ManHuntUtilities.updateCompassMeta((CompassMeta) player.getInventory()
                                .getItemInOffHand().getItemMeta()));
            }
        }
    }

}
