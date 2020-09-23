package io.github.thesummergrinch.mcmanhunt.commands.config;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.game.GameController;
import io.github.thesummergrinch.mcmanhunt.game.cache.UserCache;
import io.github.thesummergrinch.mcmanhunt.game.entity.PlayerRole;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetForceRandomTeamCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() && args != null && args.length == 1
                && GameController.getInstance().getGameState().equals(GameController.GameState.DEFAULT)) {
            switch (args[0].toLowerCase()) {
                case "true":
                    GameController.getInstance().setManHuntGameMode(GameController.GameMode.RANDOM);
                    UserCache.getInstance().getAllPlayerStates().forEach(playerState -> playerState.setPlayerRole(PlayerRole.DEFAULT));
                    MCManHunt.getPlugin(MCManHunt.class).getServer().broadcastMessage("The ManHunt-Gamemode has been " +
                            "set to Random. You can only join using /joinrandomteam.");
                    return true;
                case "false":
                    GameController.getInstance().setManHuntGameMode(GameController.GameMode.NORMAL);
                    MCManHunt.getPlugin(MCManHunt.class).getServer().broadcastMessage("The ManHunt-Gamemode has been set" +
                            "to Normal. You can join a team using /joinhunters, /joinrunners and /joinrandomteam.");
                    return true;
                default:
                    sender.sendMessage("The argument entered is invalid. Please only use \"true\", or \"false\".");
                    return true;
            }
        }
        return false;
    }
}
