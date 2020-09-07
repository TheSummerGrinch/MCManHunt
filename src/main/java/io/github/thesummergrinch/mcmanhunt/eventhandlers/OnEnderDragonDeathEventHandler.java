package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnEnderDragonDeathEventHandler implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDragonDeathEvent(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();
        if (ManHuntUtilities.GAME_IN_PROGRESS.get() && event.getEntity() instanceof EnderDragon) {
            ManHuntUtilities.SERVER.broadcastMessage("The Runners have won the Game!");
            ManHuntUtilities.stopGame();
        }
    }

}
