package io.github.thesummergrinch.mcmanhunt.commands;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinRunnersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player && ((Player) sender).isOnline() && !ManHuntUtilities.GAME_IN_PROGRESS.get()) {
            final Player player = ((Player) sender).getPlayer();
            if (ManHuntUtilities.isHunter(player)) {
                player.sendMessage("You are already a member of the Hunter-team! To leave the Hunter-team and join the Hunter-team, use /leavehunters and /joinrunners.");
            } else if (ManHuntUtilities.isRunner(player)) {
                player.sendMessage("You are already a member of the Runner-team.");
            } else if (ManHuntUtilities.addRunner(player)) {
                ManHuntUtilities.SERVER.broadcastMessage(player.getName() + " has joined the Runner-team!");
            } else {
                player.sendMessage("Something went wrong. The server could not add you to the Runner-team. Please contact the plugin-author.");
            }
            return true;
        }
        return false;
    }

}
