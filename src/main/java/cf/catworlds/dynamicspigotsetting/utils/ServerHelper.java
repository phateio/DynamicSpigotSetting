package cf.catworlds.dynamicspigotsetting.utils;

public class ServerHelper {

	@SuppressWarnings("deprecation")
	static public double getTPS() {
		return net.minecraft.server.v1_12_R1.MinecraftServer.getServer().recentTps[0];
	}

	@SuppressWarnings("deprecation")
	static public double getItemMerge() {
		return net.minecraft.server.v1_12_R1.MinecraftServer.getServer().worlds.get(0).spigotConfig.itemMerge;
	}

	@SuppressWarnings("deprecation")
	static public void setItemMerge(double radius) {
		net.minecraft.server.v1_12_R1.MinecraftServer.getServer().worlds
				.forEach((ws) -> ws.spigotConfig.itemMerge = radius);
	}

	@SuppressWarnings("deprecation")
	static public double getExpMerge() {
		return net.minecraft.server.v1_12_R1.MinecraftServer.getServer().worlds.get(0).spigotConfig.expMerge;
	}

	@SuppressWarnings("deprecation")
	static public void setExpMerge(double radius) {
		net.minecraft.server.v1_12_R1.MinecraftServer.getServer().worlds
				.forEach((ws) -> ws.spigotConfig.expMerge = radius);
	}

}
