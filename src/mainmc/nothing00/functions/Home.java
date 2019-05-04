package mainmc.nothing00.functions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import mainmc.folders.UserData;
import mainmc.nothing00.utils.PluginLoc;

public class Home extends UserData {

	public Home(String user) {
		super(user);
	}

	public boolean hasHomes() {
		return super.get().get("userdata.homes") != null && !super.getStringList("userdata.homes").isEmpty();
	}

	public List<String> getHomes() {
		if (this.hasHomes())
			return super.getStringList("userdata.homes");
		return new ArrayList<String>();
	}

	public Location getHome(String name) {
		name=name.toLowerCase();
		String locstr = "";
		for (String home : getHomes()) {
			if (name.equalsIgnoreCase(home.split(" ")[0].replace(")", ""))) {
				locstr = home.substring(home.split(" ")[0].length() + 1, home.length());
			}
		}
		return PluginLoc.getLocFromString(locstr);
	}

	public void setHome(Location loc) {
		List<String> homes = new ArrayList<String>();
		if (this.hasHomes())
			homes = getHomes();
		int n = getHomes().size() + 1;
		PluginLoc pl = new PluginLoc(loc);
		homes.add(n + ") " + pl.toStringComplete() + " " + pl.getWorld());
		super.get().set("userdata.homes", homes);
		super.save();
	}

	public void setHome(Location loc, String name) {
		name=name.toLowerCase();
		List<String> homes = new ArrayList<String>();
		if (this.hasHomes())
			homes = getHomes();
		PluginLoc pl = new PluginLoc(loc);
		homes.add(name + ") " + pl.toStringComplete() + " " + pl.getWorld());
		super.get().set("userdata.homes", homes);
		super.save();
	}

	public List<String> getHomeNames() {
		List<String> homes = new ArrayList<String>();
		for (String home : getHomes()) {
			homes.add(home.split(" ")[0].replace(")", ""));
		}
		return homes;
	}

	public void deleteHome(String home) {
		home=home.toLowerCase();
		if (getHomes().isEmpty())
			return;
		if(getHomeNames().contains(home)) {
			List<String> list = super.getStringList("userdata.homes");
			for(String h : getHomes()) {
				if(h.contains(home+")")) {
					list.remove(h);
				}
			}
			super.get().set("userdata.homes", list);
			super.save();
		}
	}

}
