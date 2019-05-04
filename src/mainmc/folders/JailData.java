package mainmc.folders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;

import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.utils.PluginLoc;

public class JailData extends Config {
	
	private String name;

	public JailData(String name) {
		super(new File(MainPlugin.getInstance().getDataFolder() + "/jails.yml"));
		this.name=name.toLowerCase();
	}
	
	public JailData() {
		super(new File(MainPlugin.getInstance().getDataFolder() + "/jails.yml"));
		this.name=null;
	}

	public void onCreate() {
		File file = new File(MainPlugin.getInstance().getDataFolder() + "/jails.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
				super.get().createSection("Jails");
				super.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setJail(Location loc) {
		super.get().set("Jails." + this.name + ".x", loc.getX());
		super.get().set("Jails." + this.name + ".y", loc.getY());
		super.get().set("Jails." + this.name + ".z", loc.getZ());
		super.get().set("Jails." + this.name + ".yaw", loc.getYaw());
		super.get().set("Jails." + this.name + ".pitch", loc.getPitch());
		super.get().set("Jails." + this.name + ".world", loc.getWorld().getName());
		super.save();
	}
	
	public void delJail() {
		super.get().set("Jails." + this.name, null);
		super.save();
	}

	public Location getLocation() {
		double x = Double.parseDouble(super.getString("Jails." + this.name + ".x"));
		double y = Double.parseDouble(super.getString("Jails." + this.name + ".y"));
		double z = Double.parseDouble(super.getString("Jails." + this.name + ".z"));
		float yaw = Float.parseFloat(super.getString("Jails." + this.name + ".yaw"));
		float pitch = Float.parseFloat(super.getString("Jails." + this.name + ".pitch"));
		World world = PluginLoc.findWorld(super.getString("Jails." + this.name + ".world"));
		return new Location(world, x, y, z, yaw, pitch);
	}

	public List<String> getJails() {
		List<String> jails = new ArrayList<String>();
		Set<String> jaildata = super.getConfiguration("Jails");
		jails.addAll(jaildata);
		return jails;
	}

	public String getName() {
		return this.name;
	}
	
	public boolean exists() {
		return getJails().contains(this.name);
	}

}
