package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class OnPlayerJoinEventHandler implements Listener {

    private final MCManHunt manhuntPlugin;

    public OnPlayerJoinEventHandler(final MCManHunt manhuntPlugin) {
        this.manhuntPlugin = manhuntPlugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoinEvent(final PlayerJoinEvent event) {

        if (event.getPlayer().isOp()
                && manhuntPlugin.isUpdateAvailable()
                && this.manhuntPlugin.getFileConfiguration().getBoolean("update-alert")) {
            event.getPlayer().sendMessage(LanguageFileLoader.getInstance().getString("update-available")
                    + manhuntPlugin.getVersionString());
        }

        final UUID playerUUID = event.getPlayer().getUniqueId();
        PlayerState playerState = null;

        if (PlayerStateCache.getInstance().getPlayerState(playerUUID) == null) {

            playerState = new PlayerState(playerUUID);

        } else {

            if (PlayerStateCache.getInstance().getPlayerState(playerUUID).isInGame())
                return;

        }

        if (!event.getPlayer().getInventory().contains(Material.COMPASS))
            return;

        final Player player = event.getPlayer();
        final ItemStack[] inventoryContents =
                player.getInventory().getContents();

        for (ItemStack itemStack : inventoryContents) {

            if (itemStack != null) {

                if (itemStack.getType() == Material.COMPASS
                        && itemStack.hasItemMeta()) {

                    ItemMeta itemMeta = itemStack.getItemMeta();

                    if (itemMeta.hasLore()) {

                        List<String> lore = itemMeta.getLore();

                        for (String loreString : lore) {

                            loreString.contains(LanguageFileLoader
                                    .getInstance()
                                    .getString("manhunt-compass"));

                            player.getInventory().clear();

                            return;

                        }
                    }
                }
            }
        }
    }
}
