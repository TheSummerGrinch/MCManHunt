package io.github.thesummergrinch.mcmanhunt.io.settings;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class DefaultSettingsContainer implements ConfigurationSerializable {

    private static volatile DefaultSettingsContainer instance;

    private final Map<String, Object> defaultSettings;
    private final Map<String, Integer> integerSettings;
    private final Map<String, Boolean> booleanSettings;
    private final Map<String, String> stringSettings;

    private DefaultSettingsContainer() {

        this.defaultSettings = new HashMap<>();
        this.integerSettings = new HashMap<>();
        this.booleanSettings = new HashMap<>();
        this.stringSettings = new HashMap<>();

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



        return settingsMap;

    }

    private boolean isBoolean(final Object possibleBoolean) {

        if (possibleBoolean instanceof Boolean) {
            return true;
        } else if (possibleBoolean instanceof String) {
            return ((String) possibleBoolean).equalsIgnoreCase("true")
                    || ((String) possibleBoolean).equalsIgnoreCase("false");
        }

        return false;

    }

    private boolean isInteger(@NotNull final Object possibleInteger) {
        if (possibleInteger instanceof Integer) {
            return true;
        } else if (possibleInteger instanceof String) {
            int length = ((String) possibleInteger).length();
            if (length == 0) {
                return false;
            }
            int i = 0;
            if (((String) possibleInteger).charAt(0) == '-') {
                if (length == 1) {
                    return false;
                }
                i = 1;
            }
            for (; i < length; i++) {
                char c = ((String) possibleInteger).charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int getInteger(final String key) {

        return this.integerSettings.get(key) != null ?
                this.integerSettings.get(key) : -1;

    }

    public boolean getBoolean(final String key) {

        return this.booleanSettings.get(key) != null ?
                this.booleanSettings.get(key) : false;

    }

    @Nullable
    public String getSetting(final String key) {

        return this.stringSettings.get(key) != null ?
                this.stringSettings.get(key) : "";

    }

    public void setSetting(final String key, final String value) {

        this.stringSettings.put(key, value);

    }

    public void setSettings(final Map<String, Object> settings) {

        settings.forEach((key, value) -> {
            if (value instanceof Boolean) {
                setBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                setInteger(key, (Integer) value);
            } else if (value instanceof String) {
                setSetting(key, (String) value);
            }
        });

    }

    public void setBoolean(final String key, boolean value) {

        this.booleanSettings.put(key, value);

    }

    public void setInteger(final String key, int value) {

        this.integerSettings.put(key, value);

    }

    public boolean contains(final String key) {
        return this.booleanSettings.containsKey(key)
                || this.integerSettings.containsKey(key)
                || this.stringSettings.containsKey(key);
    }

    public Map<String, Object> getAllSettings() {
        final Map<String, Object> allSettings = new HashMap<>();
        allSettings.putAll(this.booleanSettings);
        allSettings.putAll(this.integerSettings);
        allSettings.putAll(this.stringSettings);
        return allSettings;
    }

    public void saveSettings(final Plugin plugin) {

        FileConfiguration fileConfiguration = plugin.getConfig();

        this.stringSettings.forEach(fileConfiguration::set);
        this.integerSettings.forEach(fileConfiguration::set);
        this.booleanSettings.forEach(fileConfiguration::set);

        plugin.saveConfig();

    }

    @Deprecated
    public Map<String, Object> getDefaultSettings() {
        return this.defaultSettings;
    }

}
