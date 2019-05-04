package mainmc.nothing00.functions.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Gui {
	
	public void openGui();
	
	public ItemStack createButton(Material id, short subid, String name, String lore1, String lore2);

}
