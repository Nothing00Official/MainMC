package mainmc.folders;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.World;

import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.utils.PluginLoc;

public class SpawnData extends Config {

	public SpawnData() {
		super(new File(MainPlugin.getInstance().getDataFolder() + "/spawn.yml"));
	}

	public void onCreate() {
		File file = new File(MainPlugin.getInstance().getDataFolder() + "/spawn.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setSpawn(Location loc) {
		super.get().set("Spawn.x", loc.getX());
		super.get().set("Spawn.y", loc.getY());
		super.get().set("Spawn.z", loc.getZ());
		super.get().set("Spawn.yaw", loc.getYaw());
		super.get().set("Spawn.pitch", loc.getPitch());
		super.get().set("Spawn.world", loc.getWorld().getName());
		super.save();
	}

	public Location getSpawn() {
		double x = Double.parseDouble(super.getString("Spawn.x"));
		double y = Double.parseDouble(super.getString("Spawn.y"));
		double z = Double.parseDouble(super.getString("Spawn.z"));
		float yaw = Float.parseFloat(super.getString("Spawn.yaw"));
		float pitch = Float.parseFloat(super.getString("Spawn.pitch"));
		World world = PluginLoc.findWorld(super.getString("Spawn.world"));
		return new Location(world, x, y, z, yaw, pitch);
	}

	public boolean thereIsSpawn() {
		return super.getString("Spawn") != null;
	}

}
