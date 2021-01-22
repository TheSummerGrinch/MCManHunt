package io.github.thesummergrinch.mcmanhunt;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.MCManHuntStringCache;
import io.github.thesummergrinch.mcmanhunt.cache.UniverseCache;
import io.github.thesummergrinch.mcmanhunt.commands.game.info.ListGamesCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.info.ListHuntersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.info.ListRunnersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.ManHuntRuleCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow.*;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.universe.DestroyUniverseCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.universe.SetDestroyUniverseOnStopCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.player.JoinGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.player.JoinTeamCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.plugin.info.ManHuntVersionCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.*;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.FileConfigurationLoader;
import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class MCManHunt extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(PlayerState.class);
        ConfigurationSerialization.registerClass(Game.class);
        ConfigurationSerialization.registerClass(Universe.class);
        ConfigurationSerialization.registerClass(GameCache.class);
        ConfigurationSerialization.registerClass(MCManHuntStringCache.class);
        ConfigurationSerialization.registerClass(GameState.class);
    }


    @Override
    public void onEnable() {
        // Plugin startup logic
        registerEventHandlers();
        registerCommands();
        GameCache.getInstance().getGameCacheFromSave();
        FileConfigurationLoader.getInstance().loadStrings();
        enableMetrics();
    }

    @Override
    public void onDisable() {
        UniverseCache.getInstance().onDisable();
        FileConfigurationLoader.getInstance().saveGames();
    }

    private void registerCommands() {
        this.getCommand("initializegame").setExecutor(new InitializeGameCommandExecutor());
        this.getCommand("joingame").setExecutor(new JoinGameCommandExecutor());
        this.getCommand("startgame").setExecutor(new StartGameCommandExecutor());
        this.getCommand("jointeam").setExecutor(new JoinTeamCommandExecutor());
        this.getCommand("listhunters").setExecutor(new ListHuntersCommandExecutor());
        this.getCommand("listrunners").setExecutor(new ListRunnersCommandExecutor());
        this.getCommand("listgames").setExecutor(new ListGamesCommandExecutor());
        this.getCommand("pausegame").setExecutor(new PauseGameCommandExecutor());
        this.getCommand("resumegame").setExecutor(new ResumeGameCommandExecutor());
        this.getCommand("stopgame").setExecutor(new StopGameCommandExecutor());
        this.getCommand("destroyuniverse").setExecutor(new DestroyUniverseCommandExecutor());
        this.getCommand("setdestroyuniverseonstop").setExecutor(new SetDestroyUniverseOnStopCommandExecutor());
        this.getCommand("manhuntversion").setExecutor(new ManHuntVersionCommandExecutor());
        this.getCommand("manhuntrule").setExecutor(new ManHuntRuleCommandExecutor());
    }

    private void registerEventHandlers() {
        this.getServer().getPluginManager().registerEvents(new OnBlockDamageEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnEnderDragonDeathEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnManHuntWinEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDamagedEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDeathEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerInteractEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoinEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerMoveEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerPortalEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerRespawnEventHandler(), this);
    }

    private void enableMetrics() {
        final String allowMetrics = FileConfigurationLoader.getInstance().getConfigOptions().get("allow-metrics");
        if (allowMetrics != null) {
            if (Boolean.parseBoolean(allowMetrics)) {
                final int pluginID = 8784;
                new Metrics(this, pluginID);
                getLogger().log(Level.INFO, MCManHuntStringCache.getInstance().getStringFromCache("metrics-enabled"));
            } else {
                getLogger().log(Level.INFO, MCManHuntStringCache.getInstance().getStringFromCache("metrics-disabled"));
            }
        } else {
            FileConfigurationLoader.getInstance().saveConfigOption("allow-metrics", "true");
            getLogger().log(Level.INFO, MCManHuntStringCache.getInstance().getStringFromCache("metrics-enabled-on-next-launch"));
        }
    }

}
