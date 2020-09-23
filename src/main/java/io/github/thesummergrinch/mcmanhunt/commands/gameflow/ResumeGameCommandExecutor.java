package io.github.thesummergrinch.mcmanhunt.commands.gameflow;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.game.GameController;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class ResumeGameCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() && GameController.getInstance().getGameState().equals(GameController.GameState.PAUSED)) {
            MCManHunt.getPlugin(MCManHunt.class).getServer().broadcastMessage(ChatColor.RED + "The Game will resume in " +
                    "5 seconds!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    GameController.getInstance().resumeGame();
                }
            }.runTaskLater(MCManHunt.getPlugin(MCManHunt.class), 100);
            return true;
        }
        return false;
    }
}
