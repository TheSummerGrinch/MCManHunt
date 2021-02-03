package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class OnPlayerJoinEventHandler implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoinEvent(final PlayerJoinEvent event) {
        final UUID playerUUID = event.getPlayer().getUniqueId();
        if (PlayerStateCache.getInstance().getPlayerState(playerUUID) == null) {
            new PlayerState(playerUUID);
            if (!event.getPlayer().getInventory().contains(Material.COMPASS)) return;
            final Player player = event.getPlayer();
            for (ItemStack itemStack : player.getInventory()) {
                if (itemStack.getType().equals(Material.COMPASS) && itemStack.getItemMeta().hasLore()) {
                    List<String> lore = itemStack.getItemMeta().getLore();
                    for (String loreString : lore) {
                        if (loreString.equals(LanguageFileLoader.getInstance().getString("manhunt-compass"))) {
                            player.getInventory().clear();
                            return;
                        }
                    }
                }
            }
        }
    }

}
