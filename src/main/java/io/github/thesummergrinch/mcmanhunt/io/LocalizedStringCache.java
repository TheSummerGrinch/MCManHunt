package io.github.thesummergrinch.mcmanhunt.io;

import java.util.HashMap;

public final class LocalizedStringCache {

    private static volatile LocalizedStringCache instance;

    private final HashMap<String, String> localizedStringMap;

    private LocalizedStringCache() {
        localizedStringMap = new HashMap<>();
    }

    public static LocalizedStringCache getInstance() {
        LocalizedStringCache localizedStringCache = instance;
        if (localizedStringCache != null) return localizedStringCache;
        synchronized (LocalizedStringCache.class) {
            if (instance == null) instance = new LocalizedStringCache();
            return instance;
        }
    }

    public void addLocalizedString(final String key, final String value) {
        this.localizedStringMap.put(key, value);
    }

    public String getLocalizedString(final String key) {
        return this.localizedStringMap.get(key);
    }

}
