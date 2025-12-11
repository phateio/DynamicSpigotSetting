package cf.catworlds.dynamicspigotsetting.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;

public class ServerHelper {

    static public double getTPS() {
        return Bukkit.getServer().getTPS()[0];
    }

    @SuppressWarnings("deprecation")
    static public double getItemMerge() {
        World world = Bukkit.getWorlds().get(0);
        return ((CraftWorld) world).getHandle().spigotConfig.itemMerge;
    }

    @SuppressWarnings("deprecation")
    static public void setItemMerge(double radius) {
        for (World world : Bukkit.getWorlds()) {
            ((CraftWorld) world).getHandle().spigotConfig.itemMerge = radius;
        }
    }

    @SuppressWarnings("deprecation")
    static public double getExpMerge() {
        World world = Bukkit.getWorlds().get(0);
        return ((CraftWorld) world).getHandle().spigotConfig.expMerge;
    }

    @SuppressWarnings("deprecation")
    static public void setExpMerge(double radius) {
        for (World world : Bukkit.getWorlds()) {
            ((CraftWorld) world).getHandle().spigotConfig.expMerge = radius;
        }
    }

}
