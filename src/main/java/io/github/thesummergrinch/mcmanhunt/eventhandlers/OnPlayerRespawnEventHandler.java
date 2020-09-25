package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerState;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.ArrayList;

public class OnPlayerRespawnEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawnEvent(final PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        final PlayerState playerState = UserCache.getInstance().getPlayerState(player.getUniqueId());
        if (playerState.getPlayerRole().equals(PlayerRole.HUNTER)) {
            int index = 0;
            for (PlayerState runner : UserCache.getInstance().getRunnerPlayerStates()) {
                ItemStack compass = new ItemStack(Material.COMPASS);
                runner.updateCompassMeta();
                compass.setItemMeta(runner.getCompassMeta());
                player.getInventory().setItem(index, compass);
                index += 1;
            }
        }
    }

}
