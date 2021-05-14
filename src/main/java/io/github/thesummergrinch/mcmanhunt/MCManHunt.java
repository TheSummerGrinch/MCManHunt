package io.github.thesummergrinch.mcmanhunt;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.commands.chat.SayGlobalCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.chat.SayLobbyCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.info.ListGamesCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.info.ListRoleCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.debug.DebugCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.settings.ManHuntRuleCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow.InitializeGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow.PauseGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow.ResumeGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow.StartGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.gameflow.StopGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.settings.SetManHuntLanguageCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.universe.DestroyUniverseCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.op.universe.SetDestroyUniverseOnStopCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.player.JoinGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.player.JoinTeamCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.game.player.LeaveGameCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.plugin.info.ManHuntVersionCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnAsyncPlayerChatEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnBlockDamageEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnEnderDragonDeathEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnManHuntWinEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerDamagedEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerDeathEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerInteractEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerJoinEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerLeaveEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerMoveEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerPortalEventHandler;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.OnPlayerRespawnEventHandler;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.Game;
import io.github.thesummergrinch.mcmanhunt.game.gamecontrols.GameState;
import io.github.thesummergrinch.mcmanhunt.game.players.PlayerState;
import io.github.thesummergrinch.mcmanhunt.io.data.SavedGamesLoader;
import io.github.thesummergrinch.mcmanhunt.io.lang.LanguageFileLoader;
import io.github.thesummergrinch.mcmanhunt.io.settings.DefaultSettingsContainer;
import io.github.thesummergrinch.mcmanhunt.io.settings.FileConfigurationLoader;
import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

/**
 * @author Marcus Talbot (TheSummerGrinch)
 */
public final class MCManHunt extends JavaPlugin {

    @Override
    public void onEnable() {

        // Copy the config default over to file, if no config-file exists in
        // the datafolder.
        this.saveDefaultConfig();

        //Register serializable classes.
        registerSerializableClasses();

        // Load settings
        // TODO load settings in DefaultSettingsContainer maybe?
        FileConfigurationLoader.getInstance().loadDefaultSettings("settings");

        // Load existing games.
        // TODO make a config option to disable saving games
        GameCache.getInstance().getGameCacheFromSave("saved-games");

        // Saving the config.
        // TODO is this necessary. Doubt it.
        // this.saveConfig();

        // Load language file corresponding to the locale set in the config.
        loadLanguageFile();

        // Register event-handlers.
        registerEventHandlers();

        // Register commands.
        registerCommands();

        // Enable metrics if not first run, and enabled in config.
        enableMetrics();

        // Check for updates if enabled in config.
        checkForUpdate();

        // Open Plugin Messaging Channel, if bungeecord is enabled. Prone to
        // user error. Should check if it worked.
        if (DefaultSettingsContainer.getInstance().getBoolean("bungeecord" +
                "-enabled")) {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        }

    }

    @Override
    public void onDisable() {

        saveConfigFile();

    }

    private void registerSerializableClasses() {

        ConfigurationSerialization.registerClass(PlayerState.class);
        ConfigurationSerialization.registerClass(Game.class);
        ConfigurationSerialization.registerClass(Universe.class);
        ConfigurationSerialization.registerClass(GameCache.class);
        ConfigurationSerialization.registerClass(GameState.class);
        ConfigurationSerialization.registerClass(DefaultSettingsContainer.class);

    }

    /**
     * Registers all commands used by MCManHunt.
     */
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
        this.getCommand("leavegame").setExecutor(new LeaveGameCommandExecutor());
        this.getCommand("setlanguage").setExecutor(new SetManHuntLanguageCommandExecutor());
        this.getCommand("saylobby").setExecutor(new SayLobbyCommandExecutor());
        this.getCommand("sayglobal").setExecutor(new SayGlobalCommandExecutor());
        this.getCommand("mhdebug").setExecutor(new DebugCommandExecutor());

    }

    /**
     * Registers all necessary EventHandlers.
     */
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
        this.getServer().getPluginManager().registerEvents(new OnAsyncPlayerChatEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerLeaveEventHandler(), this);

    }

    /**
     * Enables bStats-{@link Metrics}.
     */
    private void enableMetrics() {

        if (DefaultSettingsContainer.getInstance().getBoolean("first-run")) {

            DefaultSettingsContainer.getInstance().setBoolean("first-run",
                    false);
            getLogger().log(Level.INFO, LanguageFileLoader.getInstance().getString("metrics-enabled-on-next-launch"));

        } else if (DefaultSettingsContainer.getInstance().getBoolean("allow" +
                "-metrics")) {

            final int pluginID = 8784;

            new Metrics(this, pluginID);

            getLogger().log(Level.INFO, LanguageFileLoader.getInstance().getString("metrics-enabled"));

        } else {

            getLogger().log(Level.INFO, LanguageFileLoader.getInstance().getString("metrics-disabled"));

        }
    }

    /**
     * Saves the configurations to file.
     */
    private void saveConfigFile() {

        DefaultSettingsContainer.getInstance().saveSettings(this);
        SavedGamesLoader.getInstance().saveGameCache(GameCache.getInstance());

    }

    /**
     * Checks for updates by comparing the most recent version number known
     * at SpigotMC.
     */
    private void checkForUpdate() {

        if (DefaultSettingsContainer.getInstance().getBoolean(
                "enable-update-checking")) {

            new UpdateChecker(this, 83665).getVersion(version -> {

                String[] publishedVersion = version.substring(1).split("\\.");
                String[] currentVersion = this.getDescription().getVersion().split("\\.");

                if (publishedVersion.length == currentVersion.length) {

                    for (int i = 0; i < publishedVersion.length; i++) {

                        if (Integer.parseInt(publishedVersion[i]) > Integer.parseInt(currentVersion[i])) {

                            getLogger().warning("A new version is available: " + version);

                            return;

                        }

                    }

                } else if (publishedVersion.length < (currentVersion).length) {

                    for (int i = 0; i < publishedVersion.length; i++) {

                        if (Integer.parseInt(publishedVersion[i]) > Integer.parseInt(currentVersion[i])) {

                            getLogger().warning("A new version is available: " + version);

                            return;

                        }

                    }

                } else {

                    for (int i = 0; i < currentVersion.length; i++) {

                        if (Integer.parseInt(publishedVersion[i]) > Integer.parseInt(currentVersion[i])) {

                            getLogger().warning("A new version is available: " + version);

                            return;

                        }
                    }
                }
            });
        }
    }

    /**
     * Asynchronously loads the LanguageFileLoader.
     */
    private void loadLanguageFile() {

        new BukkitRunnable() {

            @Override
            public void run() {

                LanguageFileLoader.getInstance();

            }
        }.runTaskAsynchronously(this);

    }

}
