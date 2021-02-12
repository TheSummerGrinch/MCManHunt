package io.github.thesummergrinch.mcmanhunt.universe;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.cache.UniverseCache;
import io.github.thesummergrinch.mcmanhunt.io.WorldDirectoryFileVisitor;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Monster;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Wrapper class used to hold and interact with the three {@link org.bukkit.World}-objects necessary to construct an
 * MCManHunt-game.
 */
public final class Universe implements ConfigurationSerializable {

    // The Map<String, UUID> that effectively functions as the cache.
    private final HashMap<String, UUID> worldHashMap;

    // Identifier used to register the Universe-object in the UniverseCache.
    private final String universeName;

    // Toggleable flag used to denote whether or not the Universe, and the
    // related persistent world-files should be deleted once the game ends
    // (naturally or through commands). True by default, unless the game will be
    // played in the default world.
    private boolean destroyWhenGameIsStopped;

    // Flag used to determine whether or not the persistent world-files of this
    // Universe should be deleted when the server is (gracefully) stopped.
    private boolean markedForDestruction;

    public Universe(final String universeName) {

        this.worldHashMap = new HashMap<>();
        this.universeName = universeName;

        populateUniverse();
        UniverseCache.getInstance().cacheUniverse(this.universeName, this);

        this.destroyWhenGameIsStopped = !universeName.equals("world");
        this.markedForDestruction = false;

    }

    /**
     * Method solely used to deserialize a yml-representation of a
     * {@link Universe}, as specified in the {@link #serialize}-method.
     *
     * @param objects - Map of key-value pairs, representing the
     * {@link Universe}-object.
     * @return universe - a reconstruction of the saved {@link Universe}.
     */
    @SuppressWarnings("unused")
    public static @NotNull Universe deserialize(Map<String, Object> objects) {

        Universe universe = new Universe((String) objects.get("name"));

        universe.setDestroyWhenGameIsStopped(
                (Boolean) objects.get("destroy-when-game-is-stopped")
        );

        return universe;
    }

    /**
     * Generates an Overworld, a Nether and an End dimension, and stores them
     * in the {@link HashMap}.
     */
    private void populateUniverse() {

        final World overworld = WorldCreator.name(universeName)
                .environment(World.Environment.NORMAL)
                .generateStructures(true).type(WorldType.NORMAL).createWorld();

        final World nether = WorldCreator.name(universeName + "_nether")
                .environment(World.Environment.NETHER)
                .generateStructures(true).type(WorldType.NORMAL).createWorld();

        final World end = WorldCreator.name(universeName + "_the_end")
                .environment(World.Environment.THE_END)
                .generateStructures(true).type(WorldType.NORMAL).createWorld();

        this.worldHashMap.put(overworld.getName(), overworld.getUID());
        this.worldHashMap.put(nether.getName(), nether.getUID());
        this.worldHashMap.put(end.getName(), end.getUID());

        if (MCManHunt.getPlugin(MCManHunt.class).getServer().getPluginManager()
                .getPlugin("Multiverse-Core") != null
                && MCManHunt.getPlugin(MCManHunt.class).getServer()
                .getPluginManager()
                .getPlugin("Multiverse-NetherPortals") != null) {

            MultiverseUniverseBridge.getInstance()
                    .registerWorldsInMultiverse
                            (Bukkit.getConsoleSender(), this);

        }
    }

    /**
     * Gets the {@link org.bukkit.World} stored with the given key.
     *
     * @param worldName - key to the {@link org.bukkit.World}-object.
     * @return {@link org.bukkit.World}
     */
    @Nullable
    public World getWorld(@NotNull final String worldName) {

        return Bukkit.getWorld(worldName);

    }

    /**
     * Serializes the {@link Universe}-object into a yml-representation.
     *
     * @return {@link Map} containing key-value pairs representing the
     * {@link Universe}.
     */
    @Override
    public @NotNull Map<String, Object> serialize() {

        HashMap<String, Object> objects = new HashMap<>();

        objects.put("name", this.universeName);
        objects.put("destroy-when-game-is-stopped", this.destroyWhenGameIsStopped);

        return objects;

    }

    /**
     * Gets the name of the {@link Universe}.
     *
     * @return - the universe name.
     */
    public @NotNull String getName() {

        return this.universeName;

    }

    /**
     * Sets the {@link Difficulty} for each of the {@link World}-objects stored
     * in the {@link Universe}.
     *
     * @param difficulty - target difficulty
     */
    public void setDifficulty(@NotNull final Difficulty difficulty) {

        this.worldHashMap.values().forEach(worldUID
            -> Bukkit.getWorld(worldUID).setDifficulty(difficulty));

    }

    /**
     * Unloads all the {@link World}-objects stored in this {@link Universe},
     * and deletes the corresponding persistent world-files.
     */
    public void destroyUniverse() {

        this.worldHashMap.keySet().forEach(worldName -> {

            Bukkit.unloadWorld(worldName, false);

            try {

                File worldDirectory = new File(Bukkit.getWorldContainer(), "/" + worldName);

                Files.walkFileTree(worldDirectory.toPath(), new WorldDirectoryFileVisitor());

            } catch (IOException exception) {

                MCManHunt.getPlugin(MCManHunt.class).getLogger().log(Level.SEVERE,
                        "Could not delete World " + worldName + ". Please " +
                                "check whether the world exists, or contact " +
                                "the developer (GitHub: Github.com/TheSummerGrinch).");
            }
        });

    }

    /**
     * @return boolean - whether or not the {@link Universe} should be
     * permanently deleted when the game ends.
     */
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

        unloadAndDestroy();

    }

    public void unloadAndDestroy() {

        if (this.universeName.equals("world")) return;

        for (Map.Entry<String, UUID> entry : this.worldHashMap.entrySet()) {

            String name = entry.getKey();

            Bukkit.unloadWorld(name, !markedForDestruction);

        }

        if (!markedForDestruction) return;

        if (MCManHunt.getPlugin(MCManHunt.class).getServer().getPluginManager()
                .getPlugin("Multiverse-Core") != null) {

            MultiverseUniverseBridge.getInstance()
                    .unloadAndDestroy(Bukkit.getConsoleSender(), this);

            UniverseCache.getInstance().removeUniverse(this.universeName);

            return;

        }

        this.worldHashMap.forEach((worldName, world) -> {

            File worldDirectory = new File(Bukkit.getWorldContainer(), "/" + worldName);

            new BukkitRunnable() {

                @Override
                public void run() {

                    if (!markedForDestruction) return;

                    try {

                        Files.walkFileTree(worldDirectory.toPath(), new WorldDirectoryFileVisitor());

                    } catch (IOException exception) {

                        MCManHunt.getPlugin(MCManHunt.class).getLogger()
                                .log(Level.SEVERE, "Could not delete World "
                                    + worldName + ". Please check whether the world exists, " +
                                    "or contact the developer (GitHub: Github.com/TheSummerGrinch).");

                    }
                }
            }.runTaskAsynchronously(MCManHunt.getPlugin(MCManHunt.class));
        });

        UniverseCache.getInstance().removeUniverse(this.universeName);

    }

    /**
     * Sets the specified {@link GameRule} to the specified value. E.g.
     * {@code setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)} disables the
     * daylight-cycle of the Worlds stored in this
     * {@link Universe}.
     *
     * @param gameRule - The {@link GameRule} to be changed/set.
     * @param value    - The new value.
     * @param <T>      - Generic type
     */
    public <T> void setGameRule(GameRule<T> gameRule, T value) {

        this.worldHashMap.values().forEach(worldUID
                -> Bukkit.getWorld(worldUID).setGameRule(gameRule, value));

    }

    public void despawnMobs() {

        for (Map.Entry<String, UUID> entry : this.worldHashMap.entrySet()) {

            UUID worldUID = entry.getValue();

            for (Entity entity : Bukkit.getWorld(worldUID).getEntities()) {

                if (entity instanceof Monster || entity instanceof Flying) {

                    entity.remove();

                }

            }
        }
    }
}
