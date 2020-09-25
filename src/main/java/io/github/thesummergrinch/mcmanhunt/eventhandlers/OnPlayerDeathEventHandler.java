package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.game.GameController;
import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerState;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeathEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeathEvent(final PlayerDeathEvent event) {
        final PlayerState playerState = UserCache.getInstance().getPlayerState(event.getEntity().getUniqueId());
        if(GameController.getInstance().getGameState().equals(GameController.GameState.RUNNING)) {
            if (playerState.getPlayerRole().equals(PlayerRole.RUNNER)) {
                playerState.setPlayerRole(PlayerRole.SPECTATOR);
                event.getEntity().setGameMode(GameMode.SPECTATOR);
            }
            if (UserCache.getInstance().getNumberOfRunners() == 0) {
                MCManHunt.getPlugin(MCManHunt.class).getServer().broadcastMessage("The Hunters have won the game!");
                GameController.getInstance().stopGame();
            }
        }
    }

}
