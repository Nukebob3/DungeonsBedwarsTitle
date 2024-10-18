package net.nukebob.config.dbtitles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.nukebob.DungeonsBedwars;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DungeonsBedwarsTitlesConfig {
    public boolean mod_enabled = true;
    public boolean victory_enabled = true;
    public boolean death_enabled = true;
    public boolean bed_enabled = true;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/dungeonsBedwarsTitles.json");
    private static DungeonsBedwarsTitlesConfig config;

    public static DungeonsBedwarsTitlesConfig loadConfig() {
        if (!CONFIG_FILE.exists()) {
            config = new DungeonsBedwarsTitlesConfig();
            saveConfig();
        } else {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                config = GSON.fromJson(reader, DungeonsBedwarsTitlesConfig.class);
            } catch (IOException e) {
                DungeonsBedwars.LOGGER.error("Could not load config file", e);
            }
        }
        return config;
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            DungeonsBedwars.LOGGER.error("Could not save config file", e);
        }
    }
}
