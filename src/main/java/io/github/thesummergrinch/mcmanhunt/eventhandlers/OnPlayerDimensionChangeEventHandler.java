package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.inventory.meta.CompassMeta;

public class OnPlayerDimensionChangeEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDimensionChangeEvent(final EntityPortalExitEvent event) {
        if (event.getEntityType().equals(EntityType.PLAYER)) {
            return;
        }
        final Player player = (Player) event.getEntity();
        if (ManHuntUtilities.isHunter(player)) {
            if (player.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                player.getInventory().forEach((item) -> {
                    if (item.getType().equals(Material.COMPASS) && item.getItemMeta().getDisplayName().split(" ").length > 1) {
                        final CompassMeta compassMeta = (CompassMeta) item.getItemMeta();
                        compassMeta.setLodestoneTracked(true);
                        item.setItemMeta(compassMeta);
                    }
                });
            } else if (player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                player.getInventory().forEach((item) -> {
                    if (item.getType().equals(Material.COMPASS) && item.getItemMeta().getDisplayName().split(" ").length > 1) {
                        final CompassMeta compassMeta = (CompassMeta) item.getItemMeta();
                        compassMeta.setLodestoneTracked(false);
                        item.setItemMeta(compassMeta);
                    }
                });
            }
        }
    }

}
