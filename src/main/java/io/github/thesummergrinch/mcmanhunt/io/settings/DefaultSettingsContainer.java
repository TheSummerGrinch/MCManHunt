package io.github.thesummergrinch.mcmanhunt.io.settings;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class DefaultSettingsContainer implements ConfigurationSerializable {

    private static volatile DefaultSettingsContainer instance;

    private final Map<String, Object> defaultSettings;

    private DefaultSettingsContainer() {

        this.defaultSettings = new HashMap<>();

    }

    public static DefaultSettingsContainer getInstance() {

        if (instance == null) {

            synchronized (DefaultSettingsContainer.class) {

                instance = new DefaultSettingsContainer();

            }

        }

        return instance;

    }

    @SuppressWarnings("unused")
    public static DefaultSettingsContainer deserialize(final Map<String, Object> objects) {

        DefaultSettingsContainer defaultSettingsContainer = DefaultSettingsContainer.getInstance();

        defaultSettingsContainer.defaultSettings.putAll((HashMap<String, String>) objects.get("settings"));

        return DefaultSettingsContainer.getInstance();

    }

    @Override
    public @NotNull Map<String, Object> serialize() {

        HashMap<String, Object> settingsMap = new HashMap<>();

        settingsMap.put("settings", defaultSettings);

        return settingsMap;

    }

    public int getInteger(final String key) {

        Object value = this.defaultSettings.get(key);

        if (value instanceof Integer) return (Integer) value;

        value =
                FileConfigurationLoader.getInstance().getDefaultSettings().get(key);

        if (value instanceof Integer) {
            this.defaultSettings.put(key, value);
            return (Integer) value;
        }

        return -1;

    }

    public boolean getBoolean(final String key) {

        Object value = this.defaultSettings.get(key);

        if (value instanceof Boolean) return (Boolean) value;

        value =
                FileConfigurationLoader.getInstance().getDefaultSettings().get(key);

        if (value instanceof Boolean) {
            this.defaultSettings.put(key, value);
            return (Boolean) value;
        }

        return false;

    }

    @Nullable
    public String getSetting(final String key) {

        Object value = this.defaultSettings.get(key);

        if (value instanceof String) return (String) value;

        value = FileConfigurationLoader.getInstance().getDefaultSettings().get(key);

        if (value instanceof String) {
            setSetting(key, value);
            return (String) value;
        }

        return null;

    }

    public void setSetting(final String key, final Object value) {

        this.defaultSettings.put(key, value);

    }

    public void setSettings(final Map<String, Object> settings) {

        this.defaultSettings.putAll(settings);

    }

    public void setBoolean(final String key, boolean value) {

        this.defaultSettings.put(key, value);

    }

    public void setInteger(final String key, int value) {

        this.defaultSettings.put(key, value);

    }

    public boolean contains(final String key) {
        return this.defaultSettings.containsKey(key);
    }

}
