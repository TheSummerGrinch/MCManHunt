package io.github.thesummergrinch.mcmanhunt.universe;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.UniverseCache;
import io.github.thesummergrinch.mcmanhunt.io.WorldDirectoryFileVisitor;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class Universe implements ConfigurationSerializable {

    private final HashMap<String, World> worldHashMap;

    private final String universeName;

    private boolean destroyWhenGameIsStopped;
    private boolean markedForDestruction;

    public Universe(final String universeName) {
        this.worldHashMap = new HashMap<>();
        this.universeName = universeName;
        populateUniverse();
        UniverseCache.getInstance().cacheUniverse(this.universeName, this);
        this.destroyWhenGameIsStopped = !universeName.equals("world");
        this.markedForDestruction = false;
    }

    public static @NotNull Universe deserialize(Map<String, Object> objects) {
        Universe universe = new Universe((String) objects.get("name"));
        universe.setDestroyWhenGameIsStopped((Boolean) objects.get("destroy-when-game-is-stopped"));
        return universe;
    }

    private void populateUniverse() {
        final World overworld = WorldCreator.name(universeName).environment(World.Environment.NORMAL)
                .generateStructures(true).type(WorldType.NORMAL).createWorld();
        final World nether = WorldCreator.name(universeName + "_nether").environment(World.Environment.NETHER)
                .generateStructures(true).type(WorldType.NORMAL).createWorld();
        final World end = WorldCreator.name(universeName + "_the_end").environment(World.Environment.THE_END)
                .generateStructures(true).type(WorldType.NORMAL).createWorld();
        this.worldHashMap.put(overworld.getName(), overworld);
        this.worldHashMap.put(nether.getName(), nether);
        this.worldHashMap.put(end.getName(), end);
    }

    @Nullable
    public World getWorld(@NotNull final String worldName) {
        return worldHashMap.get(worldName);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> objects = new HashMap<>();
        objects.put("name", this.universeName);
        objects.put("destroy-when-game-is-stopped", this.destroyWhenGameIsStopped);
        return objects;
    }

    public @NotNull String getName() {
        return this.universeName;
    }

    public void setDifficulty(@NotNull final Difficulty difficulty) {
        this.worldHashMap.values().forEach(world -> world.setDifficulty(difficulty));
    }

    public void destroyUniverse() {
        this.worldHashMap.forEach((worldName, world) -> {
            Bukkit.unloadWorld(worldName, false);
            try {
                File worldDirectory = new File(Bukkit.getWorldContainer(), "/" + worldName);
                Files.walkFileTree(worldDirectory.toPath(), new WorldDirectoryFileVisitor());
            } catch (IOException exception) {
                MCManHunt.getPlugin(MCManHunt.class).getLogger().log(Level.SEVERE, "Could not delete World "
                        + worldName + ". Please check whether the world exists, " +
                        "or contact the developer (GitHub: Github.com/TheSummerGrinch).");
            }
        });
    }

    public boolean getDestroyWhenGameIsStopped() {
        return this.destroyWhenGameIsStopped;
    }

    public void setDestroyWhenGameIsStopped(final boolean destroyWhenGameIsStopped) {
        this.destroyWhenGameIsStopped = destroyWhenGameIsStopped;
    }

    public boolean getMarkedForDestruction() {
        return this.markedForDestruction;
    }

    public void setMarkedForDestruction(final boolean markedForDestruction) {
        this.markedForDestruction = true;
    }

    public <T> void setGameRule(GameRule<T> gameRule, T value) {
        this.worldHashMap.values().forEach(world -> world.setGameRule(gameRule, value));
    }

}
