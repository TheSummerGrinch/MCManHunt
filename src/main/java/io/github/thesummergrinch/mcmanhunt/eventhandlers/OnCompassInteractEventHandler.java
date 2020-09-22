package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class OnCompassInteractEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCompassInteractEvent(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();
        if (!UserCache.getInstance().getPlayerState(playerUUID).getPlayerRole().equals(PlayerRole.HUNTER)) return;
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (itemInMainHand.getType().equals(Material.COMPASS)
                && itemInMainHand.getItemMeta().getDisplayName().contains("Tracker")) {
            PlayerState targetPlayerState = getTargetPlayerState(itemInMainHand.getItemMeta().getDisplayName().split(" ")[0]);
            targetPlayerState.updateCompassMeta();
            itemInMainHand.setItemMeta(targetPlayerState.getCompassMeta());
        } else if (itemInOffHand.getType().equals(Material.COMPASS)
                && itemInOffHand.getItemMeta().getDisplayName().contains("Tracker")) {
            PlayerState targetPlayerState = getTargetPlayerState(itemInOffHand.getItemMeta().getDisplayName().split(" ")[0]);
            targetPlayerState.updateCompassMeta();
            itemInMainHand.setItemMeta(targetPlayerState.getCompassMeta());
        }
    }

    private PlayerState getTargetPlayerState(final String targetName) {
        return UserCache.getInstance().getPlayerState(UserCache.getInstance().getUniqueIDByName(targetName));
    }

}
