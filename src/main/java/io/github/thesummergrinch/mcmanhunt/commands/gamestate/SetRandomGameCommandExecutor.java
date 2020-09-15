package io.github.thesummergrinch.mcmanhunt.commands.gamestate;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRandomGameCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length >= 1) {
            if (args[0].equalsIgnoreCase("true")) {
                GameFlowUtilities.setTeamsAreRandomized(true);
                ManHuntUtilities.getRunners().forEach(player -> {
                    if (player.isOnline()) player.sendMessage("[MCManHunt] The gamemode has been set to Random. " +
                            "You will be removed from the Hunter-team.");
                });
                ManHuntUtilities.getHunters().forEach(player -> {
                    if (player.isOnline()) player.sendMessage("[MCManHunt] The gamemode has been set to Random. " +
                            "You will be removed from the Hunter-team.");
                });
                ManHuntUtilities.resetplayerroles();
            } else if (args[0].equalsIgnoreCase("false")) GameFlowUtilities.setTeamsAreRandomized(false);
            return true;
        }
        return false;
    }
}
