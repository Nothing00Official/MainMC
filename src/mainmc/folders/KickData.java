package mainmc.folders;

import java.io.File;
import java.io.IOException;

import mainmc.nothing00.MainPlugin;

public class KickData extends Config {

	private String user;

	public KickData() {
		super(new File(MainPlugin.getInstance().getDataFolder() + "/data/kicks.yml"));
		this.user = null;
	}

	public KickData(String user) {
		super(new File(MainPlugin.getInstance().getDataFolder() + "/data/kicks.yml"));
		this.user = user;
	}

	public void onCreate() {
		if (!super.yml.exists()) {
			try {
				super.yml.createNewFile();
				super.get().createSection("Kicks");
				super.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getCount() {
		if (super.get().get("Kicks." + this.user) != null || super.getStringList("Kicks." + this.user).isEmpty())
			return 0;
		return super.getStringList("Kicks." + this.user).size();
	}

}
