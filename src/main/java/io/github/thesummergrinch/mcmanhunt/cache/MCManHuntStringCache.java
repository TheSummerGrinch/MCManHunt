package io.github.thesummergrinch.mcmanhunt.cache;

import io.github.thesummergrinch.mcmanhunt.io.settings.FileConfigurationLoader;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public final class MCManHuntStringCache implements ConfigurationSerializable {

    private static final Map<String, String> stringCache = new HashMap<>();
    private static volatile MCManHuntStringCache instance;

    private MCManHuntStringCache() {
    }

    public static MCManHuntStringCache getInstance() {
        MCManHuntStringCache mcManHuntStringCache = instance;
        if (mcManHuntStringCache == null) {
            synchronized (MCManHuntStringCache.class) {
                if (instance == null) {
                    mcManHuntStringCache = instance = new MCManHuntStringCache();
                }
            }
        }
        return mcManHuntStringCache;
    }

    public static MCManHuntStringCache deserialize(Map<String, Object> objects) {
        instance = getInstance();
        objects.forEach((key, value) -> MCManHuntStringCache.stringCache.put(key, (String) value));
        return getInstance();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return new HashMap<>(stringCache);
    }

    @Deprecated
    public void addStringsToCache(final Map<String, String> strings) {
        stringCache.putAll(strings);
    }

    @Deprecated
    public String getStringFromCache(final String key) {
        String stringFromCache = stringCache.get(key);
        if (stringFromCache != null) return ChatColor.DARK_PURPLE + "[MCManHunt] " + ChatColor.GOLD + stringFromCache;
        stringFromCache = FileConfigurationLoader.getInstance().getStandardStringMap().get(key);
        if (stringFromCache != null) {
            stringCache.put(key, stringFromCache);
            return ChatColor.DARK_PURPLE + "[MCManHunt] " + ChatColor.GOLD + stringFromCache;
        } else {
            throw new NullPointerException("String with identifier: \"" + key + "\" could not be found. " +
                    "Please contact developer at GitHub.com/TheSummerGrinch");
        } //TODO Fix it so that Tracker in InteractEvent can be taken from the cache.
    }
}
