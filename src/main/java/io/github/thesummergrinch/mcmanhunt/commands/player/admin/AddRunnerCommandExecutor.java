package io.github.thesummergrinch.mcmanhunt.commands.player.admin;

import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddRunnerCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() && args.length > 0) {
            PlayerState playerState = UserCache.getInstance().getPlayerState(UserCache.getInstance()
                    .getUniqueIDByName(args[0]));
            if (playerState == null) {
                sender.sendMessage("Could not find the specified player. Please check the spelling.");
                return true;
            } else if (playerState.getPlayerRole().equals(PlayerRole.RUNNER)) {
                sender.sendMessage("Player is already in the Runner-team!");
                return true;
            }
            playerState.setPlayerRole(PlayerRole.RUNNER);
            sender.sendMessage("Player was successfully added to the Runner-Team.");
            return true;
        }
        return false;
    }
}
