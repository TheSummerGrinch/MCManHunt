package io.github.thesummergrinch.mcmanhunt.commands.player.info;

import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListHuntersCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (UserCache.getInstance().getNumberOfHunters() != 0) {
            StringBuilder stringBuilder = new StringBuilder("The Hunter-Team consists of: ");
            UserCache.getInstance().getHunters().forEach(playerState ->
                    stringBuilder.append(playerState.getPlayerObject().getName()).append(", "));
            sender.sendMessage(stringBuilder.substring(0, stringBuilder.length() - 2));
        } else sender.sendMessage("The Hunter-Team has no members!");
        return true;
    }
}
