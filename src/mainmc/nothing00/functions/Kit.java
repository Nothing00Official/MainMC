package mainmc.nothing00.functions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mainmc.MainPermissions;
import mainmc.folders.KitData;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.utils.Date;
import mainmc.nothing00.utils.Time;

public class Kit extends KitData {

	private String kit;
	private Player p;

	public Kit(String kit, Player p) {
		super(kit);
		this.kit = kit;
		this.p = p;
	}

	public String getName() {
		return this.kit;
	}

	public List<ItemStack> getEquipment() {
		List<ItemStack> equip = new ArrayList<ItemStack>();
		if (getHelmet() != null)
			equip.add(getHelmet());
		if (getChes() != null)
			equip.add(getChes());
		if (getLeg() != null)
			equip.add(getLeg());
		if (getBoot() != null)
			equip.add(getBoot());
		return equip;
	}

	public void setDelay() {
		Time time = new Time(new Date());
		if (super.getDelay() == 0)
			return;
		MainPlugin.getDataBase().setKitDelay(this.p.getUniqueId().toString(), this.kit.toLowerCase(),
				time.addTime(0, 0, 0, 0, 0, super.getDelay()).toString());
	}

	public boolean hasDelay() {
		return MainPlugin.getDataBase().hasDelay(this.p.getUniqueId().toString(), this.kit.toLowerCase());
	}

	public Date getExpire() {
		return new Date(MainPlugin.getDataBase().getDelay(this.p.getUniqueId().toString(), this.kit.toLowerCase()));
	}

	public void expired() {
		MainPlugin.getDataBase().deleteKit(this.p.getUniqueId().toString(), this.kit.toLowerCase());
	}

	public List<String> getPermittendKits() {
		List<String> kits = new ArrayList<String>();
		for (String k : super.getKits()) {
			if (!this.p.hasPermission("main.kit." + k))
				continue;
			kits.add(k);
		}
		return kits;
	}

	public boolean hasPermissionKit() {
		MainPermissions sender = new MainPermissions(this.p);
		if (sender.hasPermission("main.kit.*"))
			return true;
		if (sender.hasPermission("main.kit." + this.kit.toLowerCase()))
			return true;
		return false;
	}

	public ItemStack getHelmet() {
		if (super.getHelemt().isEmpty())
			return null;
		if (super.getHelemt().contains("skull"))
			return ItemPlugin.getSkullByString(super.getHelemt());
		String[] split = super.getHelemt().split(" ");
		ItemStack item = ItemPlugin.getItemString(split[0]);
		ItemPlugin it = new ItemPlugin(item);

		if (split.length > 1) {
			for (int i = 1; i < split.length; i++) {
				if (split[i].contains("name:")) {
					it.setName(split[i]);
				} else if (split[i].contains(":")) {
					it.enchant(split[i], it.getItem());
				}
			}
		}
		return it.getItem();
	}

	public ItemStack getChes() {
		if (super.getChestplate().isEmpty())
			return null;
		String[] split = super.getChestplate().split(" ");
		ItemStack item = ItemPlugin.getItemString(split[0]);
		ItemPlugin it = new ItemPlugin(item);

		if (split.length > 1) {
			for (int i = 1; i < split.length; i++) {
				if (split[i].contains("name:")) {
					it.setName(split[i]);
				} else if (split[i].contains(":")) {
					it.enchant(split[i], it.getItem());
				}
			}
		}
		return it.getItem();
	}

	public ItemStack getLeg() {
		if (super.getLeggings().isEmpty())
			return null;
		String[] split = super.getLeggings().split(" ");
		ItemStack item = ItemPlugin.getItemString(split[0]);
		ItemPlugin it = new ItemPlugin(item);

		if (split.length > 1) {
			for (int i = 1; i < split.length; i++) {
				if (split[i].contains("name:")) {
					it.setName(split[i]);
				} else if (split[i].contains(":")) {
					it.enchant(split[i], it.getItem());
				}
			}
		}
		return it.getItem();
	}

	public ItemStack getBoot() {
		if (super.getBoots().isEmpty())
			return null;
		String[] split = super.getBoots().split(" ");
		ItemStack item = ItemPlugin.getItemString(split[0]);
		ItemPlugin it = new ItemPlugin(item);

		if (split.length > 1) {
			for (int i = 1; i < split.length; i++) {
				if (split[i].contains("name:")) {
					it.setName(split[i]);
				} else if (split[i].contains(":")) {
					it.enchant(split[i], it.getItem());
				}
			}
		}
		return it.getItem();
	}

	public static ItemStack getCmdIcon(String cmd) {
		ItemStack item = new ItemStack(Material.PAPER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(cmd);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getAliasIcon(String alias) {
		ItemStack item = new ItemStack(Material.PAPER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(alias);
		item.setItemMeta(meta);
		return item;
	}

	public void giveKit() {
		User user = new User(this.p.getName());
		if (getHelmet() != null) {
			if (this.p.getInventory().getHelmet() != null) {
				user.addItem(getHelmet());
			} else {
				this.p.getInventory().setHelmet(getHelmet());
			}
		}
		if (getChes() != null) {
			if (this.p.getInventory().getChestplate() != null) {
				user.addItem(getChes());
			} else {
				this.p.getInventory().setChestplate(getChes());
			}
		}

		if (getLeg() != null) {
			if (this.p.getInventory().getLeggings() != null) {
				user.addItem(getLeg());
			} else {
				this.p.getInventory().setLeggings(getLeg());
			}
		}

		if (getBoot() != null) {
			if (this.p.getInventory().getBoots() != null) {
				user.addItem(getBoot());
			} else {
				this.p.getInventory().setBoots(getBoot());
			}
		}

		for (ItemStack i : getContents()) {
			if (i.getType().equals(Material.PAPER) && i.hasItemMeta() && i.getItemMeta().getDisplayName() != null
					&& i.getItemMeta().getDisplayName().contains("cmd:")
					|| i.getType().equals(Material.PAPER) && i.hasItemMeta() && i.getItemMeta().getDisplayName() != null
							&& i.getItemMeta().getDisplayName().contains("kit:"))

				continue;

			user.addItem(i);
		}
		this.p.updateInventory();

		if (super.containsCommands()) {
			for (String cmd : super.getCommands()) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						cmd.replace("{player}", this.p.getName()));
			}
		}

		if (super.containsAlias()) {
			for (String k : super.getAliases()) {
				Kit kitdata = new Kit(k, this.p);
				kitdata.giveKit();
			}
		}
	}

	public List<ItemStack> getContents() {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (String c : super.getInventory()) {
			if (c.contains("skull")) {
				items.add(ItemPlugin.getSkullByString(c));
				continue;
			}
			if (c.contains("cmd:")) {
				items.add(getCmdIcon(c));
				continue;
			}
			if (c.contains("kit:")) {
				items.add(getAliasIcon(c));
				continue;
			}
			if (c.contains("book:")) {
				Book book = new Book(c.split(" ")[1]);
				if (!book.exists())
					continue;
				items.add(ItemPlugin.getBook(book.getTitle(), book.getAuthor(), book.getPages()));
				continue;
			}
			String[] split = c.split(" ");
			if (split.length < 2)
				continue;
			ItemPlugin it = new ItemPlugin(ItemPlugin.getItemString(split[0]));
			if (NumberUtils.isNumber(split[1]))
				it.getItem().setAmount(Integer.parseInt(split[1]));
			if (split.length > 2) {
				for (int i = 2; i < split.length; i++) {
					if (split[i].contains("name:")) {
						it.setName(split[i]);
					} else if (split[i].contains(":")) {
						it.enchant(split[i], it.getItem());
					}
				}
			}
			items.add(it.getItem());
			continue;
		}
		return items;
	}

	public void createKit(Inventory inv) {

		ArrayList<String> items = new ArrayList<String>();
		String helmet = "", chestplate = "", leggings = "", boots = "";
		for (ItemStack i : inv.getContents()) {

			if (i == null)
				continue;

			ItemPlugin item = new ItemPlugin(i);
			if (helmet.isEmpty() && item.isHelmet()) {
				helmet = item.armorToString();
				continue;
			}
			if (helmet.isEmpty() && item.isSkull()) {
				helmet = item.toString();
				continue;
			}
			if (chestplate.isEmpty() && item.isChestplate()) {
				chestplate = item.armorToString();
				continue;
			}
			if (leggings.isEmpty() && item.isLeggings()) {
				leggings = item.armorToString();
				continue;
			}
			if (boots.isEmpty() && item.isBoots()) {
				boots = item.armorToString();
				continue;
			}
			items.add(item.toString());
		}
		if (items.isEmpty() && helmet.isEmpty() && chestplate.isEmpty() && leggings.isEmpty() && boots.isEmpty())
			return;
		super.createKit(helmet, chestplate, leggings, boots, items);
	}

	public void openCreatorInventory() {
		Inventory content = Bukkit.createInventory(null, 36, "Kit: " + this.kit.toLowerCase());
		this.p.openInventory(content);
	}

}
