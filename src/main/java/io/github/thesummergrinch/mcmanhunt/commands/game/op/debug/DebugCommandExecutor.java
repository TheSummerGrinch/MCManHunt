package io.github.thesummergrinch.mcmanhunt.commands.game.op.debug;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.events.ManHuntWinEvent;
import io.github.thesummergrinch.mcmanhunt.io.settings.DefaultSettingsContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DebugCommandExecutor implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (DefaultSettingsContainer.getInstance().getInteger("debug-level") <= 0) {
            sender.sendMessage("The debug-level is set to 0. To enable " +
                    "debugging features, raise the debug-level to a valid " +
                    "number in the config.yml.");
            return true;
        }
        if (sender.isOp() && sender instanceof Player && args != null && args.length >= 1) {
            if (args[0].equalsIgnoreCase("winevent") && args.length >= 2) {
                if (GameCache.getInstance().containsKey(args[1])) {
                    Bukkit.getPluginManager().callEvent(new ManHuntWinEvent(args[1],
                            GameCache.getInstance().getGameFromCache(args[1]).getHunterUUIDs()));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> options = new ArrayList<>();
        if (args.length <= 1) {
            options.add("winevent");
            return options;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("winevent")) {
                return GameCache.getInstance().getGameNamesAsList();
            }
        }
        return options;
    }
}
