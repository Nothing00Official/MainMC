package mainmc.folders;

import java.io.File;
import java.io.IOException;

import mainmc.nothing00.MainPlugin;

public class BanData extends Config{
	
	private String user;
	
	public BanData() {
		super(new File(MainPlugin.getInstance().getDataFolder()+"/data/bans.yml"));
		this.user=null;
	}
	
	public BanData(String user) {
		super(new File(MainPlugin.getInstance().getDataFolder()+"/data/bans.yml"));
		this.user=user;
	}
	
	public void onCreate() {
		if(!super.yml.exists()) {
			try {
				super.yml.createNewFile();
				super.get().createSection("Bans");
				super.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getCount() {
		if (super.get().get("Bans." + this.user) != null || super.getStringList("Bans." + this.user).isEmpty())
			return 0;
		return super.getStringList("Bans." + this.user).size();
	}

}
