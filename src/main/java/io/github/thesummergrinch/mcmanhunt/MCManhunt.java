package io.github.thesummergrinch.mcmanhunt;

import io.github.thesummergrinch.mcmanhunt.commands.config.SetMaxHuntersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.config.SetMaxRunnersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.gameflow.PauseGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.gameflow.ResumeGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.gameflow.StartGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.gameflow.StopGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.player.admin.AddHunterCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.player.admin.AddRunnerCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.player.info.ListHuntersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.player.info.ListRunnersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.player.team.JoinHuntersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.player.team.JoinRunnersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.player.team.LeaveTeamCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.*;
import io.github.thesummergrinch.mcmanhunt.io.FileConfigurationLoader;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class MCManHunt extends JavaPlugin {

    @Override
    public void onEnable() {
        registerEvents();
        registerCommandExecutors();
        if (FileConfigurationLoader.getInstance().getBoolean("allow-metrics")) enableMetrics();
    }

    @Override
    public void onDisable() {
        FileConfigurationLoader.getInstance().saveConfig();
    }

    private void registerCommandExecutors() {
        this.getCommand("setmaxhunters").setExecutor(new SetMaxHuntersCommandExecutor());
        this.getCommand("setmaxrunners").setExecutor(new SetMaxRunnersCommandExecutor());
        this.getCommand("pausegame").setExecutor(new PauseGameCommandExecutor());
        this.getCommand("startgame").setExecutor(new StartGameCommandExecutor());
        this.getCommand("stopgame").setExecutor(new StopGameCommandExecutor());
        this.getCommand("resumegame").setExecutor(new ResumeGameCommandExecutor());
        this.getCommand("addhunter").setExecutor(new AddHunterCommandExecutor());
        this.getCommand("addrunner").setExecutor(new AddRunnerCommandExecutor());
        this.getCommand("listhunters").setExecutor(new ListHuntersCommandExecutor());
        this.getCommand("listrunners").setExecutor(new ListRunnersCommandExecutor());
        this.getCommand("joinhunters").setExecutor(new JoinHuntersCommandExecutor());
        this.getCommand("joinrunners").setExecutor(new JoinRunnersCommandExecutor());
        this.getCommand("leaveteam").setExecutor(new LeaveTeamCommandExecutor());
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new OnPlayerMoveEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnCompassInteractEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoinEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDeathEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnBlockDamageEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerRespawnEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerPortalEventHandler(), this);
    }

    private void enableMetrics() {
        final int pluginID = 8784;
        new Metrics(this, pluginID);
        getLogger().log(Level.INFO, "Metrics are enabled.");
    }

}
