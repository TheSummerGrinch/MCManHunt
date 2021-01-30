package io.github.thesummergrinch.mcmanhunt.commands.game.player;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class JoinGameCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player && args.length >= 1) {
            Game game = GameCache.getInstance().getGameFromCache(args[0]);
            if (game == null) return false;
            UUID playerUUID = ((Player) sender).getUniqueId();
            game.addPlayerToGame(playerUUID);
            sender.sendMessage(LanguageFileLoader.getInstance().getString("joined-game"));
            return true;
        }
        return false;
    }
}
