package re.imc.geysermodelenginepackgenerator.managers;

import re.imc.geysermodelenginepackgenerator.util.FileConfiguration;

public class ConfigManager {

    private FileConfiguration config;

    public ConfigManager() {
        load();
    }

    public void load() {
        this.config = new FileConfiguration("config.yml");
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
