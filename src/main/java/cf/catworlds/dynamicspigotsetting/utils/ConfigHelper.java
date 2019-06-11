package cf.catworlds.dynamicspigotsetting.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigHelper {

    static public FileConfiguration loadConfig(File file) {
        if (!file.exists())
            return new YamlConfiguration();
        return YamlConfiguration.loadConfiguration(file);
    }

    static public boolean saveConfig(FileConfiguration config, File file) {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            config.options().copyDefaults(true);
            config.save(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
