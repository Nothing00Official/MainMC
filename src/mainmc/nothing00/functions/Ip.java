package mainmc.nothing00.functions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.utils.punishments.BanType;
import mainmc.nothing00.utils.punishments.PunishType;
import mainmc.nothing00.utils.punishments.Punishment;

public class Ip {

	private String address;

	public Ip(String address) {
		this.address = address;
	}

	public String toString() {
		return this.address;
	}

	public void banIp(String motiv) {
		MainPlugin.getDataBase().banIp(this.address);
		banAllPlayersWithThisAddress(motiv);
	}

	public List<String> getUsersWithThisAddress() {
		List<String> users = new ArrayList<String>();
		for (String user : User.getUserList()) {
			User u = new User(user);
			if (u.getCurrentHost().split(":")[0].equals(this.address)) {
				users.add(u.getName());
			}
		}
		return users;
	}

	public void unBanIp() {
		MainPlugin.getDataBase().unBanIp(this.address);
	}

	public boolean isBanned() {
		return MainPlugin.getDataBase().isBannedIp(this.address);
	}

	public boolean isAddress() {
		String[] split = getBytes();
		if (split.length != 4)
			return false;
		return NumberUtils.isNumber(split[0]) && NumberUtils.isNumber(split[1]) && NumberUtils.isNumber(split[2])
				&& NumberUtils.isNumber(split[3]);

	}

	public boolean isValidAddress() {
		String[] split = getBytes();
		if (split.length != 4)
			return false;
		
		return NumberUtils.isNumber(split[0]) && NumberUtils.isNumber(split[1]) && NumberUtils.isNumber(split[2])
				&& NumberUtils.isNumber(split[3]) && Integer.parseInt(split[0]) <= 256
				&& Integer.parseInt(split[1]) <= 256 && Integer.parseInt(split[2]) <= 256
				&& Integer.parseInt(split[3]) <= 256;

	}

	private String[] getBytes() {
		int[] dots = new int[3];
		int cont = 0;
		char[] car = this.address.toCharArray();
		for (int i = 0; i < car.length; i++) {
			if (car[i] == '.') {
				dots[cont] = i;
				cont++;
			}
		}
		if (cont != 3)
			return new String[0];
		String[] bytes = {
				this.address.substring(0, dots[0]), 
				this.address.substring(dots[0]+1, dots[1]), 		
				this.address.substring(dots[1]+1, dots[2]),
				this.address.substring(dots[2]+1, this.address.length()), 
		};
		
		return bytes;
	}

	public void banAllPlayersWithThisAddress(String motiv) {
		for (String user : User.getUserList()) {
			User u = new User(user);
			if (u.getCurrentHost().split(":")[0].equals(this.address)) {
				if (u.isBanned())
					continue;
				u.setBanned(true);
				u.setBanAuthor("hidden");
				Punishment punish = new Punishment(u.getName(), "hidden", motiv);
				punish.registerPunish(BanType.IP, PunishType.BAN, null);
				u.setLastBanMotivation(motiv.replaceAll("§", "&").replaceAll(" ", "_") + " [IP]");
				if (u.isOnline()) {
					u.getPlayer().kickPlayer(motiv);
				}
			}
			List<String> ips = u.getOldAdresses();
			for (String ip : ips) {
				if (ip.split(":")[0].equals(this.address)) {
					if (u.isBanned())
						continue;
					u.setBanned(true);
					u.setBanAuthor("hidden");
					Punishment punish = new Punishment(u.getName(), "hidden", motiv);
					punish.registerPunish(BanType.IP, PunishType.BAN, null);
					u.setLastBanMotivation(motiv.replaceAll("§", "&").replaceAll(" ", "_") + " [IP]");
					if (u.isOnline()) {
						u.getPlayer().kickPlayer(motiv);
					}
				}
				Ip add = new Ip(ip);
				add.banIp(motiv);
			}
		}
	}

}
