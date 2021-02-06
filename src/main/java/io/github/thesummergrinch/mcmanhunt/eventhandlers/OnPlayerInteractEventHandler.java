package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.CompassStateCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.game.players.compasses.CompassState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
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

        final PlayerState playerState = PlayerStateCache.getInstance()
        .getPlayerState(event.getPlayer().getUniqueId());

        if (!playerState.isInGame()
                || playerState.getPlayerRole().equals(PlayerRole.DEFAULT))
            return;

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR)
                || event.getAction().equals(Action.LEFT_CLICK_AIR)) {

            final Player sender = event.getPlayer();
            ItemStack compass;

            if (sender.getInventory().getItemInMainHand().getType()
                .equals(Material.COMPASS)) {

                compass = sender.getInventory().getItemInMainHand();

                if (compass.getItemMeta().hasLore() && compass.getItemMeta()
                .getLore().get(0).equals(LanguageFileLoader.getInstance()
                .getString("manhunt-compass"))) {

                    updateCompassMeta(sender,
                            compass.getItemMeta().getLore().get(1),
                            (CompassMeta) compass.getItemMeta(), Hand.MAIN_HAND);

                }
            }

            if (sender.getInventory().getItemInOffHand().getType()
                    .equals(Material.COMPASS)) {

                compass = sender.getInventory().getItemInOffHand();

                if (compass.getItemMeta().hasLore() && compass.getItemMeta()
                        .getLore().get(0).equals(LanguageFileLoader.getInstance()
                                .getString("manhunt-compass"))) {

                    updateCompassMeta(sender,
                            compass.getItemMeta().getLore().get(1),
                            (CompassMeta) compass.getItemMeta(), Hand.OFF_HAND);

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

    private void updateCompassMeta(@NotNull final Player sender,
                                   @NotNull final String targetName,
                                   @NotNull final CompassMeta playerCompassMeta,
                                   @NotNull final Hand handWithItem) {

        final Player target = Bukkit.getPlayer(targetName);

        if (target ==  null) return;

        final PlayerState playerState =
                PlayerStateCache.getInstance().getPlayerState(target.getUniqueId());

        if (playerState.isInGame() && !playerState.getPlayerRole().equals(PlayerRole.DEFAULT)) {

            CompassState compassState = getUpdatedCompassMeta(target.getUniqueId(), playerCompassMeta);
            CompassMeta compassMeta = compassState.getCompassMeta();

            if (handWithItem.equals(Hand.MAIN_HAND)) {

                sender.getInventory().getItemInMainHand().setItemMeta(compassMeta);

            } else {

                sender.getInventory().getItemInOffHand().setItemMeta(compassMeta);

            }

        } else {

            if (handWithItem.equals(Hand.MAIN_HAND)) {

                sender.getInventory().setItemInMainHand(null);

            } else {

                sender.getInventory().setItemInOffHand(null);

            }
        }
    }

    private enum Hand {
        MAIN_HAND,
        OFF_HAND
    }

}
