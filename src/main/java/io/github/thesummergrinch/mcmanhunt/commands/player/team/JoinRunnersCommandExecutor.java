package io.github.thesummergrinch.mcmanhunt.commands.player.team;

import io.github.thesummergrinch.mcmanhunt.game.GameController;
import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinRunnersCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && GameController.getInstance().getGameState().equals(GameController.GameState.DEFAULT)) {
            if (UserCache.getInstance().getNumberOfRunners() >= GameController.getInstance().getMaxRunners()) {
                sender.sendMessage("the Runner-team is full. You can join the Hunter-team, or ask an OP to increase " +
                        "the Runner-cap.");
                return true;
            }
            UserCache.getInstance().getPlayerState(((Player) sender).getUniqueId()).setPlayerRole(PlayerRole.RUNNER);
            sender.sendMessage("You have joined the Runner-Team!");
            return true;
        }
        return false;
    }
}
