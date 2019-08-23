package mainmc.nothing00.functions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import mainmc.folders.Conf;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.utils.Economy;

public class ItemPlugin {

	private ItemStack item;

	public ItemPlugin(ItemStack item) {
		this.item = item;
	}

	public ItemPlugin(Material id, int q, short subid) {
		this.item = new ItemStack(id, q, subid);
	}

	public ItemStack getItem() {
		return this.item;
	}

	public void setName(String name) {
		name = name.substring(5, name.length());
		name = name.replace("_", " ").replaceAll("&", "§");
		ItemMeta meta = this.item.getItemMeta();
		meta.setDisplayName(name);
		this.item.setItemMeta(meta);
	}

	public boolean repair() {
		Conf config = new Conf();
		if (item == null)
			return false;
		if (!item.getEnchantments().isEmpty() && !config.repairEnchanted()) {
			return false;
		} else {
			if (item.getDurability() != 0) {
				if (item.getType().getMaxDurability() == 0)
					return false;
				item.setDurability((short) 0);
				return true;
			}
		}
		return false;
	}

	public static void RepairAll(Player p) {
		Conf config = new Conf();
		for (ItemStack items : p.getInventory().getContents()) {
			if (items == null || items.getDurability() == 0)
				continue;
			if (items.getType().getMaxDurability() == 0)
				continue;
			if (!config.repairEnchanted()) {
				if (items.getEnchantments().isEmpty()) {
					items.setDurability((short) 0);
				}
			} else {
				items.setDurability((short) 0);
			}
		}

		for (ItemStack items : p.getEquipment().getArmorContents()) {
			if (items == null || items.getDurability() == 0)
				continue;
			if (!config.repairEnchanted()) {
				if (items.getEnchantments().isEmpty()) {
					items.setDurability((short) 0);
				}
			} else {
				items.setDurability((short) 0);
			}
		}

		p.updateInventory();
	}

	public ItemStack getSpawner(EntityType type) {
		if (!this.item.getType().equals(Material.MOB_SPAWNER))
			return this.item;
		ItemMeta meta = this.item.getItemMeta();
		meta.setDisplayName("§eSpawner: " + type.toString());
		this.item.setItemMeta(meta);
		return this.item;
	}

	public boolean isSpawner() {
		if (!this.item.getType().equals(Material.MOB_SPAWNER))
			return false;
		if (!this.item.hasItemMeta())
			return false;
		ItemMeta meta = this.item.getItemMeta();
		return meta.getDisplayName().contains("§eSpawner:");
	}

	public static ItemStack getBankCheck(Economy eco) {
		ItemStack ass = new ItemStack(Material.PAPER, 1);
		ItemMeta meta = ass.getItemMeta();
		meta.setDisplayName("§e" + eco.getMoneyToString());
		List<String> list = new ArrayList<String>();
		list.add("§ev: " + eco.getMoneyValue());
		list.add("§ca: " + eco.getUser());
		meta.setLore(list);
		ass.setItemMeta(meta);
		eco.removeMoney();
		return ass;
	}

	public boolean isBankCheck() {
		if (!this.item.hasItemMeta())
			return false;
		if (this.item.getItemMeta().getLore() == null)
			return false;
		if (this.item.getItemMeta().getLore().size() < 2)
			return false;
		if (this.item.getItemMeta().getLore().get(0).contains("§ev: ")
				&& this.item.getItemMeta().getLore().get(1).contains("§ca: "))
			return true;
		return false;
	}

	public double getCheckBankValue() {
		return Double.parseDouble(this.item.getItemMeta().getLore().get(0).split(" ")[1]);
	}

	public void setSpawner(Block b) {
		final CreatureSpawner spawner = (CreatureSpawner) b.getState();
		ItemMeta meta = this.item.getItemMeta();
		spawner.setSpawnedType(EntityType.valueOf(meta.getDisplayName().split(" ")[1]));
		spawner.update();
	}


	@SuppressWarnings("deprecation")
	public String toString() {

		if (this.item.getType().equals(Material.SKULL_ITEM)) {
			SkullMeta meta = (SkullMeta) this.item.getItemMeta();
			return "skull owner:" + meta.getOwner();
		}

		String item = this.item.getType().toString() + "";
		if (this.item.getData().getData() != 0) {
			item += ":" + this.item.getData().getData();
		}
		item += " " + this.item.getAmount();
		if (!this.item.getEnchantments().isEmpty()) {
			item = addEnchanmentList(item);
		}
		if (this.item.hasItemMeta() && this.item.getItemMeta().getDisplayName() != null) {
			item += " name:" + this.item.getItemMeta().getDisplayName().replaceAll("§", "&").replaceAll(" ", "_");
		}
		return item;
	}

	public ItemStack addCommand(String cmd, boolean console) {
		ItemMeta meta = this.item.getItemMeta();
		List<String> lore;
		if (meta.getLore() == null) {
			lore = new ArrayList<String>();
		} else {
			lore = meta.getLore();
		}
		if (console)
			lore.add("§econsole:" + cmd);
		else
			lore.add("§eplayer:" + cmd);
		meta.setLore(lore);
		this.item.setItemMeta(meta);
		return this.item;
	}

	public void executeCommands(Player p) {
		ItemMeta meta = this.item.getItemMeta();

		if (meta.getLore() == null)
			return;

		List<String> lore = meta.getLore();
		for (String cmd : lore) {
			if (cmd.contains("console:")) {

				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						cmd.substring(10, cmd.length()).replace("{player}", p.getName()));

				continue;
			}
			if (cmd.contains("player:")) {
				Bukkit.getServer().dispatchCommand(p, cmd.substring(9, cmd.length()));
				continue;
			}
		}

	}

	public static ItemStack getBook(String title, String author, List<String> pages) {
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta meta = (BookMeta) book.getItemMeta();
		meta.setAuthor(author);
		meta.setPages(pages);
		meta.setTitle(title);
		meta.setDisplayName(title);
		book.setItemMeta(meta);
		return book;
	}

	public String armorToString() {

		if (!isHelmet() && !isChestplate() && !isLeggings() && !isBoots())
			return "";

		String item = this.item.getType().toString() + "";

		if (!this.item.getEnchantments().isEmpty()) {
			item = addEnchanmentList(item);
		}
		if (this.item.hasItemMeta() && this.item.getItemMeta().getDisplayName() != null) {
			item += " name:" + this.item.getItemMeta().getDisplayName().replaceAll("§", "&").replaceAll(" ", "_");
		}
		return item;
	}

	private String addEnchanmentList(String item) {
		ArrayList<Enchantment> enchlist = new ArrayList<Enchantment>();
		enchlist.add(Enchantment.PROTECTION_ENVIRONMENTAL);
		enchlist.add(Enchantment.PROTECTION_FIRE);
		enchlist.add(Enchantment.PROTECTION_FALL);
		enchlist.add(Enchantment.PROTECTION_EXPLOSIONS);
		enchlist.add(Enchantment.PROTECTION_PROJECTILE);
		enchlist.add(Enchantment.OXYGEN);
		enchlist.add(Enchantment.WATER_WORKER);
		enchlist.add(Enchantment.THORNS);
		enchlist.add(Enchantment.DEPTH_STRIDER);
		enchlist.add(Enchantment.DAMAGE_ALL);
		enchlist.add(Enchantment.DAMAGE_UNDEAD);
		enchlist.add(Enchantment.DAMAGE_ARTHROPODS);
		enchlist.add(Enchantment.KNOCKBACK);
		enchlist.add(Enchantment.FIRE_ASPECT);
		enchlist.add(Enchantment.LOOT_BONUS_MOBS);
		enchlist.add(Enchantment.DIG_SPEED);
		enchlist.add(Enchantment.SILK_TOUCH);
		enchlist.add(Enchantment.LOOT_BONUS_BLOCKS);
		enchlist.add(Enchantment.ARROW_DAMAGE);
		enchlist.add(Enchantment.ARROW_KNOCKBACK);
		enchlist.add(Enchantment.ARROW_FIRE);
		enchlist.add(Enchantment.ARROW_INFINITE);
		enchlist.add(Enchantment.LUCK);
		enchlist.add(Enchantment.LURE);
		enchlist.add(Enchantment.DURABILITY);
		Enchantment[] ench = enchlist.toArray(new Enchantment[0]);
		for (int i = 0; i < ench.length; i++) {
			if (this.item.getEnchantments().containsKey(ench[i]))
				item += " " + enchantToString(ench[i]) + ":" + this.item.getEnchantments().get(ench[i]);
		}
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getItemString(String item) {
		if (MainPlugin.version < 13) {
			if (item.contains(":")) {
				String[] split = item.split(":");
				if (NumberUtils.isNumber(split[0]) && NumberUtils.isNumber(split[1])) {
					return new ItemStack(Integer.parseInt(split[0]), 1, Short.parseShort(split[1]));
				} else if (NumberUtils.isNumber(split[1])) {
					return new ItemStack(Material.getMaterial(split[0].toUpperCase()), 1, Short.parseShort(split[1]));
				}
				return new ItemStack(Material.AIR, 1);
			} else if (NumberUtils.isNumber(item)) {
				return new ItemStack(Integer.parseInt(item), 1);
			} else if (Material.getMaterial(item.toUpperCase()) != null) {
				return new ItemStack(Material.getMaterial(item.toUpperCase()), 1);
			}
			return new ItemStack(Material.AIR, 1);
		}
		if (item.contains(":")) {
			String[] split = item.split(":");
		    if (NumberUtils.isNumber(split[1]) && Material.getMaterial(split[0].toUpperCase()) != null) {
				return new ItemStack(Material.getMaterial(split[0].toUpperCase()), 1, Short.parseShort(split[1]));
			}
			return new ItemStack(Material.AIR, 1);
		} else if (Material.getMaterial(item.toUpperCase()) != null) {
			return new ItemStack(Material.getMaterial(item.toUpperCase()), 1);
		}
		return new ItemStack(Material.AIR, 1);

	}

	public static ItemStack getSkullByUser(String user) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(user);
		skull.setItemMeta(meta);
		return skull;
	}

	public static ItemStack getSkullByString(String str) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		if (!str.contains("skull"))
			return new ItemStack(Material.AIR, 1);

		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(str.split(" ")[1].split(":")[1]);
		skull.setItemMeta(meta);
		return skull;
	}

	public boolean isHelmet() {
		return this.item.getType() == Material.CHAINMAIL_HELMET || this.item.getType() == Material.DIAMOND_HELMET
				|| this.item.getType() == Material.GOLD_HELMET || this.item.getType() == Material.GOLD_HELMET
				|| this.item.getType() == Material.IRON_HELMET || this.item.getType() == Material.LEATHER_HELMET;
	}

	public boolean isChestplate() {
		return this.item.getType() == Material.CHAINMAIL_CHESTPLATE
				|| this.item.getType() == Material.DIAMOND_CHESTPLATE || this.item.getType() == Material.GOLD_CHESTPLATE
				|| this.item.getType() == Material.GOLD_CHESTPLATE || this.item.getType() == Material.IRON_CHESTPLATE
				|| this.item.getType() == Material.LEATHER_CHESTPLATE;
	}

	public boolean isLeggings() {
		return this.item.getType() == Material.CHAINMAIL_LEGGINGS || this.item.getType() == Material.DIAMOND_LEGGINGS
				|| this.item.getType() == Material.GOLD_LEGGINGS || this.item.getType() == Material.GOLD_LEGGINGS
				|| this.item.getType() == Material.IRON_LEGGINGS || this.item.getType() == Material.LEATHER_LEGGINGS;
	}

	public boolean isBoots() {
		return this.item.getType() == Material.CHAINMAIL_BOOTS || this.item.getType() == Material.DIAMOND_BOOTS
				|| this.item.getType() == Material.GOLD_BOOTS || this.item.getType() == Material.GOLD_BOOTS
				|| this.item.getType() == Material.IRON_BOOTS || this.item.getType() == Material.LEATHER_BOOTS;
	}

	public boolean isSkull() {
		return this.item.getType().equals(Material.SKULL_ITEM);
	}

	@SuppressWarnings("deprecation")
	private Enchantment convenchant(String nametext, ItemStack item) {

		String name = nametext.toLowerCase();

		ArrayList<Integer> armature = new ArrayList<Integer>();
		for (int i = 298; i < 318; i++) {
			armature.add(i);
		}

		ArrayList<Integer> armi = new ArrayList<Integer>();
		armi.add(267);
		armi.add(268);
		armi.add(272);
		armi.add(276);

		ArrayList<Integer> tools = new ArrayList<Integer>();
		for (int i = 256; i < 259; i++) {
			tools.add(i);
		}
		for (int i = 269; i < 272; i++) {
			tools.add(i);
		}
		for (int i = 273; i < 276; i++) {
			tools.add(i);
		}
		for (int i = 277; i < 280; i++) {
			tools.add(i);
		}
		for (int i = 284; i < 287; i++) {
			tools.add(i);
		}

		Enchantment en = null;

		switch (name) {

		case "prot":
		case "protection":

			if (armature.contains(item.getType().getId())) {
				en = Enchantment.PROTECTION_ENVIRONMENTAL;
			} else {
				en = null;
			}

			break;

		case "fireprot":
		case "fireprotection":

			if (armature.contains(item.getType().getId())) {
				en = Enchantment.PROTECTION_FIRE;
			} else {
				en = null;
			}

			break;

		case "featherfalling":
		case "featherprot":
		case "fallprot":
		case "featherprotection":

			if (item.getType().getId() == 301 || item.getType().getId() == 305 || item.getType().getId() == 309
					|| item.getType().getId() == 313 || item.getType().getId() == 317) {
				en = Enchantment.PROTECTION_FALL;
			} else {
				en = null;
			}

			break;

		case "blast":
		case "blastprot":
		case "blastprotection":

			if (armature.contains(item.getType().getId())) {
				en = Enchantment.PROTECTION_EXPLOSIONS;
			} else {
				en = null;
			}

			break;

		case "projprot":
		case "projectile":
		case "projectileprotection":

			if (armature.contains(item.getType().getId())) {
				en = Enchantment.PROTECTION_PROJECTILE;
			} else {
				en = null;
			}

			break;

		case "respiration":

			if (item.getType().getId() == 310 || item.getType().getId() == (314) || item.getType().getId() == (306)
					|| item.getType().getId() == (302) || item.getType().getId() == (298)) {
				en = Enchantment.OXYGEN;
			} else {
				en = null;
			}

			break;

		case "aqua":
		case "aquaaffinity":

			if (item.getType().getId() == 310 || item.getType().getId() == (314) || item.getType().getId() == (306)
					|| item.getType().getId() == (302) || item.getType().getId() == (298)) {
				en = Enchantment.WATER_WORKER;
			} else {
				en = null;
			}

			break;

		case "thorns":

			if (armature.contains(item.getType().getId())) {
				en = Enchantment.THORNS;
			} else {
				en = null;
			}

			break;

		case "depth":
		case "depthstrider":

			if (item.getType().getId() == 301 || item.getType().getId() == 305 || item.getType().getId() == 309
					|| item.getType().getId() == 313 || item.getType().getId() == 317) {
				en = Enchantment.DEPTH_STRIDER;
			} else {
				en = null;
			}

			break;

		case "sharpness":

			if (armi.contains(item.getType().getId()) || item.getType().getId() == 271 || item.getType().getId() == 275
					|| item.getType().getId() == 258 || item.getType().getId() == 286
					|| item.getType().getId() == 279) {
				en = Enchantment.DAMAGE_ALL;
			} else {
				en = null;
			}

			break;

		case "smite":

			if (armi.contains(item.getType().getId())) {
				en = Enchantment.DAMAGE_UNDEAD;
			} else {
				en = null;
			}

			break;

		case "arthropods":
		case "baneofarthropods":

			if (armi.contains(item.getType().getId())) {
				en = Enchantment.DAMAGE_ARTHROPODS;
			} else {
				en = null;
			}

			break;

		case "knockback":

			if (armi.contains(item.getType().getId()) || item.getType().getId() == 271 || item.getType().getId() == 275
					|| item.getType().getId() == 258 || item.getType().getId() == 286
					|| item.getType().getId() == 279) {
				en = Enchantment.KNOCKBACK;
			} else {
				en = null;
			}

			break;

		case "fire":
		case "fireaspect":

			if (armi.contains(item.getType().getId()) || item.getType().getId() == 271 || item.getType().getId() == 275
					|| item.getType().getId() == 258 || item.getType().getId() == 286
					|| item.getType().getId() == 279) {
				en = Enchantment.FIRE_ASPECT;
			} else {
				en = null;
			}

			break;

		case "loot":
		case "looting":

			if (armi.contains(item.getType().getId())) {
				en = Enchantment.LOOT_BONUS_MOBS;
			} else {
				en = null;
			}

			break;

		case "eff":
		case "efficiency":

			if (tools.contains(item.getType().getId())) {
				en = Enchantment.DIG_SPEED;
			} else {
				en = null;
			}

			break;

		case "silk":
		case "silktouch":

			if (tools.contains(item.getType().getId())) {
				en = Enchantment.SILK_TOUCH;
			} else {
				en = null;
			}

			break;

		case "fortune":

			if (tools.contains(item.getType().getId())) {
				en = Enchantment.LOOT_BONUS_BLOCKS;
			} else {
				en = null;
			}

			break;

		case "power":

			if (item.getType().getId() == 261) {
				en = Enchantment.ARROW_DAMAGE;
			} else {
				en = null;
			}

			break;

		case "punch":

			if (item.getType().getId() == 261) {
				en = Enchantment.ARROW_KNOCKBACK;
			} else {
				en = null;
			}

			break;

		case "flame":

			if (item.getType().getId() == 261) {
				en = Enchantment.ARROW_FIRE;
			} else {
				en = null;
			}

			break;

		case "infinity":

			if (item.getType().getId() == 261) {
				en = Enchantment.ARROW_INFINITE;
			} else {
				en = null;
			}

			break;

		case "sea":
		case "luck":
		case "luckofthesea":

			if (item.getType().getId() == 346) {
				en = Enchantment.LUCK;
			} else {
				en = null;
			}

			break;

		case "lure":

			if (item.getType().getId() == 346) {
				en = Enchantment.LURE;
			} else {
				en = null;
			}

			break;

		case "unbreaking":
		case "ind":
		case "durability":
		case "unbreak":

			if (item.getType().getId() == 346 || item.getType().getId() == 261
					|| armature.contains(item.getType().getId()) || armi.contains(item.getType().getId())
					|| tools.contains(item.getType().getId())) {
				en = Enchantment.DURABILITY;
			} else {
				en = null;
			}

			break;

		default:
			en = null;
			break;

		}

		return en;

	}

	private boolean checklevel(String en, int l) {

		boolean c = false;

		switch (en) {

		case "prot":
		case "protection":
		case "fireprot":
		case "fireprotection":
		case "featherprot":
		case "fallprot":
		case "feathereprotection":
		case "blast":
		case "blastprot":
		case "blastprotection":
		case "projprot":
		case "projectile":
		case "projectileprotection":

			if (l <= 4 && l > 0) {
				c = true;
			}

			break;

		case "respiration":

			if (l <= 3 && l > 0) {
				c = true;
			}

			break;

		case "aqua":
		case "aquaaffinity":

			if (l == 1 && l > 0) {
				c = true;
			}

			break;

		case "thorns":
		case "depth":
		case "depthstrider":

			if (l <= 3 && l > 0) {
				c = true;
			}

			break;

		case "sharpness":
		case "smite":
		case "arthropods":
		case "baneofarthropods":

			if (l <= 5 && l > 0) {
				c = true;
			}

			break;

		case "knockback":
		case "fire":
		case "fireaspect":

			if (l <= 2 && l > 0) {
				c = true;
			}

			break;

		case "loot":
		case "looting":

			if (l <= 3 && l > 0) {
				c = true;
			}

			break;

		case "eff":
		case "efficiency":

			if (l <= 5 && l > 0) {
				c = true;
			}

			break;

		case "silk":
		case "silktouch":

			if (l == 1 && l > 0) {
				c = true;
			}

			break;

		case "fortune":

			if (l <= 3 && l > 0) {
				c = true;
			}
			break;

		case "power":

			if (l <= 5 && l > 0) {
				c = true;
			}

			break;

		case "punch":

			if (l <= 2 && l > 0) {
				c = true;
			}

			break;

		case "flame":
		case "infinity":

			if (l == 1 && l > 0) {
				c = true;
			}

			break;

		case "sea":
		case "luck":
		case "luckofthesea":
		case "lure":

			if (l <= 3 && l > 0) {
				c = true;
			}

			break;

		case "unbreaking":
		case "ind":
		case "durability":
		case "unbreak":

			if (l <= 3 && l > 0) {
				c = true;
			}

			break;

		default:
			c = false;
			break;

		}

		return c;

	}

	public void enchant(String enfull, ItemStack item) {

		if (!enfull.contains(":"))
			return;

		String[] array = enfull.split(":");
		if (NumberUtils.isNumber(array[1])) {

			int l = Integer.parseInt(array[1]);

			Enchantment en = null;

			Conf config = new Conf();
			if (Enchantment.getByName(array[0]) == null) {
				en = convenchant(array[0], item);
				if (en == null)
					return;

				if (config.bypassEnchant()) {
					item.addUnsafeEnchantment(en, l);
				} else {
					if (checklevel(array[0], l)) {
						item.addEnchantment(en, l);
					}
				}

			} else {
				if (config.bypassEnchant()) {
					item.addUnsafeEnchantment(en, l);
				} else {
					if (checklevel(array[0], l)) {
						item.addEnchantment(en, l);
					}
				}
			}
		}

	}

	public static String enchantToString(Enchantment en) {
		if (en == Enchantment.PROTECTION_ENVIRONMENTAL) {
			return "protection";
		} else if (en == Enchantment.PROTECTION_FIRE) {
			return "fireprotection";
		} else if (en == Enchantment.PROTECTION_FALL) {
			return "feathereprotection";
		} else if (en == Enchantment.PROTECTION_PROJECTILE) {
			return "projectileprotection";
		} else if (en == Enchantment.PROTECTION_EXPLOSIONS) {
			return "blastprotection";
		} else if (en == Enchantment.OXYGEN) {
			return "respiration";
		} else if (en == Enchantment.WATER_WORKER) {
			return "aqua";
		} else if (en == Enchantment.THORNS) {
			return "thorns";
		} else if (en == Enchantment.DEPTH_STRIDER) {
			return "depth";
		} else if (en == Enchantment.DAMAGE_ALL) {
			return "sharpness";
		} else if (en == Enchantment.DAMAGE_UNDEAD) {
			return "smite";
		} else if (en == Enchantment.DAMAGE_ARTHROPODS) {
			return "arthropods";
		} else if (en == Enchantment.KNOCKBACK) {
			return "knockback";
		} else if (en == Enchantment.FIRE_ASPECT) {
			return "fireaspect";
		} else if (en == Enchantment.LOOT_BONUS_MOBS) {
			return "looting";
		} else if (en == Enchantment.DIG_SPEED) {
			return "efficiency";
		} else if (en == Enchantment.SILK_TOUCH) {
			return "silktouch";
		} else if (en == Enchantment.LOOT_BONUS_BLOCKS) {
			return "fortune";
		} else if (en == Enchantment.ARROW_DAMAGE) {
			return "power";
		} else if (en == Enchantment.ARROW_KNOCKBACK) {
			return "punch";
		} else if (en == Enchantment.ARROW_FIRE) {
			return "flame";
		} else if (en == Enchantment.ARROW_INFINITE) {
			return "infinity";
		} else if (en == Enchantment.LUCK) {
			return "sea";
		} else if (en == Enchantment.LURE) {
			return "lure";
		} else if (en == Enchantment.DURABILITY) {
			return "durability";
		} else {
			return "";
		}
	}

	public static int parseId(String s) {
		char[] car = s.toCharArray();
		int cont = 0;
		for (int i = 0; i < s.length(); i++) {
			if (car[i] == '.') {
				cont++;
			}
		}

		if (cont == 1) {
			double sdouble = Double.parseDouble(s);
			double floor = Math.floor(sdouble);

			return (int) floor;
		} else if (cont == 0) {
			return Integer.parseInt(s);
		} else {
			s = s.replaceAll(".", "");
			return Integer.parseInt(s);
		}
	}

	@SuppressWarnings("deprecation")
	public boolean isHat() {
		return this.item != null && this.item.getType().getId() < 256 && this.item.getType().getId() != 0;
	}

}