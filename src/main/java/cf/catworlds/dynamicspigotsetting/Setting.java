package cf.catworlds.dynamicspigotsetting;

import cf.catworlds.dynamicspigotsetting.text.BasicText;
import cf.catworlds.dynamicspigotsetting.text.TextHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Setting {
    @SettingInfo(path = "Debug")
    static public boolean debug;
    @SettingInfo(path = "Timer_sec")
    static public int timerSec;
    @SettingInfo(path = "ItemMerge_TPS_Radius")
    static private List<String> itemMergeList;
    static public double[] itemMerge_TPS;
    static public double[] itemMerge_Radius;
    @SettingInfo(path = "ExpMerge_TPS_Radius")
    static private List<String> expMergeList;
    static public double[] expMerge_TPS;
    static public double[] expMerge_Radius;

    static public void loadSetting(FileConfiguration config, CommandSender sender) {
        // set default value
        debug = false;
        timerSec = 30;
        itemMergeList = Arrays.asList("20:0", "15:2", "10:2.5");
        expMergeList = Arrays.asList("20:0", "15:1", "10:3.5");
        // save default then load
        default_load(config, sender);
        // post value
        List<Double> tpsList = new ArrayList<>(), radiusList = new ArrayList<>();
        for (String str : itemMergeList) {
            String[] sp = str.split(":");
            if (sp.length != 2)
                continue;
            tpsList.add(Double.parseDouble(sp[0]));
            radiusList.add(Double.parseDouble(sp[1]));
        }
        tpsList.add(-1.0);
        itemMerge_TPS = tpsList.stream().mapToDouble(d -> d).toArray();
        itemMerge_Radius = radiusList.stream().mapToDouble(d -> d).toArray();

        tpsList.clear();
        radiusList.clear();
        for (String str : expMergeList) {
            String[] sp = str.split(":");
            if (sp.length != 2)
                continue;
            tpsList.add(Double.parseDouble(sp[0]));
            radiusList.add(Double.parseDouble(sp[1]));
        }
        tpsList.add(-1.0);
        expMerge_TPS = tpsList.stream().mapToDouble(d -> d).toArray();
        expMerge_Radius = radiusList.stream().mapToDouble(d -> d).toArray();
    }

    static public void loadSetting(FileConfiguration config, Plugin plugin) {
        loadSetting(config, plugin.getServer().getConsoleSender());
    }

    private static void default_load(FileConfiguration config, CommandSender sender) {
        for (final Field field : Setting.class.getDeclaredFields()) {
            final SettingInfo info = field.getAnnotation(SettingInfo.class);
            if (info == null)
                continue;
            // if not static, do nothing
            if ((field.getModifiers() & Modifier.STATIC) == 0)
                continue;
            field.setAccessible(true);
            String path = info.path();
            config.addDefault(path, getValue(field));
            try {
                field.set(null, config.get(path));
            } catch (IllegalArgumentException e) {
                sender.sendMessage(TextHelper.format(BasicText.ConfigLoadError, path, "" + config.get(path),
                        field.getType().getName(), "" + config.getDefaults().get(path)));
            } catch (Throwable ignore) {
            }
        }
    }

    private static Object getValue(Field field) {
        try {
            return field.get(null);
        } catch (Throwable ignore) {
        }
        // TODO read fail message
        return null;
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SettingInfo {
        String path();
    }
}
