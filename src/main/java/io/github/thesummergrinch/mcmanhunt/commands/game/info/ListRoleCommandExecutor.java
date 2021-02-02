package io.github.thesummergrinch.mcmanhunt.commands.game.info;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.PlayerStateCache;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerRole;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
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

        if ((args.length >= 1 && args[0].equalsIgnoreCase("runners"))
                || label.equalsIgnoreCase("listrunners")
                || args.length >= 1 && args[0].equalsIgnoreCase(LanguageFileLoader.getInstance().getString("runners"))) {
            roleToList = PlayerRole.RUNNER;
        } else if ((args.length >= 1 && args[0].equalsIgnoreCase("hunters"))
                || label.equalsIgnoreCase("listhunters")
                || args.length >= 1 && args[0].equalsIgnoreCase(LanguageFileLoader.getInstance().getString("hunters"))) {
            roleToList = PlayerRole.HUNTER;
        } else if (label.equalsIgnoreCase("listteams")) {
            roleToList = PlayerRole.DEFAULT;
        } else {
            return false;
        }

        final Game game;
        boolean labelsUsed = (label.equalsIgnoreCase("listrunners")
                || label.equalsIgnoreCase("listhunters")
                || label.equalsIgnoreCase("listteams"));
        if (sender instanceof Player) {
            final PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(((Player) sender).getUniqueId());

            if ((args.length >= 2 && GameCache.getInstance().getGameFromCache(args[1]) != null)) {
                game = GameCache.getInstance().getGameFromCache(args[1]);
            } else if (playerState.isInGame()) {
                game = GameCache.getInstance().getGameFromCache(playerState.getGameName());
            } else if (labelsUsed && args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {
                game = GameCache.getInstance().getGameFromCache(args[0]);
            } else {
                return false;
            }

        } else {
            if ((args.length >= 2 && GameCache.getInstance().getGameFromCache(args[1]) != null)) {
                game = GameCache.getInstance().getGameFromCache(args[1]);
            } else if (labelsUsed && args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {
                game = GameCache.getInstance().getGameFromCache(args[0]);
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
            hunters = (HashSet<PlayerState>) game.getHunters();
            if (hunters.isEmpty()) {
                stringBuilder.append(LanguageFileLoader.getInstance().getString("hunter-team-no-members"));
            } else {
                stringBuilder.append(LanguageFileLoader.getInstance().getString("list-hunters"));
                hunters.forEach(hunter -> stringBuilder.append(hunter.getPlayerName()).append(", "));
                stringBuilder.substring(0, stringBuilder.length() - 3);
            }
            if (roleToList.equals(PlayerRole.DEFAULT)) {
                stringBuilder.append("\n\n");
                runners = (HashSet<PlayerState>) game.getRunners();
                if (runners.isEmpty()) {
                    stringBuilder.append(LanguageFileLoader.getInstance().getString("runner-team-no-members"));
                } else {
                    stringBuilder.append(LanguageFileLoader.getInstance().getString("list-runners"));
                    runners.forEach(hunter -> stringBuilder.append(hunter.getPlayerName()).append(", "));
                    stringBuilder.substring(0, stringBuilder.length() - 3);
                }
            }
        } else if (roleToList.equals(PlayerRole.RUNNER)) {
            runners = (HashSet<PlayerState>) game.getRunners();
            if (runners.isEmpty()) {
                stringBuilder.append(LanguageFileLoader.getInstance().getString("runner-team-no-members"));
            } else {
                stringBuilder.append(LanguageFileLoader.getInstance().getString("list-runners"));
                runners.forEach(hunter -> stringBuilder.append(hunter.getPlayerName()).append(", "));
                stringBuilder.substring(0, stringBuilder.length() - 3);
            }
        }
        sender.sendMessage(stringBuilder.toString());
    }

}
