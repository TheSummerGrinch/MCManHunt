package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class OnPlayerPortalEventHandler implements Listener {

    //TODO rewrite to make it more readable
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPortalEvent(@NotNull final PlayerPortalEvent event) {

        if (Bukkit.getPluginManager().getPlugin("Multiverse-Core") != null
                && Bukkit.getPluginManager()
                .getPlugin("Multiverse-NetherPortals") != null) {
            return;
        }

        final String worldName = event.getFrom().getWorld().getName();
        final UUID playerUUID = event.getPlayer().getUniqueId();
        final PlayerState playerState =
                PlayerStateCache.getInstance().getPlayerState(playerUUID);

        if (!playerState.isInGame()) return;

        if (worldName.equals("world") || worldName.equals("world_nether")
                || worldName.equals("world_the_end")) return;

        if (event.getCause()
                .equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {

            event.setSearchRadius(128);

            if (event.getFrom().getWorld().getEnvironment()
                    .equals(World.Environment.NORMAL)) {

                Location to = event.getTo();
                World worldTo = Bukkit.getWorld(worldName + "_nether");

                to.setWorld(worldTo);
                to.setZ(event.getFrom().getZ() / 8);
                to.setY(event.getFrom().getY() / 8);
                to.setX(event.getFrom().getX() / 8);
                event.setTo(to);

            } else {

                Location to = event.getTo();
                World worldTo = Bukkit.getWorld(worldName.split("_")[0]);

                to.setWorld(worldTo);
                to.setZ(event.getFrom().getZ() * 8);
                to.setY(event.getFrom().getY() * 8);
                to.setX(event.getFrom().getX() * 8);
                event.setTo(to);

            }

        } else if (event.getCause()
                .equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {

            if (event.getFrom().getWorld().getEnvironment()
                    .equals(World.Environment.THE_END)) {

                Location bedLocation = event.getPlayer().getBedSpawnLocation();

                if (bedLocation != null) {

                    event.setTo(bedLocation);

                } else {

                    World toWorld = Bukkit.getWorld(worldName.split("_")[0]);

                    event.setTo(toWorld.getSpawnLocation());

                }

            } else {

                World toWorld = Bukkit.getWorld(worldName + "_the_end");
                Location to = event.getTo();

                to.setWorld(toWorld);
                event.setTo(to);

            }

            event.setSearchRadius(128);

        }
    }
}
