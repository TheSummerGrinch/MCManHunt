# MCManHunt
ManHunt-plugin for Minecraft 1.16

Become the Hunter or Hunted in this thrilling minigame! Every Hunter receives one compass per player on the Runner-team. The Runners receive a 30 second head-start. After that, the hunt is on!


This plugin contains no complicated Discord-integration. It is just the barebones-game.

 

How to play

 

The players choose a team, by using the /joinhunters and /joinrunners commands. Players can also switch teams before the game starts, by using the /leavehunters or /leaverunners commands and joining the other team, using the aforementioned join-commands.

 

When everyone is ready, a player with OP-privilleges can use the /startgame command, to start the game. Everyone will be locked into place for 10 seconds, after which the Runners get a 30-second headstart.

 

The Hunters will receive a compass for every Runner, to track their direction. The Hunters win by killing every Runner once, and the Runners win by killing the Enderdragon! Good luck and have fun!

 

 

Features

 

    Become a speedrunner and beat the game before you are killed!
    Play with friends and team up!
    Pause during the game for breaks!

 

 

Commands

 

    /joinhunters - The user joins the Hunter-team.
    /joinrunners - The user joins the Runner-team.
    /leavehunters - The user leaves the Hunter-team.
    /leaverunners - The user leaves the Runner-team.
    /listhunters - Lists the players on the Hunter-team.
    /listrunners - Lists the players on the Runner-team.
    /startgame - Starts the game. (Requires OP)
    /stopgame - Stops the ongoing game. (Requires OP)
    /pausegame - Pauses the game. (Requires OP)
    /resumegame - Resumes the game. (Requires OP)
    /addhunter [playername] - Adds specified player to the Hunter-team. (Requires OP)
    /addrunner [playername] - Adds the specified player to the Runner-team. (Requires OP)
    /resetplayerroles - Resets both team. (Requires OP)

 

 

Permissions

 

 

    mcmanhunt.roles - Allows access to /addrunner, /addhunter and /resetplayerroles. Default = op
    mcmanhunt.gamestate - Allows access to /startgame, /stopgame, /pausegame and /resumegame. Default = op.
    mcmanhunt.info - Allows access to /listhunters and /listrunners. Default = true.
    mcmanhunt.roles.player - Allows access to /joinhunters, /joinrunners, /leavehunters and /leaverunners. Default = true.

 

 

Configuration

 

    max-hunters - Integer value. Sets the maximum number of hunters on a team. Must be greater than 0.
    max-runners - Integer value. Sets the maximum number of runners on a team. Must be greater than 0.
    allow-metrics - Boolean value. If true, allows for the collection of data (see below for details). If false, metrics are disabled.

 

 

Installation

 

Place the plugin-jar into your plugin-folder. A config-file will be generated on the first run.

 

 

 

Metrics / Data-collection

 

MCManHunt uses bStats to collect data. You can disable bStats data-collection in the bStats-config (Serverroot -> plugins -> bStats -> config.yml -> enabled: false). Additionally, you can also specifically stop data-collection for MCManHunt by changing the allow-metrics field, in the plugin.yml, to false.

 

 

MCManHunt collects the following datapoints:

 

    Amount of servers that run MCManHunt and amount of players using these servers - I keep track of these metrics so I know what level of active development is expected of me.
    Online/Offline mode - I do not support the use Offline servers; therefore, I need to know if and when to block service for Offline servers.
    Server Software - Allows me to anticipate and mend problems based on the known problems and bugs of the server software.
    Plugin Version - Allows me to investigate what makes one version more appealing than another.
    System Information (Core count, Architecture, OS) - Allows me to anticipate and mend problems based upon System properties.
    Server Location - I may enable localization, but I need to know for what regions.
    Java Version - Once a significant amount of servers use Java 9 or higher, I too can switch to developing using a higher version.
