package io.github.thesummergrinch.mcmanhunt.io.lang;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public final class LanguageFileLoader {

    private static volatile LanguageFileLoader instance;

    private static final String RESOURCE_BASENAME = "MCManHunt";

    private final FileConfiguration fileConfiguration;
    private ResourceBundle resourceBundle;
    private Locale locale;

    private LanguageFileLoader() {

        this.fileConfiguration = MCManHunt.getPlugin(MCManHunt.class).getFileConfiguration();

        this.locale = new Locale(this.fileConfiguration.getString("locale").substring(0,2), this.fileConfiguration.getString("locale").substring(2,4));

        try {

            loadLanguageFileFromDisk(
                    new File(
                            MCManHunt.getPlugin(MCManHunt.class).getDataFolder().getPath() + File.separator + "lang"
                    ), this.locale);

        } catch (IOException e) {

            MCManHunt.getPlugin(MCManHunt.class).getLogger().warning(e.getMessage() + " The Language-file will be created next time the plugin is loaded.");

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

    private void loadLanguageFileFromDisk(final File directory, final Locale locale) throws IOException {

        if (!directory.exists() || !directory.isDirectory()) {

            directory.mkdir();
            writeFilesToDisk(directory, locale);

        }

        File languageFile = new File(directory.getPath() + File.separator + LanguageFileLoader.RESOURCE_BASENAME + "_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties");

        if (languageFile.exists()) {

            languageFile.createNewFile();

            URL[] urls = {directory.toURI().toURL()};
            ClassLoader classLoader = new URLClassLoader(urls);
            this.resourceBundle = ResourceBundle.getBundle("MCManHunt", locale, classLoader);

            try {

                if (!resourceBundle.containsKey("translation-version") || !resourceBundle.getString("translation-version").equals(ResourceBundle.getBundle("MCManHunt", locale).getString("translation-version"))) {

                    writeFilesToDisk(directory, locale);

                }

            } catch (NullPointerException exception) {

                writeFilesToDisk(directory, locale);

            }

        } else {

            writeFilesToDisk(directory, locale);

        }
    }

    private void writeFilesToDisk(final File directory, final Locale locale) {

        ResourceBundle.clearCache();

        this.resourceBundle = ResourceBundle.getBundle("MCManHunt", locale, this.getClass().getClassLoader());
        File file = new File(directory.getPath() + File.separator + LanguageFileLoader.RESOURCE_BASENAME + "_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties");

        try {

            FileWriter writer = new FileWriter(file);
            Set<String> keys = this.resourceBundle.keySet();

            for (String key : keys) {

                writer.write(key + " = " + this.resourceBundle.getString(key) + "\n");

            }

            writer.close();

        } catch (IOException exception) {

            exception.printStackTrace();

        }
    }

    public String getString(final String key) {

        if (this.resourceBundle.containsKey(key)) {

            return this.resourceBundle.getString(key);

        } else {

            writeFilesToDisk(MCManHunt.getPlugin(MCManHunt.class).getDataFolder(), this.locale);

        }

        return this.resourceBundle.getString(key);

    }

    public void loadNewLanguage(final File directory, final Locale locale) throws IOException {

        loadLanguageFileFromDisk(directory, locale);

    }

}
