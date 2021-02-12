package io.github.thesummergrinch.mcmanhunt.io.settings;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class DefaultSettingsContainer implements ConfigurationSerializable {

    private static volatile DefaultSettingsContainer instance;

    private final Map<String, String> defaultSettings;

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

    public String getSetting(final String key) {

        String value = this.defaultSettings.get(key);

        if (value != null) return value;

        value = FileConfigurationLoader.getInstance().getDefaultSettings().get(key);

        if (value != null) setSetting(key, value);

        return value;

    }

    public void setSetting(final String key, final String value) {

        this.defaultSettings.put(key, value);

    }

    public void setSettings(final Map<String, String> settings) {

        this.defaultSettings.putAll(settings);

    }
}
