package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.CompassStateCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.game.players.compasses.CompassState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OnPlayerInteractEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEvent(@NotNull final PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_AIR)) {
            final Player sender = event.getPlayer();
            ItemStack compass;
            if (sender.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
                compass = sender.getInventory().getItemInMainHand();
                if (compass.getItemMeta().getDisplayName().contains(" Tracker")) {
                    updateCompassMeta(sender, compass.getItemMeta().getDisplayName().split(" ")[0], (CompassMeta) compass.getItemMeta());
                }
            }
            if (sender.getInventory().getItemInOffHand().getType().equals(Material.COMPASS)) {
                compass = sender.getInventory().getItemInOffHand();
                if (compass.getItemMeta().getDisplayName().contains(" Tracker")) {
                    updateCompassMeta(sender, compass.getItemMeta().getDisplayName().split(" ")[0], (CompassMeta) compass.getItemMeta());
                }
            }
        }
    }

    private @NotNull CompassState getUpdatedCompassMeta(@NotNull final UUID targetUUID, @NotNull CompassMeta compassMeta) {
        CompassState compassState = CompassStateCache.getInstance().getCompassState(targetUUID);
        if (compassState == null) {
            compassState = new CompassState(targetUUID, compassMeta);
        }
        return compassState;
    }

    private void updateCompassMeta(@NotNull final Player sender, @NotNull final String targetName, @NotNull CompassMeta playerCompassMeta) {
        final Player target = Bukkit.getPlayer(targetName);
        if (target != null) {
            CompassState compassState = getUpdatedCompassMeta(target.getUniqueId(), playerCompassMeta);
            CompassMeta compassMeta = compassState.getCompassMeta();
            sender.getInventory().getItemInMainHand().setItemMeta(compassMeta);
        }
    }

}
