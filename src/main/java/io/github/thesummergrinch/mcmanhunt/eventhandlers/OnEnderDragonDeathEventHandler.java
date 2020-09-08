package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
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
        if (GameFlowUtilities.isGameInProgress() && event.getEntity() instanceof EnderDragon) {
            ManHuntUtilities.broadcastMessage("The Runners have won the Game!");
            GameFlowUtilities.stopGame();
        }
    }

}
