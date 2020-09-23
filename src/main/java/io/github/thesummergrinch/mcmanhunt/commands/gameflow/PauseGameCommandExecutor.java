package io.github.thesummergrinch.mcmanhunt.commands.gameflow;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.game.GameController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PauseGameCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() && GameController.getInstance().getGameState().equals(GameController.GameState.RUNNING)) {
            GameController.getInstance().pauseGame();
            MCManHunt.getPlugin(MCManHunt.class).getServer().broadcastMessage("The Game has been paused. Difficulty was " +
                    "automatically set to peaceful.");
            return true;
        }
        return false;
    }
}
