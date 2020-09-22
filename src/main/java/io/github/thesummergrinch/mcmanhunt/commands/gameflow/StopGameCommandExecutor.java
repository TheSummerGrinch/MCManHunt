package io.github.thesummergrinch.mcmanhunt.commands.gameflow;

import io.github.thesummergrinch.mcmanhunt.game.GameController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopGameCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() && !GameController.getInstance().getGameState().equals(GameController.GameState.DEFAULT)) {
            GameController.getInstance().stopGame();
            return true;
        }
        return false;
    }

}
