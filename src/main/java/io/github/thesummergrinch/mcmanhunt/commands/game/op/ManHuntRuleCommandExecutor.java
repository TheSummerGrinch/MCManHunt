package io.github.thesummergrinch.mcmanhunt.commands.game.op;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ManHuntRuleCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.isOp() && args.length >= 2) {
            final Game game = GameCache.getInstance().getGameFromCache(args[0]);
            if (game != null) {
                game.setManHuntRule(args[1], args[2]);
            } else {
                sender.sendMessage(ChatColor.RED + MCManHuntStringCache.getInstance().getStringFromCache("rule-change-failed"));
            }
            return true;
        }
        return false;
    }
}