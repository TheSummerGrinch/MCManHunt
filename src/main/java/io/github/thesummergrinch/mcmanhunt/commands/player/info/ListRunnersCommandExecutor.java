package io.github.thesummergrinch.mcmanhunt.commands.player.info;

import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListRunnersCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (UserCache.getInstance().getNumberOfRunners() != 0) {
            StringBuilder stringBuilder = new StringBuilder("The Runner-Team consists of: ");
            UserCache.getInstance().getRunnerPlayerStates().forEach(playerState ->
                    stringBuilder.append(playerState.getPlayerObject().getName()).append(", "));
            sender.sendMessage(stringBuilder.substring(0, stringBuilder.length() - 2));
        } else sender.sendMessage("The Runner-Team has no members!");
        return true;
    }
}
