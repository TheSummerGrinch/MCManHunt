# MCManHunt
ManHunt-plugin for Minecraft 1.16

Compatible with SpigotMC 1.16.x servers and PaperMC 1.16.x servers!

Become the Hunter or Hunted in this thrilling minigame! Every Hunter receives one compass per player on the Runner-team. The Runners receive a 30 second head-start. After that, the hunt is on!

This plugin contains no complicated Discord-integration. It is just the barebones-game.

If you like this plugin, please leave a rating and/or feedback on what you would like in the next update!

How to play

First, a player with OP-privilleges uses the /initializegame [GameName]-command. This creates a dedicated Overworld, Nether and End for the ManHunt-game. Note: This takes a lot of server resources, so lag for roughly 15 seconds should be expected while the world are being initialized.

The players may then join the game by using the /joingame [GameName]-command.

The players choose a team, by using the /jointeam [hunters|runners]-command (aliases: /joinhunters and /joinrunners). Players can also switch teams before the game starts, by joining the other team, using the aforementioned join-commands.

When everyone is ready, a player with OP-privilleges can use the /startgame [GameName]-command, to start the game. Everyone will be locked into place for 10 seconds, after which the Runners get a 30-second headstart.

The Hunters will receive a compass for every Runner, to track their direction, which can be updated by placing it in your main hand and right-clicking. The Hunters win by killing every Runner once, and the Runners win by killing the Enderdragon! Good luck and have fun!

Additionally, if all players leave during a game, and the server is (gracefully) stopped, the game will be saved and can be continued later.

Features

    Become a speedrunner and beat the game before you are killed!
    Play with friends and team up!
    Pause during the game for breaks!



Commands

    /jointeam [hunters|runners] - Join the specified team. (aliases: /joinhunters and /joinrunners)
    /listhunters - Lists the players on the Hunter-team.
    /listrunners - Lists the players on the Runner-team.
    /listgames - List all initialized games.
    /startgame - Starts the game. (Requires OP)
    /stopgame - Stops the ongoing game. (Requires OP)
    /pausegame - Pauses the game. (Requires OP)
    /resumegame - Resumes the game. (Requires OP)
    /destroyuniverse [GameName] - Deletes the world-folders of the specified game.
    /setdestroyuniverseonstop [GameName] [true|false] - Sets whether or not the world-folders of the specified game will be automatically deleted when the game is stopped. (default: true)



Permissions

    mcmanhunt.game.op - Gives access to various OP-only commands. Default = OP.
    mcmanhunt.op.gameflow - Gives access to OP-only gameflow commands (start/stop/resume). Default = OP.
    mcmanhunt.op.universe - Allows access to /destroyuniverse and /setdestroyuniverseonstop. Default = OP.
    mcmanhunt.game.info - Allows access to /listgames, /listhunters and /listrunners. Default = true.
    mcmanhunt.game.player - Allows players to join games and teams. Default = true.
    mcmanhunt.plugin.info - Allows access to the /manhuntversion-command. Default = OP.



Configuration

    No Player configuration


Installation

Place the plugin-jar into your plugin-folder. A config-file will be generated on the first run.


Metrics / Data-collection

NOTE: DATA COLLECTION IS CURRENTLY DISABLED IN THE LATEST VERSION OF MCMANHUNT. THE INFORMATION BELOW IS TRUE FOR VERSIONS 1.5.X AND LOWER.

MCManHunt uses bStats to collect data. You can disable bStats data-collection in the bStats-config (Serverroot -> plugins -> bStats -> config.yml -> enabled: false). Additionally, you can also specifically stop data-collection for MCManHunt by changing the allow-metrics field, in the plugin.yml, to false.


MCManHunt collects the following datapoints:

    Amount of servers that run MCManHunt and amount of players using these servers - I keep track of these metrics so I know what level of active development is expected of me.
    Online/Offline mode - I do not support the use Offline servers; therefore, I need to know if and when to block service for Offline servers.
    Server Software - Allows me to anticipate and mend problems based on the known problems and bugs of the server software.
    Plugin Version - Allows me to investigate what makes one version more appealing than another.
    System Information (Core count, Architecture, OS) - Allows me to anticipate and mend problems based upon System properties.
    Server Location - I may enable localization, but I need to know for what regions.
    Java Version - Once a significant amount of servers use Java 9 or higher, I too can switch to developing using a higher version.
