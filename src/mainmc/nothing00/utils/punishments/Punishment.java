package mainmc.nothing00.utils.punishments;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mainmc.folders.BanData;
import mainmc.folders.Conf;
import mainmc.folders.Config;
import mainmc.folders.KickData;
import mainmc.folders.MuteData;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.utils.Date;
import mainmc.nothing00.utils.Time;

public class Punishment {

	private String victim;
	private String author;
	private String motiv;

	public Punishment(String victim, String author, String motiv) {
		this.victim = victim;
		this.author = author;
		this.motiv = motiv;
	}

	public void registerPunish(Punish punishtype, PunishType type, String time) {
		this.motiv = this.motiv.replaceAll(" ", "_").replaceAll("§", "&");
		Conf config = new Conf();

		String pt = null;
		if (time != null && punishtype.toString().equals("TEMPORARILY"))
			pt = punishtype.toString() + "_" + time;
		else
			pt = punishtype.toString();

		if (config.thereIsHistory()) {

			String punish = pt + " " + LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonthValue() + "/"
					+ LocalDate.now().getYear() + " " + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute()
					+ " " + this.motiv + " " + this.author;
			if (config.punishDivided()) {
				if (type.equals(PunishType.MUTE)) {

					MuteData mute = new MuteData();

					List<String> mutelist = new ArrayList<String>();

					if (mute.getStringList("Mutes." + this.victim) == null) {
						mutelist.add(punish);
						mute.get().set("Mutes." + this.victim, mutelist);
					} else {
						mutelist = mute.getStringList("Mutes." + this.victim);
						mutelist.add(punish);
						mute.get().set("Mutes." + this.victim, mutelist);
					}
					mute.save();

				} else if (type.equals(PunishType.KICK)) {
					KickData kick = new KickData();

					List<String> kicklist = new ArrayList<String>();

					if (kick.getStringList("Kicks." + this.victim) == null) {
						kicklist.add(punish);
						kick.get().set("Kicks." + this.victim, kicklist);
					} else {
						kicklist = kick.getStringList("Kicks." + this.victim);
						kicklist.add(punish);
						kick.get().set("Kicks." + this.victim, kicklist);
					}
					kick.save();
				} else if (type.equals(PunishType.BAN)) {
					BanData ban = new BanData();

					List<String> banlist = new ArrayList<String>();

					if (ban.getStringList("Bans." + this.victim) == null) {
						banlist.add(punish);
						ban.get().set("Bans." + this.victim, banlist);
					} else {
						banlist = ban.getStringList("Bans." + this.victim);
						banlist.add(punish);
						ban.get().set("Bans." + this.victim, banlist);
					}
					ban.save();
				}
			} else {

				Config history = new Config(
						new File(MainPlugin.getInstance().getDataFolder() + "/data/punishment-history.yml"));

				List<String> punishes = new ArrayList<String>();

				if (history.getStringList("History." + this.victim) == null) {
					punish = punish + " (" + type.toString() + ")";
					punishes.add(punish);
					history.get().set("History." + this.victim, punishes);
				} else {
					punishes = history.getStringList("History." + this.victim);
					punish = punish + " (" + type.toString() + ")";
					punishes.add(punish);
					history.get().set("History." + this.victim, punishes);
				}
				history.save();

			}

		}
	}

	public File getFile() {
		File file = new File(MainPlugin.getInstance().getDataFolder() + "/data/punishment-history.yml");
		return file;
	}

	public static void createFile() {
		File file = new File(MainPlugin.getInstance().getDataFolder() + "/data/punishment-history.yml");
		Config history = new Config(
				new File(MainPlugin.getInstance().getDataFolder() + "/data/punishment-history.yml"));
		if (!file.exists()) {
			try {
				file.createNewFile();
				history.get().createSection("History");
				history.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getKickCount() {
		Config history = new Config(
				new File(MainPlugin.getInstance().getDataFolder() + "/data/punishment-history.yml"));
		if (history.get().get("History." + this.victim) != null
				&& !history.getStringList("History." + this.victim).isEmpty()) {
			int cont = 0;
			for (String punish : history.getStringList("History." + this.victim)) {
				if (punish.contains("(KICK)"))
					cont++;
			}
			return cont;
		}
		return 0;
	}

	public int getMuteCount() {
		Config history = new Config(
				new File(MainPlugin.getInstance().getDataFolder() + "/data/punishment-history.yml"));
		if (history.get().get("History." + this.victim) != null
				&& !history.getStringList("History." + this.victim).isEmpty()) {
			int cont = 0;
			for (String punish : history.getStringList("History." + this.victim)) {
				if (punish.contains("(MUTE)"))
					cont++;
			}
			return cont;
		}
		return 0;
	}

	public int getBanCount() {
		Config history = new Config(
				new File(MainPlugin.getInstance().getDataFolder() + "/data/punishment-history.yml"));
		if (history.get().get("History." + this.victim) != null
				&& !history.getStringList("History." + this.victim).isEmpty()) {
			int cont = 0;
			for (String punish : history.getStringList("History." + this.victim)) {
				if (punish.contains("(BAN)"))
					cont++;
			}
			return cont;
		}
		return 0;
	}

	public static void exporting() {

		Config history = new Config(
				new File(MainPlugin.getInstance().getDataFolder() + "/data/punishment-history.yml"));

		if (!history.getFile().exists())
			return;

		Set<String> list = history.getConfiguration("History");
		String[] user = list.toArray(new String[0]);

		KickData kick = new KickData();
		kick.onCreate();
		MuteData mute = new MuteData();
		mute.onCreate();
		BanData ban = new BanData();
		ban.onCreate();

		for (int i = 0; i < user.length; i++) {
			List<String> punish = history.getStringList("History." + user[i]);
			String[] array = punish.toArray(new String[0]);
			for (int j = 0; j < array.length; j++) {
				String[] split = array[j].split(" ");
				if (split[5].equals("(KICK)")) {
					List<String> kicked = new ArrayList<String>();
					array[j] = array[j].replace(" (KICK)", "");
					if (kick.get().getStringList("Kicks." + user[i]) != null) {
						kicked = kick.getStringList("Kicks." + user[i]);
						kicked.add(array[j]);
						kick.set("Kicks." + user[i], kicked);
					} else {
						kicked.add(array[j]);
						kick.set("Kicks." + user[i], kicked);
					}
					kick.save();
				}
				if (split[5].equals("(MUTE)")) {
					List<String> muted = new ArrayList<String>();
					array[j] = array[j].replace(" (MUTE)", "");
					if (mute.get().getStringList("Mutes." + user[i]) != null) {
						muted = mute.getStringList("Mutes." + user[i]);
						muted.add(array[j]);
						mute.set("Mutes." + user[i], muted);
					} else {
						muted.add(array[j]);
						mute.set("Mutes." + user[i], muted);
					}
					mute.save();
				}
				if (split[5].equals("(BAN)")) {
					List<String> banned = new ArrayList<String>();
					array[j] = array[j].replace(" (BAN)", "");
					if (ban.get().getStringList("Bans." + user[i]) != null) {
						banned = mute.getStringList("Bans." + user[i]);
						banned.add(array[j]);
						ban.set("Bans." + user[i], banned);
					} else {
						banned.add(array[j]);
						ban.set("Bans." + user[i], banned);
					}
					ban.save();
				}
			}
		}
		history.delete();
		System.out.println("exporting complete!");
	}

	public static Date getPunishTime(String t, PunishType type) {

		if (!Time.isTimeFormat(t))
			return null;
        
		Time time = new Time(t);
		Date pena = new Date();
		Time timenow = new Time(Time.getToDay());
	
		if(type.equals(PunishType.MUTE)) {
			if (time.isDay()) {
				Time timeday = new Time(time.toString(), null, null, null, null, null);
				int day = timeday.translateDay();
				pena = timenow.addTime(day, 0, 0, 0, 0, 0);
			} else if (time.isHour()) {
				Time timehour = new Time(null, null, null, time.toString(), null, null);
				int hour = timehour.translateHour();
				pena = timenow.addTime(0, 0, 0, hour, 0, 0);
			} else if (time.isMinute()) {
				Time timeminute = new Time(null, null, null, null, time.toString(), null);
				int minute = timeminute.translateMinutes();
				pena = timenow.addTime(0, 0, 0, 0, minute, 0);
			} else if (time.isSecond()) {
				Time timesecond = new Time(null, null, null, null, null, time.toString());
				int second = timesecond.translateSeconds();
				pena = timenow.addTime(0, 0, 0, 0, 0, second);
			} else {
				return null;
			}
		}else if(type.equals(PunishType.BAN)) {
			if (time.isDay()) {
				Time timeday = new Time(time.toString(), null, null, null, null, null);
				int day = timeday.translateDay();
				pena = timenow.addTime(day, 0, 0, 0, 0, 0);
			} else if (time.isHour()) {
				Time timehour = new Time(null, null, null, time.toString(), null, null);
				int hour = timehour.translateHour();
				pena = timenow.addTime(0, 0, 0, hour, 0, 0);
			} else if (time.isYear()) {
				Time timemyear = new Time(null, null, time.toString(), null, null, null);
				int year = timemyear.translateYear();
				pena = timenow.addTime(0, 0, year, 0, 0, 0);
			} else {
				return null;
			}
		}
		
		return pena;
	}

}
