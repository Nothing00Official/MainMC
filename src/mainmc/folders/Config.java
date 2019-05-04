package mainmc.folders;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import mainmc.nothing00.MainPlugin;

public class Config {

	private FileConfiguration config;
	protected File yml;
	
	public Config(FileConfiguration config, File yml) {
		this.config = config;
		this.yml = yml;
		
		if(this.config!=Bukkit.getPluginManager().getPlugin("MainMC").getConfig()) {
			this.config=YamlConfiguration.loadConfiguration(this.yml);
		}
	}
	
	public File getFile() {
		return this.yml;
	}
	
	public Config(File yml) {
		this.yml = yml;
	    this.config=YamlConfiguration.loadConfiguration(this.yml);
	}
	
	public void set(String index, String value) {
		config.set(index, value);
	}
	
	public void set(String index, List<String> value) {
		config.set(index, value);
	}
	
	public FileConfiguration get() {
		return this.config;
	}
	
	public String getString(String index) {
		return this.config.getString(index);
	}
	
	public List<String> getStringList(String index) {
		return this.config.getStringList(index);
	}
	
	public Set<String> getConfiguration(String index) {
		return this.config.getConfigurationSection(index).getKeys(false);
	}
	
	public void save() {
		if(this.config==Bukkit.getPluginManager().getPlugin("MainMC").getConfig()) {
			Bukkit.getPluginManager().getPlugin("MainMC").saveConfig();
		}else {
			try {
				this.config.save(this.yml);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void reload() {
		if(this.config==Bukkit.getPluginManager().getPlugin("MainMC").getConfig()) {
			Bukkit.getPluginManager().getPlugin("MainMC").reloadConfig();
		}
	}
	
	public static void folders() {
		File file = new File(MainPlugin.getInstance().getDataFolder()+"/data/");
		File file2 = new File(MainPlugin.getInstance().getDataFolder()+"/data/users");
		if(!file.exists()) {
			file.mkdir();
		}
		if(!file2.exists()) {
			file2.mkdir();
		}
	}
	
	public static void setDatabase() {
		Conf config = new Conf();
		File file = new File(MainPlugin.getInstance().getDataFolder()+"/data/"+config.getDBName());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void delete() {
		this.yml.delete();
	}
}
