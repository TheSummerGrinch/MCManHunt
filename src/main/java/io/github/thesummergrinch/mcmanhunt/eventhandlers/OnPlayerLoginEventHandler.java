package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class OnPlayerLoginEventHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLoginEvent(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        if (ManHuntUtilities.GAME_IN_PROGRESS.get()) {
            if (ManHuntUtilities.isRunner(player)) {
                if (ManHuntUtilities.GAME_PAUSED.get()) {
                    ManHuntUtilities.restrictRunnerMovement();
                } else ManHuntUtilities.allowRunnerMovement();
            } else if (ManHuntUtilities.isHunter(player)) {
                if (ManHuntUtilities.GAME_PAUSED.get()) {
                    ManHuntUtilities.restrictHunterMovement();
                } else ManHuntUtilities.allowHunterMovement();
            }
        }
    }

}
