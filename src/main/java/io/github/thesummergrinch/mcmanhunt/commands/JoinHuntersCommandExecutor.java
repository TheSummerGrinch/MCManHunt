package io.github.thesummergrinch.mcmanhunt.commands;

import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinHuntersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player && ((Player) sender).isOnline() && !ManHuntUtilities.GAME_IN_PROGRESS.get()) {
            final Player player = ((Player) sender).getPlayer();
            if (ManHuntUtilities.isHunter(player)) {
                player.sendMessage("You are already a member of the Hunter-team!");
            } else if (ManHuntUtilities.isRunner(player)) {
                player.sendMessage("You are a member of the Runner-team. To leave the Runner-team and join the Hunter-team, use /leaverunners and /joinhunters.");
            } else if (ManHuntUtilities.addHunter(player)) {
                ManHuntUtilities.SERVER.broadcastMessage(player.getName() + " has joined the Hunter-team!");
            } else {
                player.sendMessage("Something went wrong. The server could not add you to the Hunter-team. Please contact the plugin-author.");
            }
            return true;
        }
        return false;
    }

}
