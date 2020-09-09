package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
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

    /**
     * Updates the tracking compasses, so they will not point towards the Runners in the Nether. Only does so if the
     * the Entity is a Player, the Player is a Hunter, and the Environment is the Nether.
     *
     * @param event - The EntityPortalExitEvent passed by the Server.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDimensionChangeEvent(final EntityPortalExitEvent event) {
        if (!event.getEntityType().equals(EntityType.PLAYER) || !GameFlowUtilities.isGameInProgress()) {
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
