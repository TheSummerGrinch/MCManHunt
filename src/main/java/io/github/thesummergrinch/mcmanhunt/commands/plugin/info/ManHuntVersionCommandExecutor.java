package io.github.thesummergrinch.mcmanhunt.commands.plugin.info;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ManHuntVersionCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            sender.sendMessage(LanguageFileLoader.getInstance().getString("version-message-part-one")
                    + MCManHunt.getPlugin(MCManHunt.class).getDescription().getVersion()
                    + LanguageFileLoader.getInstance().getString("version-message-part-two"));
        }
        return true;
    }
}
