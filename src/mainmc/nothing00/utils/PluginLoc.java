package mainmc.nothing00.utils;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.World;

import mainmc.nothing00.MainPlugin;

public class PluginLoc {

	private Location loc;

	public PluginLoc(Location loc) {
		this.loc = loc;
	}

	public PluginLoc(double x, double y, double z, World world) {
		this.loc = new Location(world, x, y, z);
	}

	public String toString() {
		return this.loc.getX() + "/" + this.loc.getY() + "/" + this.loc.getZ();
	}

	public String toStringComplete() {
		return this.loc.getX() + "/" + this.loc.getY() + "/" + this.loc.getZ() + " (" + this.loc.getYaw() + "/"
				+ this.loc.getPitch() + ")";
	}

	public String getWorld() {
		return this.loc.getWorld().getName();
	}

	public World getServerWorld() {
		return loc.getWorld();
	}

	public Location getLocation() {
		return this.loc;
	}

	@SuppressWarnings("deprecation")
	public Location getTop() {
		int yok = 0;
		int y = (int) this.loc.getY();
		Location loc = new Location(this.loc.getWorld(), this.loc.getX(), this.loc.getY(), this.loc.getZ(),
				this.loc.getYaw(), this.loc.getPitch());
		Location loc2 = new Location(this.loc.getWorld(), this.loc.getX(), this.loc.getY(), this.loc.getZ(),
				this.loc.getYaw(), this.loc.getPitch());
		for (int i = y; i < 256; i++) {
			loc.setY(i);
			loc2.setY(i - 1);
			if (loc.getBlock().getTypeId() == 0 && loc2.getBlock().getTypeId() != 0) {
				yok = i;
			}
		}
		if (yok != 0) {
			loc.setY(yok);
			return loc;
		}
		return this.loc;

	}
	
	public void strike() {
		this.loc.getWorld().strikeLightning(this.loc);
	}

	public static Location getLocFromString(String str) {
		String xyz = str.split(" ")[0];
		double x = Double.parseDouble(xyz.split("/")[0]);
		double y = Double.parseDouble(xyz.split("/")[1]);
		double z = Double.parseDouble(xyz.split("/")[2]);
		String yawpitch = str.split(" ")[1].replace("(", "").replace(")", "");
		float yaw = Float.parseFloat(yawpitch.split("/")[0]);
		float pitch = Float.parseFloat(yawpitch.split("/")[1]);
		World world = findWorld(str.split(" ")[2]);
		return new Location(world, x, y, z, yaw, pitch);
	}

	public static World findWorld(String string) {
		Iterator<World> worlds = MainPlugin.getInstance().getServer().getWorlds().iterator();
		while (worlds.hasNext()) {
			World world = worlds.next();
			if (world.getName().equalsIgnoreCase(string)) {
				return world;
			}
		}
		return null;
	}

}
