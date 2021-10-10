package mainmc.folders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import mainmc.nothing00.MainPlugin;

public class Help {

	private File page;

	public Help() {
		this.page = new File(MainPlugin.getInstance().getDataFolder() + "/help/1.txt");
	}

	public Help(String page) {
		this.page = new File(MainPlugin.getInstance().getDataFolder() + "/help/" + page + ".txt");
	}

	public boolean isNull() {
		return !this.page.exists();
	}

	public String showPage() {
		List<String> words = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(this.page.getAbsolutePath()));
			String line;
			while ((line = reader.readLine()) != null) {
				words.add(line.replaceAll("&", "§"));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return String.join("\n", words.toArray(new String[0]));
	}

	public static void onCreate() {
		File dir = new File(MainPlugin.getInstance().getDataFolder() + "/help/");
		if (!dir.exists()) {
			dir.mkdir();
		}
		File defaulthelp = new File(MainPlugin.getInstance().getDataFolder() + "/help/1.txt");
		if (!defaulthelp.exists()) {
			try {
				defaulthelp.createNewFile();
				PrintWriter writer = new PrintWriter(defaulthelp.getAbsolutePath(), "UTF-8");
				writer.println("&6|--------------------INFO COMMANDS--------------------|");
				writer.println("&1TPA: &6/tpa <player>. Asks the player if you can teleport to them.");
				writer.println("&1SPAWN: &6/spawn. Teleports you to spawn.");
				writer.println("&1WARP: &6/warp <warp>. Teleport you in a location. ");
				writer.println("&1PAY: &6/pay <player> <ammount>. Pays a specified player.");
				writer.println("&1AFK: &6/afk. Set your status as AFK");
				writer.println("&&1HELPSTAFF: &6/helpstaff <message>. Request help from online staff.");
				writer.println("&1IGNORE: &6/ignore <player>. Ignore a player");
				writer.println("&1MAIL: &6/mail <read/clear/send>. To read your emails");
				writer.println("&1MSG: &6/msg <player> <message>. Send a private message");
				writer.println("&1SUICIDE: &6/suicide. Commit suicide.");
				writer.println("&cput here other commands to your custom help or create new file pages");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
