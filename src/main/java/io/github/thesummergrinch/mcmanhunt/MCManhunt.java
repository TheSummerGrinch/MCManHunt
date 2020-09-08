package io.github.thesummergrinch.mcmanhunt;

import io.github.thesummergrinch.mcmanhunt.commands.*;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.*;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCManhunt extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new OnCompassInteractEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnDeathEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnRespawnEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnLogoutEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLoginEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerMoveEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnEnderDragonDeathEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDimensionChangeEventHandler(), this);
        this.getCommand("addhunter").setExecutor(new AddHunterCommandExecutor());
        this.getCommand("addrunner").setExecutor(new AddRunnerCommandExecutor());
        this.getCommand("resetplayerroles").setExecutor(new ResetPlayerRolesCommandExecutor());
        this.getCommand("startgame").setExecutor(new StartGameCommandExecutor());
        this.getCommand("pausegame").setExecutor(new PauseGameCommandExecutor());
        this.getCommand("resumegame").setExecutor(new ResumeGameCommandExecutor());
        this.getCommand("stopgame").setExecutor(new StopGameCommandExecutor());
        this.getCommand("listrunners").setExecutor(new ListRunnersCommandExecutor());
        this.getCommand("listhunters").setExecutor(new ListHuntersCommandExecutor());
        this.getCommand("joinhunters").setExecutor(new JoinHuntersCommandExecutor());
        this.getCommand("joinrunners").setExecutor(new JoinRunnersCommandExecutor());
        this.getCommand("leaverunners").setExecutor(new LeaveRunnersCommandExecutor());
        this.getCommand("leavehunters").setExecutor(new LeaveHuntersCommandExecutor());
        if(ManHuntUtilities.getConfig().getBoolean("allow-metrics")) {
            final int pluginID = 8784;
            final Metrics metrics = new Metrics(this, pluginID);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
