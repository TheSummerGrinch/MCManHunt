package io.github.thesummergrinch.mcmanhunt.commands.player.team;

import io.github.thesummergrinch.mcmanhunt.game.GameController;
import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinRandomTeamCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && GameController.getInstance().getGameState().equals(GameController.GameState.DEFAULT)) {
            UserCache.getInstance().getPlayerState(((Player) sender).getUniqueId()).setPlayerRole(PlayerRole.RANDOM);
            sender.sendMessage("You will be assigned to a random team when the game begins!");
            return true;
        }
        return false;
    }
}
