package mainmc.folders;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.World;

import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.utils.PluginLoc;

public class WarpData extends Config {

	public WarpData() {
		super(new File(MainPlugin.getInstance().getDataFolder() + "/warps.yml"));
	}

	public void onCreate() {
		File file = new File(MainPlugin.getInstance().getDataFolder() + "/warps.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
				super.get().createSection("Warps");
				super.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setWarp(String name, Location loc) {
		super.get().set("Warps." + name + ".x", loc.getX());
		super.get().set("Warps." + name + ".y", loc.getY());
		super.get().set("Warps." + name + ".z", loc.getZ());
		super.get().set("Warps." + name + ".yaw", loc.getYaw());
		super.get().set("Warps." + name + ".pitch", loc.getPitch());
		super.get().set("Warps." + name + ".world", loc.getWorld().getName());
		super.get().set("Warps." + name + ".hidden", false);
		super.save();
	}

	public boolean isHidden(String name) {
		return Boolean.parseBoolean(super.getString("Warps." + name + ".hidden"));
	}

	public void setHidden(String name, boolean hidden) {
		super.get().set("Warps." + name + ".hidden", hidden);
		super.save();
	}

	public boolean hasIcon(String name) {
		return super.getString("Warps." + name + ".icon") != null;
	}

	public String getIcon(String name) {
		return super.getString("Warps." + name + ".icon");
	}

	public void setIcon(String name, String icon) {
		super.get().set("Warps." + name + ".icon", icon);
		super.save();
	}
	
	public Location getWarp(String name) {
		double x = Double.parseDouble(super.getString("Warps." + name + ".x"));
		double y = Double.parseDouble(super.getString("Warps." + name + ".y"));
		double z = Double.parseDouble(super.getString("Warps." + name + ".z"));
		float yaw = Float.parseFloat(super.getString("Warps." + name + ".yaw"));
		float pitch = Float.parseFloat(super.getString("Warps." + name + ".pitch"));
		World world = PluginLoc.findWorld(super.getString("Warps." + name + ".world"));
		return new Location(world, x, y, z, yaw, pitch);
	}

}
