package au.com.fourseasonsgaming.fourseasonsgamingbot.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tazzie02.tazbotdiscordlib.util.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigFileHandler {

    private static ConfigFileHandler instance;

    private final Gson GSON;
    private final String CONFIG_FILE_NAME;

    private final Path configFile;
    private Config config;

    {
        GSON = new GsonBuilder().setPrettyPrinting().create();
        CONFIG_FILE_NAME = "4sg-config.json";
    }

    private ConfigFileHandler() {
        Path path = Paths.get(System.getProperty("user.dir"));
        this.configFile = path.resolve(CONFIG_FILE_NAME);
        loadConfig();
    }

    public static ConfigFileHandler getInstance() {
        if (instance == null) {
            instance = new ConfigFileHandler();
        }
        return instance;
    }

    private boolean loadConfig() {
        if (!Files.exists(configFile)) {
            saveConfig();
        }
        else {
            try (BufferedReader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8)) {
                this.config = GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                System.out.println("Error loading configuration.");
                e.printStackTrace();
                return false;
            }
        }
        System.out.println("Configuration loaded.");
        return true;
    }

    private boolean saveConfig() {
        if (this.config == null) {
            this.config = defaultConfig();
        }

        String json = GSON.toJson(this.config);
        try {
            if (!Files.exists(this.configFile)) {
                FileUtil.createFileAndPath(this.configFile);
            }
            FileUtil.writeToFile(json, this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private Config defaultConfig() {
        this.config = Config.defaultFactory();
        return this.config;
    }

    public Config getConfig() {
        return config;
    }

}
