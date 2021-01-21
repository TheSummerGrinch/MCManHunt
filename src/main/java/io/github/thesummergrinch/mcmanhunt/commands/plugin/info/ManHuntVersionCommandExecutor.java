package io.github.thesummergrinch.mcmanhunt.commands.plugin.info;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ManHuntVersionCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            sender.sendMessage("[MCManHunt] You are running version "
                    + MCManHunt.getPlugin(MCManHunt.class).getDescription().getVersion() + " of the MCManHunt-plugin.");
        }
        return true;
    }
}
