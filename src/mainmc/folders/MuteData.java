package mainmc.folders;

import java.io.File;
import java.io.IOException;

import mainmc.nothing00.MainPlugin;

public class MuteData extends Config{
	
	private String user;
	
	public MuteData() {
		super(new File(MainPlugin.getInstance().getDataFolder()+"/data/mutes.yml"));
		this.user=null;
	}
	
	public MuteData(String user) {
		super(new File(MainPlugin.getInstance().getDataFolder()+"/data/mutes.yml"));
		this.user=user;
	}
	
	public void onCreate() {
		if(!super.yml.exists()) {
			try {
				super.yml.createNewFile();
				super.get().createSection("Mutes");
				super.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getCount() {
		if (super.get().get("Mutes." + this.user) != null || super.getStringList("Mutes." + this.user).isEmpty())
			return 0;
		return super.getStringList("Mutes." + this.user).size();
	}

}
