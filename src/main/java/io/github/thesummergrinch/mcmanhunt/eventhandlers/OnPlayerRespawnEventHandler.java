package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.Game;
import io.github.thesummergrinch.mcmanhunt.game.GameState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

public class OnPlayerRespawnEventHandler implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerRespawnEvent(@NotNull final PlayerRespawnEvent event) {
        final PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(event.getPlayer().getUniqueId());
        final Game game = GameCache.getInstance().getGameFromCache(playerState.getGameName());
        if (playerState.isInGame())
            if (game.getGameState().equals(GameState.RUNNING))
                if (playerState.getPlayerRole().equals(PlayerRole.HUNTER)) {
                    game.giveHunterCompasses(playerState.getPlayerUUID());
                }
        if (Bukkit.getPlayer(playerState.getPlayerUUID()).getBedSpawnLocation() == null) {
            event.setRespawnLocation(game.getWorldSpawn());
        }
    }

}
