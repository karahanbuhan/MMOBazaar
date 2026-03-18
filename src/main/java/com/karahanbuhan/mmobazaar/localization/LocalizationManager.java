package com.karahanbuhan.mmobazaar.localization;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class LocalizationManager {
    private final FileConfiguration translations;

    public LocalizationManager(JavaPlugin plugin, String locale) {
        File langFile = new File(plugin.getDataFolder(), "lang/" + locale + ".yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang/" + locale + ".yml", false);
        }
        translations = YamlConfiguration.loadConfiguration(langFile);

        // Load default translations from JAR in case of missing/corrupted language file
        FileConfiguration defaultConfig = loadFromJar(plugin, locale);
        translations.setDefaults(defaultConfig);
        translations.options().copyDefaults(true);
    }

    public String get(TranslationKey key, Object... args) {
        String message = translations.getString(key.getPath(), "§cMissing translation key: " + key.name());
        return String.format(message, args);
    }

    private FileConfiguration loadFromJar(JavaPlugin plugin, String locale) {
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(plugin.getResource("lang/" + locale + ".yml")))) {
            return YamlConfiguration.loadConfiguration(reader);
        } catch (Exception e) {
            plugin.getLogger().warning("Could not load default translation file: falling back to en_US.yml");
            // fallback to en_US.yml inside JAR
            try {
                Reader fallbackReader = new InputStreamReader(Objects.requireNonNull(plugin.getResource("lang/en_US.yml")));
                return YamlConfiguration.loadConfiguration(fallbackReader);
            } catch (Exception fallbackError) {
                plugin.getLogger().severe("FATAL: Could not load fallback en_US.yml from JAR!");
                plugin.getLogger().severe("Disabling plugin due to fatal localization failure.");
                Bukkit.getPluginManager().disablePlugin(plugin);

                throw new IllegalStateException("Failed to load fallback locale en_US.yml", fallbackError);
            }
        }
    }
}
