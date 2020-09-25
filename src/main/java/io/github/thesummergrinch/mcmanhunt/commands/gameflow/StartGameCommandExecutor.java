package io.github.thesummergrinch.mcmanhunt.commands.gameflow;

import io.github.thesummergrinch.mcmanhunt.game.GameController;
import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartGameCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (GameController.getInstance().getGameState().equals(GameController.GameState.DEFAULT)) {
                if (GameController.getInstance().getManHuntGameMode().equals(GameController.GameMode.RANDOM)
                        && UserCache.getInstance().getPlayersToRandomlyAllocate().size() >= 2) {
                    GameController.getInstance().startGame();
                    return true;
                }
                if (UserCache.getInstance().getNumberOfHunters() == 0L
                        || UserCache.getInstance().getNumberOfRunners() == 0L) {
                    sender.sendMessage("Assign players to both teams before starting!");
                    return true;
                }
            }
            GameController.getInstance().startGame();
            return true;
        }
        return false;
    }
}
