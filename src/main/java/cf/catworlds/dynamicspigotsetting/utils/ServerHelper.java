package cf.catworlds.dynamicspigotsetting.utils;

public class ServerHelper {

	@SuppressWarnings("deprecation")
	static public double getTPS() {
		return net.minecraft.server.v1_13_R2.MinecraftServer.getServer().recentTps[0];
	}

	@SuppressWarnings("deprecation")
	static public double getItemMerge() {
		return net.minecraft.server.v1_13_R2.MinecraftServer.getServer().getWorlds().iterator().next().spigotConfig.itemMerge;
	}

	@SuppressWarnings("deprecation")
	static public void setItemMerge(double radius) {
		net.minecraft.server.v1_13_R2.MinecraftServer.getServer().getWorlds()
				.forEach((ws) -> ws.spigotConfig.itemMerge = radius);
	}

	@SuppressWarnings("deprecation")
	static public double getExpMerge() {
		return net.minecraft.server.v1_13_R2.MinecraftServer.getServer().getWorlds().iterator().next().spigotConfig.expMerge;
	}

	@SuppressWarnings("deprecation")
	static public void setExpMerge(double radius) {
		net.minecraft.server.v1_13_R2.MinecraftServer.getServer().getWorlds()
				.forEach((ws) -> ws.spigotConfig.expMerge = radius);
	}

}
