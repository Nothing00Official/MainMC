package mainmc.nothing00.functions.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mainmc.folders.Conf;
import mainmc.nothing00.functions.Kit;
import mainmc.nothing00.utils.Date;
import mainmc.nothing00.utils.Time;

public class KitGui implements Gui{

	private Player p;

	public KitGui(Player p) {
		this.p = p;
	}

	public void openGui() {
		Inventory inv = Bukkit.createInventory(null, 45, "Kits");
		Kit kit = new Kit("", this.p);
		Conf config = new Conf();
		for (String k : kit.getKits()) {
			Kit kd = new Kit(k, this.p);
			if (!kd.hasPermissionKit()) {
				inv.addItem(createButton(Material.STAINED_CLAY, (short) 14, "§e" + kd.getName(),
						config.getDeniedLabel(), ""));
				continue;
			}
			if (kd.hasDelay()) {
				Time today = new Time(new Date());
				Time expire = new Time(kd.getExpire());
				if (expire.isPastOrPresent()) {
					kd.expired();
					inv.addItem(createButton(Material.STAINED_CLAY, (short) 13, "§e" + kd.getName(),
							config.getPreviewLabel(), config.getGiveLabel()));
					continue;
				} else {
					inv.addItem(createButton(Material.STAINED_CLAY, (short) 13, "§e" + kd.getName(),
							"§c" + Time.getStringTimeFromSecond(
									(int) (expire.getDateInSeconds() - today.getDateInSeconds())),
							config.getLockedPreviewLabel()));
					continue;
				}
			}
			inv.addItem(createButton(Material.STAINED_CLAY, (short) 13, "§e" + kd.getName(), config.getPreviewLabel(),
					config.getGiveLabel()));
		}
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) != null)
				continue;
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3));
		}
		this.p.openInventory(inv);
	}

	public void showPreview(String kit) {
		Inventory inv = Bukkit.createInventory(null, 36, "Preview: " + kit);
		Kit k = new Kit(kit, this.p);
		for (ItemStack item : k.getEquipment()) {
			inv.addItem(item);
		}
		for (ItemStack item : k.getContents()) {
			inv.addItem(item);
		}
		inv.setItem(35, createButton(Material.BARRIER, (short) 0, "§cKits", "", ""));
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) != null)
				continue;
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3));
		}
		this.p.openInventory(inv);
	}

	public String getCurrentKit(ItemStack item) {
		return item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length());
	}

	public boolean isUnlocked(ItemStack button) {
		List<String> lore = button.getItemMeta().getLore();
		Conf config = new Conf();
		return lore.get(1).contains(config.getGiveLabel());
	}

	public ItemStack createButton(Material id, short subid, String name, String lore1, String lore2) {
		ItemStack button = new ItemStack(id, 1, subid);
		ItemMeta meta = button.getItemMeta();
		meta.setDisplayName(name);
		List<String> lore = new ArrayList<String>();
		if (!lore1.isEmpty())
			lore.add(lore1);
		if (!lore2.isEmpty())
			lore.add(lore2);
		meta.setLore(lore);
		button.setItemMeta(meta);
		return button;
	}

}
