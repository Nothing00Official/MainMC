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
import mainmc.nothing00.functions.Warp;

public class WarpGui implements Gui {

	private Player p;

	public WarpGui(Player p) {
		this.p = p;
	}

	@SuppressWarnings("deprecation")
	public void openGui() {
		Inventory inv = Bukkit.createInventory(null, 45, "Warps");
		Conf config = new Conf();
		for (String w : Warp.getWarps()) {
			Warp warp = new Warp(w);
			if (!warp.hasWarpPermissions(this.p)) {
				inv.addItem(createButton(Material.STAINED_GLASS_PANE, (short) 14, "§e" + warp.getName(),
						config.getDenyWarpLabel(), ""));
				continue;
			}
			if (!hasAdminGuiPermissions() && warp.isHidden())
				continue;
			if (warp.hasIcon(w))
				inv.addItem(createButton(warp.getIcon().getType(), warp.getIcon().getData().getData(),
						"§e" + warp.getName(), "", ""));
			else
				inv.addItem(createButton(Material.STAINED_GLASS, (short) 13, "§e" + warp.getName(), "", ""));
		}
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) != null)
				continue;
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3));
		}
		this.p.openInventory(inv);
	}

	public void getAdminMenu(String warp) {
		Conf config = new Conf();
		Warp w = new Warp(warp);
		Inventory inv = Bukkit.createInventory(null, 36, "AdminWarp: " + warp);
		inv.setItem(10, createButton(Material.STAINED_CLAY, (short) 14, config.getWarpTpLabel(), "", ""));
		if (w.isHidden())
			inv.setItem(13, createButton(Material.EYE_OF_ENDER, (short) 0, config.getShowWarpLabel(), "", ""));
		else
			inv.setItem(13, createButton(Material.EYE_OF_ENDER, (short) 0, config.getHideWarpLabel(), "", ""));
		inv.setItem(16, createButton(Material.STONE, (short) 0, config.getWarpIconLabel(), "", ""));
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) != null)
				continue;
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3));
		}
		this.p.openInventory(inv);
	}

	public void setHidden(String warp) {
		Warp w = new Warp(warp);
		if (w.isHidden())
			w.setHidden(false);
		else
			w.setHidden(true);
		getAdminMenu(warp);
	}

	public boolean setIcon(String warp) {
		if (this.p.getInventory().getItemInHand() == null
				|| this.p.getInventory().getItemInHand().getType().equals(Material.AIR))
			return false;
		Warp w = new Warp(warp);
		w.setIcon(this.p.getInventory().getItemInHand());
		return true;
	}

	public Warp getCurrentWarp(ItemStack item) {
		return new Warp(item.getItemMeta().getDisplayName().substring(2, item.getItemMeta().getDisplayName().length()));
	}

	public ItemStack createButton(Material id, short subid, String name, String lore1, String lore2) {
		ItemStack button = new ItemStack(id, 1, subid);
		ItemMeta meta = Bukkit.getItemFactory().getItemMeta(id);
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

	public boolean hasAdminGuiPermissions() {
		return this.p.hasPermission("main.warps.gui");
	}

}
