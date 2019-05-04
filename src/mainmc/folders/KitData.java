package mainmc.folders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.functions.Book;
import mainmc.nothing00.functions.Kit;


public class KitData extends Config {

	private String kit;

	public KitData() {
		super(new File(MainPlugin.getInstance().getDataFolder() + "/kits.yml"));
	}

	public KitData(String kit) {
		super(new File(MainPlugin.getInstance().getDataFolder() + "/kits.yml"));
		this.kit = kit.toLowerCase();
	}

	public void onCreate() {
		File file = new File(MainPlugin.getInstance().getDataFolder() + "/kits.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
				super.get().createSection("Kits");
				FileConfiguration kit = super.get();
				kit.set("Kits.test.delay", 86400);
				kit.set("Kits.test.helmet", "diamond_helmet protection:1 name:&7Pro&6Helmet");
				kit.set("Kits.test.chestplate", "diamond_helmet protection:1 name:&7Pro&6Chestplate");
				kit.set("Kits.test.leggings", "diamond_leggings protection:1 name:&7Pro&6Leggings");
				kit.set("Kits.test.boots", "diamond_boots protection:1 name:&7Pro&6Boots");
				kit.set("Kits.test.inventory", "");
				List<String> invlist = kit.getStringList("Kits.test.inventory");
				invlist.add("diamond_sword 1 sharpness:3 name:&7Pro&6Sword");
				invlist.add("golden_apple:1 32");
				kit.set("Kits.test.inventory", invlist);

				kit.set("Kits.tools.delay", 0);
				kit.set("Kits.tools.helmet", "");
				kit.set("Kits.tools.chestplate", "");
				kit.set("Kits.tools.leggings", "");
				kit.set("Kits.tools.boots", "");
				kit.set("Kits.tools.inventory", "");
				invlist = kit.getStringList("Kits.tools.inventory");
				invlist.add("stone_sword 1 durability:1");
				invlist.add("stone_pickaxe 1");
				invlist.add("stone_axe 1");
				invlist.add("stone_shovel 1");
				invlist.add("bread 8");
				kit.set("Kits.tools.inventory", invlist);

				kit.set("Kits.nothing00.delay", 30);
				kit.set("Kits.nothing00.helmet", "skull owner:Nothing00_");
				kit.set("Kits.nothing00.chestplate", "leather_chestplate durability:1");
				kit.set("Kits.nothing00.leggings", "leather_leggings durability:1");
				kit.set("Kits.nothing00.boots", "leather_boots durability:1");
				kit.set("Kits.nothing00.inventory", "");

				invlist = kit.getStringList("Kits.nothing00.inventory");
				invlist.add("kit: tools");
				invlist.add("cmd: broadcast &6&l{player} has got a Nothing00 kit!");
				invlist.add("golden_apple:1 64");
				kit.set("Kits.nothing00.inventory", invlist);
				super.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public List<String> getKits() {
		Set<String> kits = super.getConfiguration("Kits");
		List<String> kitList = new ArrayList<String>();
		kitList.addAll(kits);
		return kitList;
	}

	public boolean exists() {
		return getKits().contains(this.kit);
	}

	public int getDelay() {
		return Integer.parseInt(super.getString("Kits." + this.kit + ".delay"));
	}

	public boolean containsAlias() {
		boolean t = false;
		for (String s : getInventory()) {
			if (s.contains("kit:")) {
				t = true;
			}
		}
		return t;
	}

	public boolean containsCommands() {
		boolean t = false;
		for (String s : getInventory()) {
			if (s.contains("cmd:")) {
				t = true;
			}
		}
		return t;
	}

	public List<String> getAliases() {
		List<String> alias = new ArrayList<String>();
		for (String s : getInventory()) {
			if (s.contains("kit:")) {
				alias.add(s.split(" ")[1]);
			}
		}
		return alias;
	}

	public List<String> getCommands() {
		List<String> commands = new ArrayList<String>();
		for (String s : getInventory()) {
			if (s.contains("kit:")) {
				commands.add(s.substring(5, s.length()));
			}
		}
		return commands;
	}

	public void createKit(String helmet, String chetsplate, String leggings, String boots, List<String> inventory) {
		super.get().set("Kits." + this.kit + ".delay", 0);
		super.get().set("Kits." + this.kit + ".helmet", helmet);
		super.get().set("Kits." + this.kit + ".chestplate", chetsplate);
		super.get().set("Kits." + this.kit + ".leggings", leggings);
		super.get().set("Kits." + this.kit + ".boots", boots);
		super.get().set("Kits." + this.kit + ".inventory", inventory);
		super.save();
	}

	public void deleteKit() {
		super.get().set("Kits." + this.kit, null);
		super.save();
	}

	public void setDelay(int delay) {
		super.get().set("Kits." + this.kit + ".delay", delay);
		super.save();
	}

	public String getHelemt() {
		return super.getString("Kits." + this.kit + ".helmet");
	}

	public void setCmd(String cmd) {
		List<String> inv = super.getStringList("Kits." + this.kit + ".inventory");
		inv.add("cmd:" + cmd);
		super.get().set("Kits." + this.kit + ".inventory", inv);
		super.save();
	}

	public void setBook(String book) {
		List<String> inv = super.getStringList("Kits." + this.kit + ".inventory");
		Book b = new Book(book);
		if (!b.exists())
			return;
		inv.add("book: " + book);
		super.get().set("Kits." + this.kit + ".inventory", inv);
		super.save();
	}

	public void setAlias(Kit kit) {
		List<String> inv = super.getStringList("Kits." + this.kit + ".inventory");
		inv.add("kit: " + kit.getName());
		super.get().set("Kits." + this.kit + ".inventory", inv);
		super.save();
	}

	public String getChestplate() {
		return super.getString("Kits." + this.kit + ".chestplate");
	}

	public String getLeggings() {
		return super.getString("Kits." + this.kit + ".leggings");
	}

	public String getBoots() {
		return super.getString("Kits." + this.kit + ".boots");
	}

	public List<String> getInventory() {
		return super.getStringList("Kits." + this.kit + ".inventory");
	}
}
