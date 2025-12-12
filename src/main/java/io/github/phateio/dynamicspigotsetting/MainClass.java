package io.github.phateio.dynamicspigotsetting;

import io.github.phateio.dynamicspigotsetting.command.MainCommand;
import io.github.phateio.dynamicspigotsetting.text.BasicText;
import io.github.phateio.dynamicspigotsetting.text.TextHelper;
import io.github.phateio.dynamicspigotsetting.utils.ConfigHelper;
import io.github.phateio.dynamicspigotsetting.utils.ServerHelper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

public class MainClass extends JavaPlugin {

    private BukkitTask timer;

    @Override
    public void onEnable() {
        loadText();
        loadConfig();
        loadCommand();
        startTask();
    }

    public void reload() {
        // disable all
        timer.cancel();

        // restart all
        loadText();
        reloadConfig();
        loadConfig();
        startTask();
    }

    @Override
    public void onDisable() {

    }

    private void loadText() {
        File langFile = new File(getDataFolder(), "lang" + File.separator + "message.yml");
        FileConfiguration config = ConfigHelper.loadConfig(langFile);
        TextHelper.init(config);
        ConfigHelper.saveConfig(config, langFile);
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        Setting.loadSetting(config, this);
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void loadCommand() {
        getCommand("DynamicSpigotSetting").setExecutor(new MainCommand(this));
    }

    private void startTask() {
        this.timer = new BukkitRunnable() {

            @Override
            public void run() {
                double tps_now = ServerHelper.getTPS();
                double item = Setting.itemMerge_Radius[0], exp = Setting.expMerge_Radius[0];
                for (int i = 0; i < Setting.itemMerge_TPS.length - 1; i++)
                    if (tps_now > Setting.itemMerge_TPS[i + 1]) {
                        item = Setting.itemMerge_Radius[i];
                        break;
                    }
                for (int i = 0; i < Setting.expMerge_TPS.length - 1; i++)
                    if (tps_now > Setting.expMerge_TPS[i + 1]) {
                        exp = Setting.expMerge_Radius[i];
                        break;
                    }
                ServerHelper.setItemMerge(item);
                if (Setting.debug)
                    getLogger().warning(TextHelper.format(BasicText.TpsNow, tps_now) + ", "
                            + TextHelper.format(BasicText.ItemMergeCommand, item));
                ServerHelper.setExpMerge(exp);
                if (Setting.debug)
                    getLogger().warning(TextHelper.format(BasicText.TpsNow, tps_now) + ", "
                            + TextHelper.format(BasicText.ExpMergeCommand, exp));
            }
        }.runTaskTimer(this, Setting.timerSec * 20L, Setting.timerSec * 20L);

    }

}
