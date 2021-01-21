package io.github.thesummergrinch.mcmanhunt.cache;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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

    public void addStringsToCache(final Map<String, String> strings) {
        stringCache.putAll(strings);
    }

    public String getStringFromCache(final String key) {
        return stringCache.get(key);
    }
}
