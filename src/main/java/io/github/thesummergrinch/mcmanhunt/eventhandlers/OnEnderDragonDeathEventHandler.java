package io.github.thesummergrinch.mcmanhunt.eventhandlers;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.game.GameController;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnEnderDragonDeathEventHandler implements Listener {

    /**
     * Checks whether an EntityDeathsEvent corresponds to the death of the Enderdragon, which would fulfill the win-
     * condition of the Runner-team.
     *
     * @param event - The EntityDeathEvent passed by the Server.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDragonDeathEvent(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();
        if (GameController.getInstance().getGameState().equals(GameController.GameState.RUNNING)
                && entity instanceof EnderDragon) {
            MCManHunt.getPlugin(MCManHunt.class).getServer().broadcastMessage("The Runners have won the Game!");
            GameController.getInstance().stopGame();
        }
    }

}
