package io.github.thesummergrinch.mcmanhunt.commands.game.info;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class ListRoleCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        final PlayerRole roleToList;

        if ((args.length >= 1 && args[0].equals("runners")) || label.equals("listrunners")) {
            roleToList = PlayerRole.RUNNER;
        } else if ((args.length >= 1 && args[0].equals("hunters")) || label.equals("listhunters")) {
            roleToList = PlayerRole.HUNTER;
        } else if (label.equals("listteams")) {
            roleToList = PlayerRole.DEFAULT;
        } else {
            return false;
        }

        final Game game;
        if (sender instanceof Player) {
            final PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(((Player) sender).getUniqueId());

            if ((args.length >= 2 && GameCache.getInstance().getGameFromCache(args[1]) != null)) {
                game = GameCache.getInstance().getGameFromCache(args[1]);
            } else if (playerState.isInGame()) {
                game = GameCache.getInstance().getGameFromCache(playerState.getGameName());
            } else if (roleToList.equals(PlayerRole.DEFAULT) && args.length >= 1
                    && GameCache.getInstance().getGameFromCache(args[0]) != null) {
                game = GameCache.getInstance().getGameFromCache(args[0]);
            }else {
                return false;
            }

        } else {
            if ((args.length >= 2 && GameCache.getInstance().getGameFromCache(args[1]) != null)) {
                game = GameCache.getInstance().getGameFromCache(args[1]);
            } else {
                return false;
            }
        }
        listRole(sender, game, roleToList);
        return true;
    }

    private void listRole(final CommandSender sender, final Game game, final PlayerRole roleToList) {
        final StringBuilder stringBuilder = new StringBuilder();
        final HashSet<PlayerState> hunters;
        final HashSet<PlayerState> runners;
        if (roleToList.equals(PlayerRole.HUNTER) || roleToList.equals(PlayerRole.DEFAULT)) {
            stringBuilder.append(MCManHuntStringCache.getInstance().getStringFromCache("list-hunters"));
            hunters = (HashSet<PlayerState>) game.getHunters();
            hunters.forEach(hunter -> stringBuilder.append(hunter.getPlayerName()).append(", "));
            stringBuilder.substring(0, stringBuilder.length() - 2);
            if (roleToList.equals(PlayerRole.DEFAULT)) {
                stringBuilder.append("\n\n");
                stringBuilder.append(MCManHuntStringCache.getInstance().getStringFromCache("list-runners"));
                runners = (HashSet<PlayerState>) game.getRunners();
                runners.forEach(hunter -> stringBuilder.append(hunter.getPlayerName()).append(", "));
                stringBuilder.substring(0, stringBuilder.length() - 2);
            }
        } else if (roleToList.equals(PlayerRole.RUNNER)) {
            stringBuilder.append(MCManHuntStringCache.getInstance().getStringFromCache("list-runners"));
            runners = (HashSet<PlayerState>) game.getRunners();
            runners.forEach(hunter -> stringBuilder.append(hunter.getPlayerName()).append(", "));
            stringBuilder.substring(0, stringBuilder.length() - 2);
        }
        sender.sendMessage(stringBuilder.toString());
    }

}
