package mainmc.folders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mainmc.nothing00.MainPlugin;

public class UserData extends Config {

	private String user;

	public UserData(String user) {
		super(new File(MainPlugin.getInstance().getDataFolder() + "/data/users/" + user + ".yml"));
		this.user = user;
	}

	public String getCurrentHost() {
		return super.getString("userdata.IP");
	}
	
	public String getCurrentIp() {
		return super.getString("userdata.IP").split(":")[0];
	}

	public String getFirstJoin() {
		return super.getString("userdata.firstlogin");
	}

	public boolean isCorrectCase() {
		return this.user.equals(super.getString("userdata.username"));
	}
	
	public String getOriginalName() {
		return super.getString("userdata.username");
	}
	
	public String getUUID() {
		return super.getString("userdata.UUID");
	}

	public boolean fileExists() {
		File users = new File(MainPlugin.getInstance().getDataFolder() + "/data/users/");
		File[] files = users.listFiles();
		int i = 0;
		boolean t = false;
		while (i < files.length && !t) {
			if (files[i].getName().equalsIgnoreCase(this.user + ".yml")) {
				t = true;
			} else {
				i++;
			}
		}
		return t;
	}

	public List<String> getOldAdresses() {
		return super.getStringList("userdata.others-ips");
	}
	
	public void resetHost() {
		super.get().set("userdata.IP", "0.0.0.0");
		super.get().set("userdata.others-ips", new ArrayList<String>());
		super.save();
	}

	public void onChangeIp(String host) {
		String current = this.getCurrentHost();
		super.get().set("userdata.IP", host);
		super.save();
		List<String> list = super.getStringList("userdata.others-ips");
		if (!current.equals("0.0.0.0"))
			list.add(current);
		super.get().set("userdata.others-ips", list);
		super.save();
	}

	public boolean changedHost(String host) {
		return !(host.equals(this.getCurrentHost().split(":")[0]));
	}

	public boolean exists() {
		return super.yml.exists();
	}

	public boolean hasIgnoredUser() {
		return super.getStringList("userdata.ignores") != null;
	}

	public List<String> getIgnores() {
		if (hasIgnoredUser())
			return super.getStringList("userdata.ignores");
		return new ArrayList<String>();
	}

	public void addIgnored(String user) {
		List<String> list = new ArrayList<String>();
		if (hasIgnoredUser())
			list = super.getStringList("userdata.ignores");
		list.add(user);
		super.get().set("userdata.ignores", list);
		super.save();
	}

	public void removeIgnored(String user) {
		List<String> list = new ArrayList<String>();
		if (!hasIgnoredUser())
			return;
		list = super.getStringList("userdata.ignores");
		list.remove(user);
		super.get().set("userdata.ignores", list);
		super.save();
	}

	public String getQuitLocation() {
		return super.getString("userdata.lastlocation");
	}

}
