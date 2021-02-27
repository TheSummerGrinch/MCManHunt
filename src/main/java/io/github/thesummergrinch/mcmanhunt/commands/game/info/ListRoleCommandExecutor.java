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
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ListRoleCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        final PlayerRole roleToList = determineRequestedPlayerRole(label, args);

        if (roleToList == null) return false;

        boolean labelsUsed = labelUsed(label);
        final Game game = getGame(sender, args, labelsUsed);

        if (game == null) return false;

        listRole(sender, game, roleToList);

        return true;
    }

    private void listRole(final CommandSender sender, final Game game, final PlayerRole roleToList) {

        final StringBuilder stringBuilder = new StringBuilder();

        if (roleToList.equals(PlayerRole.HUNTER) || roleToList.equals(PlayerRole.DEFAULT)) {

            stringBuilder.append(getPlayerRoleList(game.getHunters(), PlayerRole.HUNTER));

            if (roleToList.equals(PlayerRole.DEFAULT)) {

                stringBuilder.append("\n\n");
                stringBuilder.append(getPlayerRoleList(game.getRunners(), PlayerRole.RUNNER));

            }

        } else if (roleToList.equals(PlayerRole.RUNNER)) {

            stringBuilder.append(getPlayerRoleList(game.getRunners(),
                    PlayerRole.RUNNER));

        }

        sender.sendMessage(stringBuilder.toString());
    }

    @Nullable
    private PlayerRole determineRequestedPlayerRole(final String label, final String[] args) {

        if ((args.length >= 1 && args[0].equalsIgnoreCase("runners"))
                || label.equalsIgnoreCase("listrunners")
                || args.length >= 1 && args[0].equalsIgnoreCase(LanguageFileLoader.getInstance().getString("runners"))) {

            return PlayerRole.RUNNER;

        } else if ((args.length >= 1 && args[0].equalsIgnoreCase("hunters"))
                || label.equalsIgnoreCase("listhunters")
                || args.length >= 1 && args[0].equalsIgnoreCase(LanguageFileLoader.getInstance().getString("hunters"))) {

            return PlayerRole.HUNTER;

        } else if (label.equalsIgnoreCase("listteams")) {

            return PlayerRole.DEFAULT;

        } else {

            return null;

        }
    }

    private boolean labelUsed(final String label) {

        return label.equalsIgnoreCase("listrunners")
                || label.equalsIgnoreCase("listhunters")
                || label.equalsIgnoreCase("listteams");

    }

    @Nullable
    private Game getGame(final CommandSender sender, final String[] args, final boolean labelsUsed) {

        if (sender instanceof Player) {

            final PlayerState playerState = PlayerStateCache.getInstance().getPlayerState(((Player) sender).getUniqueId());

            if ((args.length >= 2 && GameCache.getInstance().getGameFromCache(args[1]) != null)) {

                return GameCache.getInstance().getGameFromCache(args[1]);

            } else if (playerState.isInGame()) {

                return GameCache.getInstance().getGameFromCache(playerState.getGameName());

            } else if (labelsUsed && args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {

                return GameCache.getInstance().getGameFromCache(args[0]);

            }

        } else {

            if ((args.length >= 2 && GameCache.getInstance().getGameFromCache(args[1]) != null)) {

                return GameCache.getInstance().getGameFromCache(args[1]);

            } else if (labelsUsed && args.length >= 1 && GameCache.getInstance().getGameFromCache(args[0]) != null) {

                return GameCache.getInstance().getGameFromCache(args[0]);

            }
        }

        return null;
    }

    private String getPlayerRoleList(final Set<PlayerState> playerStates,
                                     final PlayerRole roleToList) {

        final StringBuilder playerRoleList = new StringBuilder();
        String message;

        if (playerStates.isEmpty()) {

            playerRoleList.append((roleToList.equals(PlayerRole.RUNNER)) ?
                    LanguageFileLoader.getInstance().getString(
                    "runner-team-no-members") : LanguageFileLoader
                    .getInstance().getString("hunter-team-no-members"));

            message = playerRoleList.toString().trim();

        } else {

            playerRoleList.append((roleToList.equals(PlayerRole.RUNNER)) ?
                    LanguageFileLoader.getInstance().getString("list" +
                    "-runners") : LanguageFileLoader.getInstance().getString(
                            "list-hunters"));

            playerStates.forEach(playerState -> playerRoleList
                    .append(playerState.getPlayerName()).append(", "));

            message = playerRoleList.toString().trim();
            message = message.substring(0, message.length() - 1);

        }

        return message;
    }

}
