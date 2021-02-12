package io.github.thesummergrinch.mcmanhunt.commands.plugin.info;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

public class ManHuntVersionCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender.isOp()) {

            sender.sendMessage(MessageFormat.format(LanguageFileLoader.getInstance().getString("version-message"),
                    MCManHunt.getPlugin(MCManHunt.class).getDescription().getVersion()));

        }

        return true;
    }
}
