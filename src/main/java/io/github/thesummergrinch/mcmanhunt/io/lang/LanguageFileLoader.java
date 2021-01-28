package io.github.thesummergrinch.mcmanhunt.io.lang;

import io.github.thesummergrinch.mcmanhunt.exceptions.ManHuntStringNotFoundException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public final class LanguageFileLoader {

    private static volatile LanguageFileLoader instance;

    private final Map<String, String> messageStringMap;

    private LanguageFileLoader() {
        this.messageStringMap = new HashMap<>();
    }

    public static LanguageFileLoader getInstance() {
        if (instance == null) {
            synchronized (LanguageFileLoader.class) {
                instance = new LanguageFileLoader();
            }
        }
        return instance;
    }

    public void loadLanguageFile(final URI path, final String langKey) throws IOException, InvalidConfigurationException {
        File directory = new File(path);
        if (!directory.isDirectory()) {
            directory.mkdir();
        }
        File languageFile = new File(directory.getPath() + "/" + langKey + ".yml");
        if (!languageFile.exists()) {
            throw new FileNotFoundException("Could not find specified language file. Please check that the file "
                    + langKey + ".yml exists in the lang directory of MCManHunt.");
        }
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(languageFile);
        HashMap<String, Object> keyValuePairs = (HashMap<String, Object>) yamlConfiguration.getValues(true);
        keyValuePairs.forEach((key, value) -> {
            messageStringMap.put(key, (String) value);
        });
    }

    public String getString(final String key) throws ManHuntStringNotFoundException {
        final String message = messageStringMap.get(key);
        if (message == null) throw new ManHuntStringNotFoundException("Message \"" + key + "\" could not be loaded. " +
                "Please make sure the language file contains this entry, or contact the developer " +
                "(Github.com/TheSummerGrinch)");
        return message;
    }

}
