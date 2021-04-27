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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Determine which role to list
        final PlayerRole roleToList = determineRequestedPlayerRole(label, args);

        // If the PlayerRole somehow equals null, return false.
        if (roleToList == null) return false;

        //Determine whether the command was issued using a label.
        boolean labelsUsed = labelUsed(label);
        // Determine which game's team-configuration(s) should be listed.
        final Game game = getGame(sender, args, labelsUsed);

        // If no game was specified, and the user was not in a game when
        // issuing the command, we return false.
        if (game == null) return false;

        // List the role(s) to the player.
        listRole(sender, game, roleToList);

        return true;
    }

    /**
     * Compiles a the requested team-configuration for the
     * {@link CommandSender} in a String, and sends it to the CommandSender.
     *
     * If the {@link PlayerRole} has been set to {@link PlayerRole#DEFAULT},
     * we compile a the team-configurations for both the Hunters, as well as
     * the Runners.
     *
     * @param sender the {@link CommandSender} responsible for calling the
     *               command
     * @param game the {@link Game} that should be modified
     * @param roleToList the {@link PlayerRole} that should be listed.
     */
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

    /**
     * Determines which team-configuration should be compiled for the
     * {@link CommandSender}.
     *
     * This is determined by using the arguments given as the CommandSender
     * sent the command, or by using the label.
     *
     * E.g.
     * "/listrunners" and "/listrole runners" are the same thing.
     * "/listhunters" and "/listrole hunters" are the same thing.
     * "/listteams" will return {@link PlayerRole#DEFAULT} to facilitate
     * compiling a list of both team-configurations.
     *
     * @param label the label used to send the command.
     * @param args the arguments given when the CommandSender issued the
     *             command.
     * @return the {@link PlayerRole} corresponding to which
     * team-configuration should be shown to the CommandSender.
     */
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

    /**
     * Determines if a non-default label (anything that is not the command
     * name) was used to issue the command.
     *
     * @param label the label used to issue the command.
     * @return true if a non-default label was used, false otherwise.
     */
    private boolean labelUsed(final String label) {

        return label.equalsIgnoreCase("listrunners")
                || label.equalsIgnoreCase("listhunters")
                || label.equalsIgnoreCase("listteams");

    }

    /**
     * Gets which game's team-configuration(s) should be retrieved.
     *
     * @param sender the {@link CommandSender} that issued the command.
     * @param args the arguments sent when the command was issued.
     * @param labelsUsed the label used to issue the command.
     * @return the {@link Game}-object of which the team-configurations
     * should be listed. If no team was found, it will return null.
     */
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

    /**
     * Compiles (a part of) the message that should be sent to the
     * CommandSender.
     * @param playerStates the {@link PlayerState}-objects associated with
     *                     the {@link Game}.
     * @param roleToList the {@link PlayerRole} that determines which
     *                   team-configuration should be listed.
     * @return (a part of) the message the will be sent to the CommandSender.
     */
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
