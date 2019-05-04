package mainmc.folders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import mainmc.nothing00.MainPlugin;

public class Motd {

	private File motd;

	public Motd() {
		this.motd = new File(MainPlugin.getInstance().getDataFolder() + "/motd.txt");
		if (!this.motd.exists()) {
			try {
				this.motd.createNewFile();
				PrintWriter writer = new PrintWriter(this.motd.getAbsolutePath(), "UTF-8");
				writer.println("&7Welcome to the server &e{player}");
				writer.println("&7Type &e/help &7for help list command");
				writer.println("&7Type &e/list &7for list of all online players");
				writer.println("&7Today is &e{day}/{month}/{year} &7and it is &e{time}");
				writer.println("&7In this server there are &e{onlineplayers} &7players online:");
				writer.println("&7{listallplayers}");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public File getMotd() {
		return this.motd;
	}

	public String showMotd() {
		List<String> words = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.motd.getAbsolutePath()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				words.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String motd = "";
		for (String line : words) {
			motd += line + "\n";
		}
		return motd;

	}

}
