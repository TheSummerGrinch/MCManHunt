package io.github.thesummergrinch.mcmanhunt.commands.plugin.info;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ManHuntVersionCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            sender.sendMessage(MCManHuntStringCache.getInstance().getStringFromCache("version-message-part-one")
                    + MCManHunt.getPlugin(MCManHunt.class).getDescription().getVersion()
                    + MCManHuntStringCache.getInstance().getStringFromCache("version-message-part-two"));
        }
        return true;
    }
}
