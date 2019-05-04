package mainmc.nothing00.functions;

import org.apache.commons.lang.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import mainmc.MainPermissions;
import mainmc.SignType;
import mainmc.folders.Conf;

@SuppressWarnings("deprecation")
public class PluginSign {

	private String line1;
	private String line2;
	private String line3;
	private String line4;

	public PluginSign(String line1, String line2, String line3, String line4) {
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
		this.line4 = line4;
	}

	public boolean isSign() {
		Conf config = new Conf();
		if (config.getSigns().contains("balance") && this.line1.contains("[balance]")) {
			return true;
		} else if (config.getSigns().contains("buy") && this.line1.contains("[buy]")) {
			return true;
		} else if (config.getSigns().contains("sell") && this.line1.contains("[sell]")) {
			return true;
		} else if (config.getSigns().contains("free") && this.line1.contains("[free]")) {
			return true;
		} else if (config.getSigns().contains("disposal") && this.line1.contains("[disposal]")) {
			return true;
		} else if (config.getSigns().contains("warp") && this.line1.contains("[warp]")) {
			return true;
		} else if (config.getSigns().contains("kit") && this.line1.contains("[kit]")) {
			return true;
		} else if (config.getSigns().contains("enchant") && this.line1.contains("[enchant]")) {
			return true;
		} else if (config.getSigns().contains("gamemode") && this.line1.contains("[gamemode]")) {
			return true;
		} else if (config.getSigns().contains("heal") && this.line1.contains("[heal]")) {
			return true;
		} else if (config.getSigns().contains("repair") && this.line1.contains("[repair]")) {
			return true;
		} else if (config.getSigns().contains("time") && this.line1.contains("[time]")) {
			return true;
		} else if (config.getSigns().contains("weather") && this.line1.contains("[weather]")) {
			return true;
		}
		return false;
	}

	public SignType getType() {
		Conf config = new Conf();
		if (config.getSigns().contains("balance") && this.line1.contains("[balance]")) {
			return SignType.BALANCE;
		} else if (config.getSigns().contains("buy") && this.line1.contains("[buy]")) {
			return SignType.BUY;
		} else if (config.getSigns().contains("sell") && this.line1.contains("[sell]")) {
			return SignType.SELL;
		} else if (config.getSigns().contains("free") && this.line1.contains("[free]")) {
			return SignType.FREE;
		} else if (config.getSigns().contains("disposal") && this.line1.contains("[disposal]")) {
			return SignType.DISPOSAL;
		} else if (config.getSigns().contains("warp") && this.line1.contains("[warp]")) {
			return SignType.WARP;
		} else if (config.getSigns().contains("kit") && this.line1.contains("[kit]")) {
			return SignType.KIT;
		} else if (config.getSigns().contains("enchant") && this.line1.contains("[enchant]")) {
			return SignType.ENCHANT;
		} else if (config.getSigns().contains("gamemode") && this.line1.contains("[gamemode]")) {
			return SignType.GAMEMODE;
		} else if (config.getSigns().contains("heal") && this.line1.contains("[heal]")) {
			return SignType.HEAL;
		} else if (config.getSigns().contains("repair") && this.line1.contains("[repair]")) {
			return SignType.REPAIR;
		} else if (config.getSigns().contains("time") && this.line1.contains("[time]")) {
			return SignType.TIME;
		} else if (config.getSigns().contains("weather") && this.line1.contains("[weather]")) {
			return SignType.WEATHER;
		}
		return null;
	}

	public void setSign() {
		Conf config = new Conf();
		if (config.getSigns().contains("balance") && line1.contains("[balance]")) {

			if (config.economyDisabled())
				return;

			this.line1 = "§1§lBalance";

		} else if (config.getSigns().contains("buy") && line1.contains("[buy]")) {
			if (config.economyDisabled())
				return;

			if (NumberUtils.isNumber(line2) && ItemPlugin.getItemString(line3) != null
					&& ItemPlugin.getItemString(line3).getType() != Material.AIR && line4.contains(config.getValuta())
					&& NumberUtils.isNumber(line4.substring(1, line4.length()))) {
				this.line1 = "§1§lBuy";
				this.line2 = "§4" + line2;
				this.line3 = "§8" + line3;
				this.line4 = "§2§l" + line4;
			} else {
				this.line1 = "§4[buy]";
			}

		} else if (config.getSigns().contains("sell") && line1.contains("[sell]")) {
			if (config.economyDisabled())
				return;
			if (NumberUtils.isNumber(line2) && ItemPlugin.getItemString(line3) != null
					&& ItemPlugin.getItemString(line3).getType() != Material.AIR && line4.contains(config.getValuta())
					&& NumberUtils.isNumber(line4.substring(1, line4.length()))) {
				this.line1 = "§1§lSell";
				this.line2 = "§4" + line2;
				this.line3 = "§8" + line3;
				this.line4 = "§2§l" + line4;
			} else {
				this.line1 = "§4[sell]";
			}

		} else if (config.getSigns().contains("free") && line1.contains("[free]")) {

			if (ItemPlugin.getItemString(line2) != null && ItemPlugin.getItemString(line2).getType() != Material.AIR) {
				this.line1 = "§1§lFree";
			} else {
				this.line1 = "§4[free]";
			}

		} else if (config.getSigns().contains("disposal") && line1.contains("[disposal]")) {
			this.line1 = "§1§lTrash";

		} else if (config.getSigns().contains("warp") && line1.contains("[warp]")) {

			Warp warp = new Warp(line2);
			if (warp.exists()) {
				this.line1 = "§1§lWarp";
			} else {
				this.line1 = "§4[warp]";
			}

		} else if (config.getSigns().contains("kit") && line1.contains("[kit]")) {
			Kit kit = new Kit("", null);
			if (kit.getKits().contains(line2)) {
				this.line1 = "§1§lKit";
			} else {
				this.line1 = "§4[kit]";
			}

		} else if (config.getSigns().contains("enchant") && line1.contains("[enchant]")) {

			if (line2.split(":").length == 2 && NumberUtils.isNumber(line2.split(":")[1])) {
				this.line1 = "§1§lEnchant";
				this.line2 = "§9§l" + line2;
			} else {
				this.line1 = "§4[enchant]";
			}

		} else if (config.getSigns().contains("gamemode") && line1.contains("[gamemode]")) {

			if (line2.contains("survival")) {
				this.line1 = "§1§lGameMode";
				this.line2 = "§9Survival";
			} else if (line2.contains("creative")) {
				this.line1 = "§1§lGameMode";
				this.line2 = "§9Creative";
			} else if (line2.contains("adventure")) {
				this.line1 = "§1§lGameMode";
				this.line2 = "§9Adventure";
			} else if (line2.contains("spectator")) {
				this.line1 = "§1§lGameMode";
				this.line2 = "§9Spectator";
			} else {
				this.line1 = "§4[gamemode]";
			}

		} else if (config.getSigns().contains("heal") && line1.contains("[heal]")) {
			this.line1 = "§1§lHeal";

		} else if (config.getSigns().contains("repair") && line1.contains("[repair]")) {

			if (line2.contains("hand")) {
				this.line1 = "§1§lRepair";
				this.line2 = "§9Hand";
			} else if (line2.contains("all")) {
				this.line1 = "§1§lRepair";
				this.line2 = "§9All";
			} else {
				this.line1 = "§4[repair]";
			}

		} else if (config.getSigns().contains("time") && line1.contains("[time]")) {

			if (line2.contains("day")) {
				this.line1 = "§1§lDay";
			} else if (line2.contains("night")) {
				this.line1 = "§1§lNight";
			} else {
				this.line1 = "§4[time]";
			}

		} else if (config.getSigns().contains("weather") && line1.contains("[weather]")) {

			if (line2.contains("sun")) {
				this.line1 = "§1§lWeather";
				this.line2 = "§9sun";
			} else if (line2.contains("storm")) {
				this.line1 = "§1§lWeather";
				this.line2 = "§9storm";
			} else {
				this.line1 = "§4[weather]";
			}

		}
	}

	public boolean hasEconomy() {
		Conf config = new Conf();
		return line3.contains(config.getValuta()) && NumberUtils.isNumber(line3.substring(5, line3.length()));
	}

	public void setEconomy() {
		Conf config = new Conf();

		if (config.economyDisabled())
			return;

		if (line3.contains(config.getValuta()) && NumberUtils.isNumber(line3.substring(1, line3.length()))) {
			this.line3 = "§2§l" + line3;

		}
	}

	public double getPrice(boolean eco) {
		if (eco)
			return Double.parseDouble(line4.substring(5, line4.length()));
		return Double.parseDouble(line3.substring(5, line3.length()));
	}

	public void colorizeSign(Player player) {

		Conf config = new Conf();
		if (!config.getSigns().contains("color"))
			return;

		MainPermissions p = new MainPermissions(player);
		if (p.hasPermission("main.sign.color") || p.hasPermission("main.sign.*")) {

			line1 = line1.replaceAll("&1", "§1").replaceAll("&2", "§2").replaceAll("&3", "§3").replaceAll("&4", "§4")
					.replaceAll("&5", "§5").replaceAll("&6", "§6").replaceAll("&7", "§7").replaceAll("&8", "§8")
					.replaceAll("&9", "§9").replaceAll("&0", "§0").replaceAll("&a", "§a").replaceAll("&b", "§b")
					.replaceAll("&c", "§c").replaceAll("&d", "§d").replaceAll("&e", "§e").replaceAll("&f", "§f");
			line2 = line2.replaceAll("&1", "§1").replaceAll("&2", "§2").replaceAll("&3", "§3").replaceAll("&4", "§4")
					.replaceAll("&5", "§5").replaceAll("&6", "§6").replaceAll("&7", "§7").replaceAll("&8", "§8")
					.replaceAll("&9", "§9").replaceAll("&0", "§0").replaceAll("&a", "§a").replaceAll("&b", "§b")
					.replaceAll("&c", "§c").replaceAll("&d", "§d").replaceAll("&e", "§e").replaceAll("&f", "§f");
			line3 = line3.replaceAll("&1", "§1").replaceAll("&2", "§2").replaceAll("&3", "§3").replaceAll("&4", "§4")
					.replaceAll("&5", "§5").replaceAll("&6", "§6").replaceAll("&7", "§7").replaceAll("&8", "§8")
					.replaceAll("&9", "§9").replaceAll("&0", "§0").replaceAll("&a", "§a").replaceAll("&b", "§b")
					.replaceAll("&c", "§c").replaceAll("&d", "§d").replaceAll("&e", "§e").replaceAll("&f", "§f");
			line4 = line4.replaceAll("&1", "§1").replaceAll("&2", "§2").replaceAll("&3", "§3").replaceAll("&4", "§4")
					.replaceAll("&5", "§5").replaceAll("&6", "§6").replaceAll("&7", "§7").replaceAll("&8", "§8")
					.replaceAll("&9", "§9").replaceAll("&0", "§0").replaceAll("&a", "§a").replaceAll("&b", "§b")
					.replaceAll("&c", "§c").replaceAll("&d", "§d").replaceAll("&e", "§e").replaceAll("&f", "§f");
		}
		if (p.hasPermission("main.sign.format") || p.hasPermission("main.sign.*")) {
			line1 = line1.replaceAll("&l", "§l").replaceAll("&m", "§m").replaceAll("&n", "§n").replaceAll("&o", "§o")
					.replaceAll("&m", "§m").replaceAll("&n", "§n");
			line2 = line2.replaceAll("&l", "§l").replaceAll("&m", "§m").replaceAll("&n", "§n").replaceAll("&o", "§o")
					.replaceAll("&m", "§m").replaceAll("&n", "§n");
			line3 = line3.replaceAll("&l", "§l").replaceAll("&m", "§m").replaceAll("&n", "§n").replaceAll("&o", "§o")
					.replaceAll("&m", "§m").replaceAll("&n", "§n");
			line4 = line4.replaceAll("&l", "§l").replaceAll("&m", "§m").replaceAll("&n", "§n").replaceAll("&o", "§o")
					.replaceAll("&m", "§m").replaceAll("&n", "§n");
		}
		if (p.hasPermission("main.sign.magic") || p.hasPermission("main.sign.*")) {
			line1 = line1.replaceAll("&k", "§k");
			line2 = line2.replaceAll("&k", "§k");
			line3 = line3.replaceAll("&k", "§k");
			line4 = line4.replaceAll("&k", "§k");
		}
	}

	public boolean isDay() {
		return line1.equals("§1§lDay");
	}

	public boolean isNight() {
		return line1.equals("§1§lNight");
	}

	public boolean isSun() {
		return line2.equals("§9sun") && line1.equals("§1§lWeather");
	}

	public boolean isStorm() {
		return line2.equals("§9storm") && line1.equals("§1§lWeather");
	}

	public boolean isAll() {
		return line2.equals("§9All") && line1.equals("§1§lRepair");
	}

	public boolean isHand() {
		return line2.equals("§9Hand") && line1.equals("§1§lRepair");
	}

	public boolean isSurvival() {
		return line2.equals("§9Survival") && line1.equals("§1§lGameMode");
	}

	public boolean isCreative() {
		return line2.equals("§9Creative") && line1.equals("§1§lGameMode");
	}

	public boolean isAdventure() {
		return line2.equals("§9Adventure") && line1.equals("§1§lGameMode");
	}

	public boolean isSpectator() {
		return line2.equals("§9Spectator") && line1.equals("§1§lGameMode");
	}

	public boolean isWarp() {
		return line1.equals("§1§lWarp");
	}

	public boolean isKit() {
		return line1.equals("§1§lKit");
	}

	public boolean isEnchant() {
		return line1.equals("§1§lEnchant");
	}

	public boolean isFree() {
		return line1.equals("§1§lFree");
	}

	public boolean isTrash() {
		return line1.equals("§1§lTrash");
	}

	public boolean isBuy() {
		return line1.equals("§1§lBuy");
	}

	public boolean isSell() {
		return line1.equals("§1§lSell");
	}

	public boolean isBalance() {
		return line1.equals("§1§lBalance");
	}

	public boolean isWeather() {
		return line1.equals("§1§lWeather");
	}

	public boolean isGameMode() {
		return line1.equals("§1§lGameMode");
	}

	public boolean isHeal() {
		return line1.equals("§1§lHeal");
	}

	public boolean isRepair() {
		return line1.equals("§1§lRepair");
	}

	public Warp getWarp() {
		return new Warp(line2);
	}

	public Kit getKit(Player p) {
		return new Kit(line2, p);
	}

	public String getEnchant() {
		return line2.substring(4, line2.length());
	}

	public Inventory getFree() {
		Inventory inv = Bukkit.createInventory(null, 54, "Free");
		for (int i = 0; i < inv.getSize(); i++) {
			ItemStack item = ItemPlugin.getItemString(line2);
			item.setAmount(item.getType().getMaxStackSize());
			inv.addItem(item);
		}
		return inv;
	}

	public Inventory getTrash() {
		return Bukkit.createInventory(null, 54, "Trash");
	}

	public ItemStack getItem() {
		ItemStack item = ItemPlugin.getItemString(line3.substring(2, line3.length()));
		item.setAmount(Integer.parseInt(line2.substring(2, line2.length())));
		return item;
	}

	public String getLine1() {
		return this.line1;
	}

	public String getLine2() {
		return this.line2;
	}

	public String getLine3() {
		return this.line3;
	}

	public String getLine4() {
		return this.line4;
	}

}