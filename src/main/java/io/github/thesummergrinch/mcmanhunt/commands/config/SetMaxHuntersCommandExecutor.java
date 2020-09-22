package io.github.thesummergrinch.mcmanhunt.commands.config;

import io.github.thesummergrinch.mcmanhunt.game.GameController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetMaxHuntersCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() && args.length != 0 && args[0].matches("[0-9]+")) {
            GameController.getInstance().setMaxHunters(Integer.parseInt(args[0]));
            return true;
        }
        return false;
    }
}
