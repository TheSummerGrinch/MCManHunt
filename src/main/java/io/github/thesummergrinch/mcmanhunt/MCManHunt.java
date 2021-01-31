package io.github.thesummergrinch.mcmanhunt;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.UniverseCache;
import io.github.thesummergrinch.mcmanhunt.commands.game.info.ListGamesCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.info.ListRoleCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.ManHuntRuleCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow.InitializeGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow.PauseGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow.ResumeGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow.StartGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow.StopGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.universe.DestroyUniverseCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.universe.SetDestroyUniverseOnStopCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.player.JoinGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.player.JoinTeamCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.plugin.info.ManHuntVersionCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnBlockDamageEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnEnderDragonDeathEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnManHuntWinEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerDamagedEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerDeathEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerInteractEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerJoinEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerMoveEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerPortalEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerRespawnEventHandler;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import io.github.thesummergrinch.mcmanhunt.io.settings.DefaultSettingsContainer;
import io.github.thesummergrinch.mcmanhunt.io.settings.FileConfigurationLoader;
import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * @author Marcus Talbot (TheSummerGrinch)
 */
public final class MCManHunt extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(PlayerState.class);
        ConfigurationSerialization.registerClass(Game.class);
        ConfigurationSerialization.registerClass(Universe.class);
        ConfigurationSerialization.registerClass(GameCache.class);
        ConfigurationSerialization.registerClass(GameState.class);
        ConfigurationSerialization.registerClass(DefaultSettingsContainer.class);
    }


    @Override
    public void onEnable() {
        // Plugin startup logic
        FileConfigurationLoader.getInstance().loadDefaultSettings("settings");
        GameCache.getInstance().getGameCacheFromSave("game-cache");
        //Testing only! getLogger().warning(LanguageFileLoader.getInstance().getString("hunters"));
        checkForUpdate();
        registerEventHandlers();
        registerCommands();
        enableMetrics();
        this.saveConfig();
    }

    @Override
    public void onDisable() {
        UniverseCache.getInstance().onDisable();
        saveConfigFile();
    }

    private void registerCommands() {
        this.getCommand("initializegame").setExecutor(new InitializeGameCommandExecutor());
        this.getCommand("joingame").setExecutor(new JoinGameCommandExecutor());
        this.getCommand("startgame").setExecutor(new StartGameCommandExecutor());
        this.getCommand("jointeam").setExecutor(new JoinTeamCommandExecutor());
        this.getCommand("listrole").setExecutor(new ListRoleCommandExecutor());
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
        if (DefaultSettingsContainer.getInstance().getSetting("first-run").equals("true")) {
            DefaultSettingsContainer.getInstance().setSetting("first-run", "false");
            getLogger().log(Level.INFO, LanguageFileLoader.getInstance().getString("metrics-enabled-on-next-launch"));
        } else if (DefaultSettingsContainer.getInstance().getSetting("allow-metrics").equals("true")) {
            final int pluginID = 8784;
            new Metrics(this, pluginID);
            getLogger().log(Level.INFO, LanguageFileLoader.getInstance().getString("metrics-enabled"));
        } else {
            getLogger().log(Level.INFO, LanguageFileLoader.getInstance().getString("metrics-disabled"));
        }
    }

    private void saveConfigFile() {
        FileConfigurationLoader.getInstance().saveItemToConfig("game-cache", GameCache.getInstance());
        FileConfigurationLoader.getInstance().saveItemToConfig("settings", DefaultSettingsContainer.getInstance());
        this.saveConfig();
    }

    private void checkForUpdate() {
        if (Boolean.parseBoolean(DefaultSettingsContainer.getInstance().getSetting("enable-update-checking"))) {
            new UpdateChecker(this, 83665).getVersion(version -> {
                if (this.getDescription().getVersion().equalsIgnoreCase(version.substring(1).trim())) {
                    return;
                } else {
                    this.getLogger().info("There is a new update available.");
                }
            });
        }
    }

}
