package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerState;
import org.bukkit.Material;
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
        final PlayerState playerState = UserCache.getInstance().getPlayerState(event.getPlayer().getUniqueId());
        if (playerState.getPlayerRole().equals(PlayerRole.HUNTER)) {
            ArrayList<CompassMeta> compassMetas = new ArrayList<>();
            UserCache.getInstance().getRunnerPlayerStates().forEach(playerState1 -> compassMetas.add(playerState.getCompassMeta()));
            Player player = (Player) playerState.getPlayerObject();
            for (int x = 0; x < compassMetas.size(); x++) {
                ItemStack compass = new ItemStack(Material.COMPASS);
                compass.setItemMeta(compassMetas.get(x));
                player.getInventory().setItem(x, compass);
            }
        }
    }

}
