package io.github.thesummergrinch.mcmanhunt;

import io.github.thesummergrinch.mcmanhunt.cache.GameCache;
import io.github.thesummergrinch.mcmanhunt.cache.UniverseCache;
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
import io.github.thesummergrinch.mcmanhunt.universe.Universe;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * @author Marcus Talbot (TheSummerGrinch)
 */
public final class MCManHunt extends JavaPlugin {

    private boolean updateAvailable = false;
    private FileConfiguration fileConfiguration;
    private String versionString = "";
    private @NotNull PluginManager pluginManager;

    @Override
    public void onEnable() {

        // Copy the config default over to file, if no config-file exists in
        // the datafolder.
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.fileConfiguration = this.getConfig();


        //Register serializable classes.
        registerSerializableClasses();

        // Load existing games.
        // TODO make a config option to disable saving games
        GameCache.getInstance().getGameCacheFromSave("saved-games");

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
        if (this.fileConfiguration.getBoolean("bungeecord" +
                "-enabled")) {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        }

    }

    @Override
    public void onDisable() {

        UniverseCache.getInstance().onDisable();
        saveConfigFile();

    }

    private void registerSerializableClasses() {

        ConfigurationSerialization.registerClass(PlayerState.class);
        ConfigurationSerialization.registerClass(Game.class);
        ConfigurationSerialization.registerClass(Universe.class);
        ConfigurationSerialization.registerClass(GameCache.class);
        ConfigurationSerialization.registerClass(GameState.class);

    }

    /**
     * Registers all commands used by MCManHunt.
     */
    private void registerCommands() {

        this.getCommand("initializegame").setExecutor(new InitializeGameCommandExecutor(this));
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
        this.getCommand("manhuntversion").setExecutor(new ManHuntVersionCommandExecutor(this));
        this.getCommand("manhuntrule").setExecutor(new ManHuntRuleCommandExecutor());
        this.getCommand("leavegame").setExecutor(new LeaveGameCommandExecutor());
        this.getCommand("setlanguage").setExecutor(new SetManHuntLanguageCommandExecutor(this));
        this.getCommand("saylobby").setExecutor(new SayLobbyCommandExecutor());
        this.getCommand("sayglobal").setExecutor(new SayGlobalCommandExecutor());
        this.getCommand("mhdebug").setExecutor(new DebugCommandExecutor(this));

    }

    /**
     * Registers all necessary EventHandlers.
     */
    private void registerEventHandlers() {

        final PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new OnBlockDamageEventHandler(), this);
        pluginManager.registerEvents(new OnEnderDragonDeathEventHandler(), this);
        pluginManager.registerEvents(new OnManHuntWinEventHandler(this), this);
        pluginManager.registerEvents(new OnPlayerDamagedEventHandler(), this);
        pluginManager.registerEvents(new OnPlayerDeathEventHandler(), this);
        pluginManager.registerEvents(new OnPlayerInteractEventHandler(), this);
        pluginManager.registerEvents(new OnPlayerJoinEventHandler(this), this);
        pluginManager.registerEvents(new OnPlayerMoveEventHandler(), this);
        pluginManager.registerEvents(new OnPlayerPortalEventHandler(), this);
        pluginManager.registerEvents(new OnPlayerRespawnEventHandler(), this);
        pluginManager.registerEvents(new OnAsyncPlayerChatEventHandler(), this);
        pluginManager.registerEvents(new OnPlayerLeaveEventHandler(), this);

    }

    /**
     * Enables bStats-{@link Metrics}.
     */
    private void enableMetrics() {

        if (this.fileConfiguration.getBoolean("first-run")) {

            this.fileConfiguration.set("first-run",
                    false);
            getLogger().log(Level.INFO, LanguageFileLoader.getInstance().getString("metrics-enabled-on-next-launch"));

        } else if (this.fileConfiguration.getBoolean("allow" +
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

        this.saveConfig();
        SavedGamesLoader.getInstance().saveGameCache(GameCache.getInstance());

    }

    /**
     * Checks for updates by comparing the most recent version number known
     * at SpigotMC.
     */
    private void checkForUpdate() {

        if (this.fileConfiguration.getBoolean(
                "enable-update-checking")) {

            new UpdateChecker(this, 83665).getVersion(version -> {

                versionString = version.substring(1);
                String[] publishedVersion = versionString.split("\\.");
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

    public boolean isUpdateAvailable() {
        return this.updateAvailable;
    }

    public String getVersionString() {
        return this.versionString;
    }

    public FileConfiguration getFileConfiguration() {
        return this.fileConfiguration;
    }

}
