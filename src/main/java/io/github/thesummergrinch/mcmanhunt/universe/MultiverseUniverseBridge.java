package io.github.thesummergrinch.mcmanhunt.universe;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.commands.ImportCommand;
import com.onarandombox.MultiverseNetherPortals.MultiverseNetherPortals;
import com.onarandombox.MultiverseNetherPortals.commands.LinkCommand;
import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class MultiverseUniverseBridge {

    private static volatile MultiverseUniverseBridge instance;

    private MultiverseUniverseBridge() {}

    public static MultiverseUniverseBridge getInstance() {
        if (instance != null) return instance;
        synchronized (MultiverseUniverseBridge.class) {
            instance = new MultiverseUniverseBridge();
        }
        return instance;
    }

    protected void registerWorldsInMultiverse(final CommandSender commandSender, final Universe universe) {

        Plugin multiversePortals = MCManHunt.getPlugin(MCManHunt.class).getServer().getPluginManager().getPlugin("Multiverse-NetherPortals");
        Plugin multiverseCore = MCManHunt.getPlugin(MCManHunt.class).getServer().getPluginManager().getPlugin("Multiverse-Core");

        new ImportCommand((MultiverseCore) multiverseCore).runCommand(commandSender, new ArrayList<String>() {{add(universe.getName()); add("normal");}});
        new ImportCommand((MultiverseCore) multiverseCore).runCommand(commandSender, new ArrayList<String>() {{add(universe.getName() + "_nether"); add("nether");}});
        new ImportCommand((MultiverseCore) multiverseCore).runCommand(commandSender, new ArrayList<String>() {{add(universe.getName() + "_the_end"); add("end");}});

        new LinkCommand((MultiverseNetherPortals) multiversePortals).runCommand(commandSender, new ArrayList<String>() {{add("nether"); add(universe.getName()); add(universe.getName() + "_nether");}});
        new LinkCommand((MultiverseNetherPortals) multiversePortals).runCommand(commandSender, new ArrayList<String>() {{add("end"); add(universe.getName()); add(universe.getName() + "_the_end");}});

    }

}
