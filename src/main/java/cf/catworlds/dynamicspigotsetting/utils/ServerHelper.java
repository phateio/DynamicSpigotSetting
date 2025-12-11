package cf.catworlds.dynamicspigotsetting.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ServerHelper {

    static public double getTPS() {
        return Bukkit.getServer().getTPS()[0];
    }

    static public double getItemMerge() {
        try {
            World world = Bukkit.getWorlds().get(0);
            Object handle = getHandle(world);
            Object spigotConfig = getSpigotConfig(handle);
            Field itemMergeField = spigotConfig.getClass().getField("itemMerge");
            return itemMergeField.getDouble(spigotConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    static public void setItemMerge(double radius) {
        try {
            for (World world : Bukkit.getWorlds()) {
                Object handle = getHandle(world);
                Object spigotConfig = getSpigotConfig(handle);
                Field itemMergeField = spigotConfig.getClass().getField("itemMerge");
                itemMergeField.setDouble(spigotConfig, radius);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public double getExpMerge() {
        try {
            World world = Bukkit.getWorlds().get(0);
            Object handle = getHandle(world);
            Object spigotConfig = getSpigotConfig(handle);
            Field expMergeField = spigotConfig.getClass().getField("expMerge");
            return expMergeField.getDouble(spigotConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    static public void setExpMerge(double radius) {
        try {
            for (World world : Bukkit.getWorlds()) {
                Object handle = getHandle(world);
                Object spigotConfig = getSpigotConfig(handle);
                Field expMergeField = spigotConfig.getClass().getField("expMerge");
                expMergeField.setDouble(spigotConfig, radius);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getHandle(World world) throws Exception {
        Method getHandle = world.getClass().getMethod("getHandle");
        return getHandle.invoke(world);
    }

    private static Object getSpigotConfig(Object worldServer) throws Exception {
        Field spigotConfigField = worldServer.getClass().getField("spigotConfig");
        return spigotConfigField.get(worldServer);
    }

}
