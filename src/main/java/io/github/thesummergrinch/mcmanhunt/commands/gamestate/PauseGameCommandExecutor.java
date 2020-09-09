package io.github.thesummergrinch.mcmanhunt.commands.gamestate;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PauseGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            if (GameFlowUtilities.isGameInProgress()) {
                if (!GameFlowUtilities.isGamePaused()) {
                    GameFlowUtilities.pauseGame((Player) sender);
                } else {
                    ManHuntUtilities.broadcastMessage("The game is already paused. To resume the game, use /resumegame.");
                }
            } else {
                ManHuntUtilities.broadcastMessage("There is no ongoing game. To start a game, use /startgame.");
            }
            return true;
        }
        return false;
    }

}
