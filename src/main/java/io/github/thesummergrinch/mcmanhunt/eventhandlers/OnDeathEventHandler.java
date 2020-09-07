package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnDeathEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(final PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (ManHuntUtilities.GAME_IN_PROGRESS.get() && ManHuntUtilities.isRunner(player)) {
            ManHuntUtilities.removeRunner(player.getName());
            player.setGameMode(GameMode.SPECTATOR);
            if (ManHuntUtilities.isRunnerMapEmpty()) {
                ManHuntUtilities.SERVER.broadcastMessage("The Hunters win!");
                ManHuntUtilities.stopGame();
            }
        }
    }

}
