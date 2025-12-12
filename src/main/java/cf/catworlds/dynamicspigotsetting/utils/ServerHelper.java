package cf.catworlds.dynamicspigotsetting.utils;

import net.minecraft.server.MinecraftServer;

public class ServerHelper {

    static public double getTPS() {
        return MinecraftServer.getServer().getTPS()[0];
    }

    static public double getItemMerge() {
        return MinecraftServer.getServer().getAllLevels().iterator().next().spigotConfig.itemMerge;
    }

    static public void setItemMerge(double radius) {
        MinecraftServer.getServer().getAllLevels().forEach((ws) -> ws.spigotConfig.itemMerge = radius);
    }

    static public double getExpMerge() {
        return MinecraftServer.getServer().getAllLevels().iterator().next().spigotConfig.expMerge;
    }

    static public void setExpMerge(double radius) {
        MinecraftServer.getServer().getAllLevels().forEach((ws) -> ws.spigotConfig.expMerge = radius);
    }

}
