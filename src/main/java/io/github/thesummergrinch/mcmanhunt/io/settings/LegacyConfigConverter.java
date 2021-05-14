package io.github.thesummergrinch.mcmanhunt.io.settings;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;
import java.util.Scanner;

public class LegacyConfigConverter {

    private static volatile LegacyConfigConverter instance;

    private LegacyConfigConverter() {}

    public static LegacyConfigConverter getInstance() {
        if (instance != null) return instance;
        synchronized (LegacyConfigConverter.class) {
            if (instance == null) instance = new LegacyConfigConverter();
        }
        return instance;
    }

    @Deprecated
    public void convertLegacyConfig(final DefaultSettingsContainer legacySettingsContainer) {

        Map<String, Object> legacySettings =
                legacySettingsContainer.getAllSettings();
        legacySettings.forEach((key, value) -> {
            final String valueString = (String) value;
            if (isBoolean(valueString)) {
                DefaultSettingsContainer.getInstance().setBoolean(key,
                        Boolean.parseBoolean(valueString));
            } else if (isInteger(valueString)) {
                DefaultSettingsContainer.getInstance().setInteger(key,
                        Integer.parseInt(valueString));
            }
        });

        FileConfigurationLoader.getInstance().getDefaultSettings().forEach((key, value) -> {
            if (!DefaultSettingsContainer.getInstance().contains(key))
                if (value instanceof Boolean) {
                    DefaultSettingsContainer.getInstance().setBoolean(key,
                            (Boolean) value);
                } else if (value instanceof Integer) {
                    DefaultSettingsContainer.getInstance().setInteger(key,
                            (Integer) value);
                } else if (value instanceof String) {
                    DefaultSettingsContainer.getInstance().setSetting(key,
                            (String) value);
                }
        });

        legacySettings.remove("settings");
        legacySettings.remove("game-cache");

    }

    public DefaultSettingsContainer convertLegacyConfig(final FileConfiguration fileConfiguration) {

        DefaultSettingsContainer settingsContainer =
                fileConfiguration.getObject("settings",
                        DefaultSettingsContainer.class);

        Map<String, Object> settings = settingsContainer.getDefaultSettings();

        settings.forEach((key, value) -> {
            if (value instanceof Boolean) {
                settingsContainer.setBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                settingsContainer.setInteger(key, (Integer) value);
            } else if (value instanceof String) {
                settingsContainer.setSetting(key, (String) value);
            }
        });

        return DefaultSettingsContainer.getInstance();

    }

    private boolean isBoolean(final String possibleBoolean) {
        return possibleBoolean.equalsIgnoreCase("true")
                || possibleBoolean.equalsIgnoreCase("false");
    }

    private boolean isInteger(final String possibleInteger) {
        Scanner sc = new Scanner(possibleInteger.trim());
        if(!sc.hasNextInt()) return false;
        sc.nextInt();
        return !sc.hasNext();
    }

}
