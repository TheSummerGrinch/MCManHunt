package io.github.thesummergrinch.mcmanhunt.io.lang;

import io.github.thesummergrinch.mcmanhunt.MCManHunt;
import io.github.thesummergrinch.mcmanhunt.io.settings.DefaultSettingsContainer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class LanguageFileLoader {

    private static volatile LanguageFileLoader instance;

    private YamlConfiguration languageFileYamlConfiguration;

    private LanguageFileLoader() {
        try {
            loadLanguageFile(DefaultSettingsContainer.getInstance().getSetting("locale"));
        } catch (IOException | InvalidConfigurationException | URISyntaxException exception) {
            exception.printStackTrace();
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

    private void copyLanguageFilesToFilesystem(String destination) throws IOException, URISyntaxException {
        List<Path> paths = getPathsFromResourceJAR("lang");
        paths.forEach(path -> {
            try {
                String[] pathParts = path.toAbsolutePath().toString().split(File.separator);
                int index = pathParts.length - 1;
                Files.copy(path, Paths.get(destination, pathParts[index]), StandardCopyOption.COPY_ATTRIBUTES);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void loadLanguageFile(final String localeString) throws IOException, URISyntaxException, InvalidConfigurationException {

        File langDirectory = new File(MCManHunt.getPlugin(MCManHunt.class).getDataFolder().getPath() + File.separator + "lang");

        if (!langDirectory.exists() || !langDirectory.isDirectory()) {
            langDirectory.mkdir();
            copyLanguageFilesToFilesystem(langDirectory.getPath());
        }

        this.languageFileYamlConfiguration = YamlConfiguration.loadConfiguration(
                new File(langDirectory.getPath() + File.separator + localeString + ".yml"));

    }

    private InputStream getFileFromResourceAsStream(final String fileName) {

        final ClassLoader classLoader = getClass().getClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) throw new IllegalArgumentException("File not found! " + fileName);

        return inputStream;

    }

    private List<Path> getPathsFromResourceJAR(final String folder) throws URISyntaxException, IOException {

        List<Path> result;

        String jarPath = getClass()
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath();

        URI uri = URI.create("jar:file:" + new File(jarPath).toURI().toString());

        try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            result = Files.walk(fs.getPath(folder))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }

        return result;

    }

    private File getFileFromPath(final Path filePath) {
        return filePath.toFile();
    }

    public String getString(final String key) {
        return this.languageFileYamlConfiguration.getString(key);
    }

    private Reader getReaderOfResource(final String resourceName) {
        return new InputStreamReader(getClass().getClassLoader().getResourceAsStream(resourceName));
    }

}
