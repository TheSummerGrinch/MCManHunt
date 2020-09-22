package io.github.thesummergrinch.mcmanhunt.commands.player.team;

import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveTeamCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            UserCache.getInstance().getPlayerState(((Player) sender).getUniqueId()).setPlayerRole(PlayerRole.DEFAULT);
            sender.sendMessage("You have left your team!");
            return true;
        }
        return false;
    }
}
