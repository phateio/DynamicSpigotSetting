package cf.catworlds.dynamicspigotsetting.utils;

import net.minecraft.server.v1_16_R1.MinecraftServer;

public class ServerHelper {

    @SuppressWarnings("deprecation")
    static public double getTPS() {
        return MinecraftServer.getServer().recentTps[0];
    }

    @SuppressWarnings("deprecation")
    static public double getItemMerge() {
        return MinecraftServer.getServer().getWorlds().iterator().next().spigotConfig.itemMerge;
    }

    @SuppressWarnings("deprecation")
    static public void setItemMerge(double radius) {
        MinecraftServer.getServer().getWorlds()
                .forEach((ws) -> ws.spigotConfig.itemMerge = radius);
    }

    @SuppressWarnings("deprecation")
    static public double getExpMerge() {
        return MinecraftServer.getServer().getWorlds().iterator().next().spigotConfig.expMerge;
    }

    @SuppressWarnings("deprecation")
    static public void setExpMerge(double radius) {
        MinecraftServer.getServer().getWorlds()
                .forEach((ws) -> ws.spigotConfig.expMerge = radius);
    }

}
