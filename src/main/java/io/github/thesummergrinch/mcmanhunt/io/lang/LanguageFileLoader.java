package io.github.thesummergrinch.mcmanhunt.io.lang;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.io.settings.DefaultSettingsContainer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public final class LanguageFileLoader {

    private static volatile LanguageFileLoader instance;

    private static final String RESOURCE_BASENAME = "MCManHunt";

    private ResourceBundle resourceBundle;

    private LanguageFileLoader() {
        try {
            loadLanguageFileFromDisk(
                    new File(
                            MCManHunt.getPlugin(MCManHunt.class).getDataFolder().getPath() + File.separator + "lang"
                    ), new Locale(DefaultSettingsContainer.getInstance().getSetting("locale").substring(0,2), DefaultSettingsContainer.getInstance().getSetting("locale").substring(2,4)));
        } catch (MalformedURLException e) {
            MCManHunt.getPlugin(MCManHunt.class).getLogger().warning(e.toString());
        }
    }

    public static LanguageFileLoader getInstance() {
        if (instance == null) {
            synchronized (LanguageFileLoader.class) {
                instance = new LanguageFileLoader();
            }
        }
        return instance;
    }

    private void loadLanguageFileFromDisk(final File directory, final Locale locale) throws MalformedURLException {
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdir();
            writeFilesToDisk(directory, locale);
        }
        File languageFile = new File(directory.getPath() + File.separator + LanguageFileLoader.RESOURCE_BASENAME + "_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties");
        if (languageFile.exists()) {
            URL[] urls = {directory.toURI().toURL()};
            ClassLoader classLoader = new URLClassLoader(urls);
            this.resourceBundle = ResourceBundle.getBundle("MCManHunt", locale, classLoader);
        } else {
            writeFilesToDisk(directory, locale);
        }
    }

    private void writeFilesToDisk(final File directory, final Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle("MCManHunt", locale);
        File file = new File(directory.getPath() + File.separator + LanguageFileLoader.RESOURCE_BASENAME + "_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties");
        try (FileWriter writer = new FileWriter(file)) {
            Set<String> keys = this.resourceBundle.keySet();
            for (String key : keys) {
                writer.write(key + " = " + this.resourceBundle.getString(key) + "\n");
            }
            writer.close();
        } catch (IOException exception) {
            MCManHunt.getPlugin(MCManHunt.class).getLogger().warning(Arrays.toString(exception.getStackTrace()));
        }
    }

    public String getString(final String key) {
        return this.resourceBundle.getString(key);
    }

}
