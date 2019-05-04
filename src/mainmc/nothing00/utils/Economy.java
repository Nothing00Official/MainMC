package mainmc.nothing00.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import mainmc.economy.EconomyAPI;
import mainmc.folders.Conf;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.functions.User;

public class Economy {

	private static HashMap<String, Double> hashTop = new HashMap<String, Double>();

	private String p;
	private double money;
	protected boolean addon;

	public Economy(Player p) {
		this.p = p.getName();
		this.money = 0;

		Conf config = new Conf();
		if (config.economyAddon() && MainPlugin.getEconomyAddon() != null && MainPlugin.getEconomyAddon().isEnabled()) {
			this.addon = true;
		} else {
			this.addon = false;
		}
	}

	public Economy(String p) {
		this.p = p;
		this.money = 0;

		Conf config = new Conf();
		if (config.economyAddon() && MainPlugin.getEconomyAddon() != null && MainPlugin.getEconomyAddon().isEnabled()) {
			this.addon = true;
		} else {
			this.addon = false;
		}
	}

	public Economy(Player p, double money) {
		this.p = p.getName();
		this.money = money;

		Conf config = new Conf();
		if (config.economyAddon() && MainPlugin.getEconomyAddon() != null && MainPlugin.getEconomyAddon().isEnabled()) {
			this.addon = true;
		} else {
			this.addon = false;
		}
	}

	public Economy(Player p, double money, boolean addon) {
		this.p = p.getName();
		this.money = money;
		this.addon = addon;
	}

	public Economy(String p, double money) {
		this.p = p;
		this.money = money;

		Conf config = new Conf();
		if (config.economyAddon() && MainPlugin.getEconomyAddon() != null && MainPlugin.getEconomyAddon().isEnabled()) {
			this.addon = true;
		} else {
			this.addon = false;
		}
	}

	public Economy(double money) {
		this.p = null;
		this.money = money;

		Conf config = new Conf();
		if (config.economyAddon() && MainPlugin.getEconomyAddon() != null && MainPlugin.getEconomyAddon().isEnabled()) {
			this.addon = true;
		} else {
			this.addon = false;
		}
	}

	public void setMoneyValue(double money) {
		this.money = money;
	}
	
	public double getMoneyValue() {
		return this.money;
	}

	public double getBalance() {
		if (!this.addon) {		
			return MainPlugin.getDataBase().getBalance(this.p);
		} else {
			EconomyAPI eco = new EconomyAPI(this.p, this.money);
			return eco.getBalance();
		}
	}

	public void addMoney() {
		if (!this.addon) {
			MainPlugin.getDataBase().setBalance(this.p, getBalance() + this.money);
		} else {
			EconomyAPI eco = new EconomyAPI(this.p, this.money);
			eco.addMoney();
		}
	}

	public void setMoney() {
		if (!this.addon) {
			MainPlugin.getDataBase().setBalance(this.p, this.money);
		} else {
			EconomyAPI eco = new EconomyAPI(this.p, this.money);
			eco.setMoney();
		}
	}

	public boolean detractable() {
		return getBalance() - this.money >= 0;
	}

	public void removeMoney() {
		if (!this.addon) {
			double money = 0;
			if (getBalance() - this.money >= 0)
				money = getBalance() - this.money;

			MainPlugin.getDataBase().setBalance(this.p, money);

		} else {
			EconomyAPI eco = new EconomyAPI(this.p, this.money);
			eco.removeMoney();
		}
	}

	public void takeAll() {
		if (!this.addon) {
			for (User user : User.getUsers()) {
				double m = 0;
				if (MainPlugin.getDataBase().getBalance(user.getName()) - this.money >= 0)
					m = MainPlugin.getDataBase().getBalance(user.getName()) - this.money;

				MainPlugin.getDataBase().setBalance(user.getName(), m);
			}
		} else {
			EconomyAPI eco = new EconomyAPI(this.p, this.money);
			eco.takeAll();
		}
	}

	public void giveAll() {
		if (!this.addon) {
			for (User user : User.getUsers()) {
				MainPlugin.getDataBase().setBalance(user.getName(), MainPlugin.getDataBase().getBalance(user.getName()) + this.money);
			}
		} else {
			EconomyAPI eco = new EconomyAPI(this.p, this.money);
			eco.addAll();
		}
	}

	public void setAll() {
		if (!this.addon) {
			for (User user : User.getUsers()) {
				MainPlugin.getDataBase().setBalance(user.getName(), this.money);
			}
		} else {
			EconomyAPI eco = new EconomyAPI(this.p, this.money);
			eco.setAll();
		}
	}

	public String toString() {
		Conf config = new Conf();
		DecimalFormat decimal = new DecimalFormat("#,###.##");
		return decimal.format(getBalance()) + config.getValuta();
	}

	public String getMoneyToString() {
		Conf config = new Conf();
		DecimalFormat decimal = new DecimalFormat("#,###.##");
		return decimal.format(this.money) + config.getValuta();
	}

	public String getUser() {
		return this.p;
	}

	public static String topToString(int size) {
		if (balTop().length < size) {
			String retur = "";
			String[] bal = balTop();
			for (int i = 0; i < balTop().length; i++) {
				Economy eco = new Economy(bal[i]);
				retur += bal[i] + " " + eco.toString() + "\n";
			}
			return retur;
		} else {
			String retur = "";
			String[] bal = balTop();
			for (int i = 0; i < size; i++) {
				Economy eco = new Economy(bal[i]);
				retur += bal[i] + " " + eco.toString() + "\n";
			}
			return retur;
		}

	}

	public static String[] balTop() {
		List<String> users = new ArrayList<String>();
		users.addAll(User.getUserList());
		hashTop.clear();
		for (String user : users) {
			Economy eco = new Economy(user);
			hashTop.put(user, eco.getBalance());
		}

		String[] array = new String[User.getUserList().size()];
		if (User.getUserList().isEmpty())
			return new String[0];
		if (User.getUserList().size() == 1) {
			array[0] = users.get(0);
			return array;
		}
		if (User.getUserList().size() >= 2) {

			for (int j = 0; j < array.length; j++) {
				String maxuser = users.get(0);
				for (int i = 1; i < users.size(); i++) {
					if (hashTop.get(users.get(i)) >= hashTop.get(maxuser)) {
						maxuser = users.get(i);
					}
				}
				users.remove(maxuser);
				array[j] = maxuser;
			}
			return array;
		}
		return new String[0];
	}

}