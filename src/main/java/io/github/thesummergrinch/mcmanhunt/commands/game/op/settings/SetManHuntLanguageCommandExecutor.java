package io.github.thesummergrinch.mcmanhunt.commands.game.op.settings;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import io.github.thesummergrinch.mcmanhunt.io.settings.DefaultSettingsContainer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//TODO implement properly
public class SetManHuntLanguageCommandExecutor implements CommandExecutor, TabCompleter {

    private static final List<String> AVAILABLE_LANGUAGES = new ArrayList<String>() {
        {
            add("enGB");
            add("enUS");
            add("nlNL");
        }
    };

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp()) {
            if (args.length < 1) return false;
            try {
                LanguageFileLoader.getInstance().loadNewLanguage(new File(
                        MCManHunt.getPlugin(MCManHunt.class).getDataFolder().getPath() + File.separator + "lang"
                ), new Locale(args[0].substring(0,2), args[0].substring(2,4)));
                sender.sendMessage(LanguageFileLoader.getInstance().getString("language-loaded"));
                return true;
            } catch (IOException exception) {
                sender.sendMessage(LanguageFileLoader.getInstance().getString("load-language-failed"));
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return SetManHuntLanguageCommandExecutor.AVAILABLE_LANGUAGES;
    }
}
