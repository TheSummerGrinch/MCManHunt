package io.github.thesummergrinch.mcmanhunt.commands.config;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetMaxRunnersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args == null || ManHuntUtilities.hasLetters(args[0])) return false;
        ManHuntUtilities.setMaxRunners(Integer.parseInt(args[0]));
        return true;
    }

}
