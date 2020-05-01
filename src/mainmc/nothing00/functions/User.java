package mainmc.nothing00.functions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import mainmc.folders.BanData;
import mainmc.folders.Conf;
import mainmc.folders.JailData;
import mainmc.folders.KickData;
import mainmc.folders.Messages;
import mainmc.folders.Motd;
import mainmc.folders.MuteData;
import mainmc.folders.SpawnData;
import mainmc.folders.UserData;
import mainmc.listener.DamageEvent;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.utils.Date;
import mainmc.nothing00.utils.PluginLoc;
import mainmc.nothing00.utils.Time;
import mainmc.nothing00.utils.punishments.BanType;
import mainmc.nothing00.utils.punishments.KickType;
import mainmc.nothing00.utils.punishments.PunishType;
import mainmc.nothing00.utils.punishments.Punishment;

public class User extends UserData {

	public static HashMap<String, User> userhash = new HashMap<String, User>();
	public static BanList bannedPlayers = Bukkit.getServer().getBanList(BanList.Type.NAME);

	private String user;
	private boolean vanish;
	private boolean tptoggle;
	private boolean god;
	private String playerReply;
	private Location loc;
	private Player tparequest;
	private boolean tpa;
	private String requestType;
	private Date joinDate;

	public User(String name) {
		super(name);
		this.user = name;
		this.vanish = false;
		this.tptoggle = false;
		this.god = false;
		this.playerReply = null;
		SpawnData spawn = new SpawnData();
		if (spawn.thereIsSpawn()) {
			this.loc = spawn.getSpawn();
		} else {
			this.loc = null;
		}
		this.tpa = false;
		this.tparequest = null;
		this.requestType = null;
		this.joinDate = new Date();
	}

	@SuppressWarnings("deprecation")
	public boolean hasItems(int number, Material type, int data) {
		int cont = 0;
		for (ItemStack item : this.getPlayer().getInventory().getContents()) {
			if (item == null)
				continue;
			if (item.getType().equals(type) && item.getData().getData() == data) {
				cont += item.getAmount();
			}
		}
		if (cont >= number)
			return true;
		return false;
	}

	@SuppressWarnings("deprecation")
	public void removeItems(int number, Material type, int data) {
		int cont = 0;
		for (ItemStack item : this.getPlayer().getInventory().getContents()) {
			if (item == null)
				continue;
			if (item.getType().equals(type) && item.getData().getData() == data) {
				cont += item.getAmount();
			}
		}
		if (cont == number) {
			for (int i = 0; i < this.getPlayer().getInventory().getSize(); i++) {
				if (this.getPlayer().getInventory().getItem(i) == null)
					continue;
				if (this.getPlayer().getInventory().getItem(i).getType().equals(type)
						&& this.getPlayer().getInventory().getItem(i).getData().getData() == data) {
					this.getPlayer().getInventory().setItem(i, null);
				}
			}
			this.getPlayer().updateInventory();
			return;
		}

		if (cont > number) {
			for (int i = 0; i < this.getPlayer().getInventory().getSize(); i++) {
				if (this.getPlayer().getInventory().getItem(i) == null)
					continue;
				if (this.getPlayer().getInventory().getItem(i).getType().equals(type)
						&& this.getPlayer().getInventory().getItem(i).getData().getData() == data) {
					this.getPlayer().getInventory().setItem(i, null);
				}
			}
			ItemStack item = new ItemStack(type, cont - number, (short) data);
			this.getPlayer().getInventory().addItem(item);
			this.getPlayer().updateInventory();
			return;
		}
	}

	public void launchFireBall() {
		this.getPlayer().launchProjectile(Fireball.class);
	}

	public boolean hasNightVision() {
		return this.getPlayer().hasPotionEffect(PotionEffectType.NIGHT_VISION);
	}

	public void setNightVision(boolean night) {
		if (!night && this.getPlayer().hasPotionEffect(PotionEffectType.NIGHT_VISION))
			this.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
		else if (night && !this.getPlayer().hasPotionEffect(PotionEffectType.NIGHT_VISION))
			this.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
	}

	public List<String> getNearPlayers(int radius) {
		List<String> list = new ArrayList<String>();
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.getName().equals(this.user))
				continue;
			if (p.getLocation().distance(this.getPlayer().getLocation()) <= radius)
				list.add(p.getName());
		}
		return list;
	}

	public boolean hasEmptyInventory() {
		boolean t = false;
		for (ItemStack item : this.getPlayer().getInventory().getContents()) {
			if (item != null && item.getType() != Material.AIR)
				t = true;
		}
		if (!t)
			return true;
		return false;
	}

	public void setJoinDate() {
		this.joinDate = new Date();
	}

	public void saveTime() {
		Time join = new Time(this.joinDate);
		Time quit = new Time(new Date());
		Long time = quit.getDateInSeconds() - join.getDateInSeconds();
		time += Long.parseLong(MainPlugin.getDataBase().getTime(this.user));
		MainPlugin.getDataBase().setTime(this.user, "" + time);
		this.joinDate = null;
	}

	public void updateTime() {
		if (!isOnline())
			return;

		Time join = new Time(this.joinDate);
		Time quit = new Time(new Date());
		Long time = quit.getDateInSeconds() - join.getDateInSeconds();
		time += Long.parseLong(MainPlugin.getDataBase().getTime(this.user));
		MainPlugin.getDataBase().setTime(this.user, "" + time);
		this.joinDate = new Date();
	}

	public int getOnlineTime() {
		updateTime();
		return Integer.parseInt(MainPlugin.getDataBase().getTime(this.user));
	}

	public void createBalance() {
		if (MainPlugin.getDataBase().hasBalance(this.user))
			return;
		Conf config = new Conf();
		MainPlugin.getDataBase().createBalance(this.user, config.startBalance());
	}

	public void banIfIpIsBanned() {
		Ip ip = new Ip(this.getPlayer().getAddress().getHostString());
		if (isBanned() && getLastBanMotivation().split(" ")[getLastBanMotivation().split(" ").length - 1]
				.equalsIgnoreCase("[IP]")) {
			ip.banIp(getLastBanMotivation().replaceAll(" [IP]", ""));
		}
		if (ip.isBanned()) {
			Conf config = new Conf();
			setBanned(true);
			setBanAuthor("CONSOLE");
			Punishment punish = new Punishment(this.user, "CONSOLE", config.getBanDefaultMotiv());
			punish.registerPunish(BanType.IP, PunishType.BAN, null);
			setLastBanMotivation(config.getBanDefaultMotiv());
			this.getPlayer().kickPlayer(config.getBanDefaultMotiv());
		}
	}

	public String lookup() {
		Messages msg = new Messages();
		Ip ip = new Ip(super.getCurrentIp());
		if (isBanned() || isMuted() || ip.isBanned()) {
			String mutetype = "", bantype = "", muteauthor = "", banauthor = "", mutemotivation = "", ipmotiv = "",
					banmotivation = "";
			if (isMuted() && getMuteExpire() != null) {
				mutetype = "Temp";
			} else if (isMuted()) {
				mutetype = "Perma";
			} else {
				mutetype = "none";
			}
			if (isBanned() && getBanExpire() != null) {
				bantype = "Temp";
			} else if (isMuted()) {
				bantype = "Perma";
			} else {
				bantype = "none";
			}
			if (isMuted()) {
				muteauthor = super.getString("userdata.mute-author");
			} else {
				muteauthor = "none";
			}
			if (isBanned()) {
				banauthor = super.getString("userdata.ban-author");
			} else {
				banauthor = "none";
			}
			if (isMuted()) {
				mutemotivation = getLastMuteMotivation();
			} else {
				mutemotivation = "none";
			}
			if (isBanned() && !ip.isBanned()) {
				banmotivation = getLastBanMotivation();
				ipmotiv = "none";
			} else if (ip.isBanned()) {
				banmotivation = "none";
				ipmotiv = getLastBanMotivation().replaceAll(" [IP]", "");
			} else {
				banmotivation = "none";
				ipmotiv = "none";
			}
			return msg.getLookupMessage().replaceAll("%player%", this.user).replaceAll("%ismuted%", "" + isMuted())
					.replaceAll("%muteauthor%", muteauthor).replaceAll("%mutemotivation%", mutemotivation)
					.replaceAll("%isbanned%", isBanned() + "").replaceAll("%mutetype%", "" + mutetype)
					.replaceAll("%banauthor%", banauthor).replaceAll("%banmotivation%", banmotivation)
					.replaceAll("%bantype%", bantype).replaceAll("%isIP%", ip.isBanned() + "")
					.replaceAll("%banipmotivation%", ipmotiv);
		}
		return msg.getMessage("NoLookup");
	}

	public String history() {
		Messages msg = new Messages();
		Conf config = new Conf();
		if (!config.thereIsHistory())
			return msg.getMessage("NoHistory");
		int totalCont = 0, kickCont = 0, muteCont = 0, banCont = 0;
		String dir = "";
		if (config.punishDivided()) {
			KickData kick = new KickData(this.user);
			MuteData mute = new MuteData(this.user);
			BanData ban = new BanData(this.user);
			totalCont = kick.getCount() + mute.getCount() + ban.getCount();
			kickCont = kick.getCount();
			muteCont = mute.getCount();
			banCont = ban.getCount();
			dir = kick.getFile().getAbsolutePath();
		} else {
			Punishment punish = new Punishment(this.user, null, null);
			totalCont = punish.getKickCount() + punish.getBanCount() + punish.getMuteCount();
			kickCont = punish.getKickCount();
			muteCont = punish.getMuteCount();
			banCont = punish.getBanCount();
			dir = punish.getFile().getAbsolutePath();
		}
		return msg.getHistoryessage().replaceAll("%kick%", kickCont + "").replaceAll("%mute%", muteCont + "")
				.replaceAll("%ban%", banCont + "").replaceAll("%tot%", totalCont + "")
				.replaceAll("%jail%", "" + isJailed()).replaceAll("%player%", this.user).replaceAll("%dir%", dir);

	}

	public boolean isOnline() {
		return Bukkit.getServer().getPlayer(this.user) != null && User.getUser(this.user) != null;
	}

	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(this.user);
	}

	@SuppressWarnings("deprecation")
	public OfflinePlayer getOffline() {
		return Bukkit.getServer().getOfflinePlayer(this.user);
	}

	public void setGamemode(GameMode gm) {
		this.getPlayer().setGameMode(gm);
	}

	public boolean hasFly() {
		return this.getPlayer().getAllowFlight();
	}

	public void setFly(boolean fly) {
		this.getPlayer().setAllowFlight(fly);
	}

	public boolean hasVanish() {
		return this.vanish;
	}

	public void setVanish(boolean vanish) {
		this.vanish = vanish;
		if (!vanish) {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				p.showPlayer(this.getPlayer());
			}
		} else {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				p.hidePlayer(this.getPlayer());
			}
		}
	}

	public void kick(String motiv, String sender) {
		this.getPlayer().kickPlayer(motiv);
		Punishment punish = new Punishment(this.user, sender, motiv);
		punish.registerPunish(KickType.NORMAL, PunishType.KICK, null);
	}

	public void addItem(ItemStack item) {
		HashMap<Integer, ItemStack> leftOver = new HashMap<Integer, ItemStack>();
		leftOver.putAll((this.getPlayer().getInventory().addItem(item)));

		if (!leftOver.isEmpty()) {
			Location loc = this.getPlayer().getLocation();
			this.getPlayer().getWorld().dropItem(loc, item);
		}
	}

	public int getExp() {
		return this.getPlayer().getTotalExperience();
	}

	public void setExp(int exp) {
		this.getPlayer().setTotalExperience(exp);
	}

	public boolean isToggled() {
		return this.tptoggle;
	}

	public void setToggled(boolean tp) {
		this.tptoggle = tp;
	}

	public boolean teleported(Player p) {
		if (this.tptoggle)
			return false;
		User.getUser(this.user).saveLastLocation();
		this.getPlayer().teleport(p);
		Conf config = new Conf();
		if (config.teleportSafe()) {
			DamageEvent.safe.add(this.user);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin.plugin, new Runnable() {
				@Override
				public void run() {
					if (DamageEvent.safe.contains(user)) {
						DamageEvent.safe.remove(user);
					}
				}
			}, config.safeTime() * 20);
		}
		return true;
	}

	public void onLogin() {
		Conf config = new Conf();
		DamageEvent.safe.add(this.user);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin.plugin, new Runnable() {
			@Override
			public void run() {
				if (DamageEvent.safe.contains(user)) {
					DamageEvent.safe.remove(user);
				}
			}
		}, config.loginAttackDelay() * 20);
	}

	public boolean teleported(Location loc) {
		if (this.tptoggle)
			return false;
		User.getUser(this.user).saveLastLocation();
		this.getPlayer().teleport(loc);
		Conf config = new Conf();
		if (config.teleportSafe()) {
			DamageEvent.safe.add(this.user);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin.plugin, new Runnable() {
				@Override
				public void run() {
					if (DamageEvent.safe.contains(user)) {
						DamageEvent.safe.remove(user);
					}
				}
			}, config.safeTime() * 20);
		}
		return true;
	}

	public void setHealth(double h) {
		this.getPlayer().setHealth(h);
	}

	public void onTpall() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (User.getUser(p.getName()).isToggled())
				continue;
			p.teleport(this.getPlayer());
		}
	}

	public void setMute(boolean mute) {
		MainPlugin.getDataBase().setMuted(this.user, mute);
		if (!mute) {
			MainPlugin.getDataBase().removeTempMute(this.user);
			super.get().set("userdata.mute-author", null);
			super.get().set("userdata.last-mute", null);
			super.save();
		}
	}

	public boolean isMuted() {
		return MainPlugin.getDataBase().getMuted(this.user);
	}

	public void setBanned(boolean ban) {
		MainPlugin.getDataBase().setBanned(this.user, ban);
		if (!ban) {
			MainPlugin.getDataBase().removeTempBan(this.user);
			super.get().set("userdata.last-ban", null);
			super.get().set("userdata.ban-author", null);
			super.save();
			bannedPlayers.pardon(this.user);
			return;
		}
		bannedPlayers.addBan(this.user, null, null, null);
	}

	public void setSocialspy(boolean social) {
		MainPlugin.getDataBase().setSocialspy(this.user, social);
	}

	public boolean hasSocialspy() {
		return MainPlugin.getDataBase().getSocialspy(this.user);
	}

	public boolean isBanned() {
		return MainPlugin.getDataBase().getBanned(this.user);
	}

	public String getName() {
		return this.user;
	}

	public boolean isGod() {
		return this.god;
	}

	public void setGod(boolean god) {
		this.god = god;
	}

	public void newUser() {
		if (!fileExists()) {
			try {
				super.yml.createNewFile();
				super.get().createSection("userdata");
				super.get().set("userdata.username", this.user);
				super.get().set("userdata.UUID", this.getPlayer().getUniqueId().toString());
				super.get().set("userdata.IP",
						this.getPlayer().getAddress().getHostString() + ":" + this.getPlayer().getAddress().getPort());
				super.get().set("userdata.firstlogin",
						LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonthValue() + "/"
								+ LocalDate.now().getYear() + " " + LocalTime.now().getHour() + ":"
								+ LocalTime.now().getMinute());
				super.get().set("userdata.others-ips", new ArrayList<String>());
				super.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		MainPlugin.getDataBase().setTime(this.user, "0");
		if (!MainPlugin.getDataBase().exists(this.getPlayer().getName()))
			MainPlugin.getDataBase().createuser(this.getPlayer().getName(), this.getPlayer().getUniqueId().toString(),
					false, false, false);
	}

	public static void vanish(Player p) {
		for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
			if (User.getUser(pl.getName()).hasVanish()) {
				p.hidePlayer(pl);
			}
		}
	}

	public static User getUser(String user) {
		return userhash.get(user.toLowerCase());
	}

	public String getHostComplete() {
		if (this.isOnline()) {
			return this.getPlayer().getAddress().getHostString() + ":" + this.getPlayer().getAddress().getPort();
		} else {
			return super.getCurrentHost();
		}
	}

	public void setItem(ItemStack item) {
		this.getPlayer().getInventory().setItemInHand(item);
		this.getPlayer().updateInventory();
	}

	public void setItem(ItemStack item, int pos) {
		this.getPlayer().getInventory().setItem(pos, item);
		this.getPlayer().updateInventory();
	}

	public ItemStack getItem() {
		return this.getPlayer().getItemInHand();
	}

	public ItemStack getItem(int pos) {
		return this.getPlayer().getInventory().getItem(pos);
	}

	public void clear() {
		this.getPlayer().getInventory().clear();
		this.getPlayer().getInventory().setChestplate(null);
		this.getPlayer().getInventory().setBoots(null);
		this.getPlayer().getInventory().setLeggings(null);
		this.getPlayer().getInventory().setHelmet(null);
		this.getPlayer().updateInventory();
	}

	public void sendMessage(String str) {
		this.getPlayer().sendMessage(str);
	}

	public boolean isUntouch() {
		return this.getPlayer() != null && this.getPlayer().hasPermission("main.untouchable");
	}

	public void feed() {
		this.getPlayer().setFoodLevel(20);
	}

	public void heal() {
		this.getPlayer().setHealth(20);
		this.getPlayer().setFoodLevel(20);
	}

	public boolean setHat(ItemStack item) {
		ItemPlugin it = new ItemPlugin(item);
		if (it.isHat()) {
			this.getPlayer().getInventory().setHelmet(item);
			this.setItem(null);
			this.getPlayer().updateInventory();
			return true;
		}
		return false;
	}

	public void removeHat() {
		this.addItem(this.getPlayer().getInventory().getHelmet());
		this.getPlayer().getInventory().setHelmet(null);
		this.getPlayer().updateInventory();
	}

	public void setWalkSpeed(int val) {

		float v = 0;

		switch (val) {

		case 0:
			v = (float) 0.0;
			break;
		case 1:
			v = (float) 0.2;
			break;
		case 2:
			v = (float) 0.3;
			break;
		case 3:
			v = (float) 0.4;
			break;
		case 4:
			v = (float) 0.5;
			break;
		case 5:
			v = (float) 0.6;
			break;
		case 6:
			v = (float) 0.7;
			break;
		case 7:
			v = (float) 0.8;
			break;
		case 8:
			v = (float) 0.9;
			break;
		case 9:
			v = (float) 0.95;
			break;
		case 10:
			v = (float) 1.0;
			break;
		default:
			v = (float) 0.2;
			break;
		}

		this.getPlayer().setWalkSpeed(v);
	}

	public void setFlySpeed(int val) {

		float v = 0;

		switch (val) {

		case 0:
			v = (float) 0.0;
			break;
		case 1:
			v = (float) 0.2;
			break;
		case 2:
			v = (float) 0.3;
			break;
		case 3:
			v = (float) 0.4;
			break;
		case 4:
			v = (float) 0.5;
			break;
		case 5:
			v = (float) 0.6;
			break;
		case 6:
			v = (float) 0.7;
			break;
		case 7:
			v = (float) 0.8;
			break;
		case 8:
			v = (float) 0.9;
			break;
		case 9:
			v = (float) 0.95;
			break;
		case 10:
			v = (float) 1.0;
			break;
		default:
			v = (float) 0.2;
			break;
		}

		this.getPlayer().setFlySpeed(v);
	}

	public void openCraft() {
		this.getPlayer().openWorkbench(this.getPlayer().getLocation(), true);
	}

	public Player getReply() {
		return Bukkit.getPlayer(this.playerReply);
	}

	public void setReply(String name) {
		this.playerReply = name;
	}

	public void saveLastLocation() {
		this.loc = this.getPlayer().getLocation();
	}

	public Location getLastLocation() {
		return this.loc;
	}

	public boolean hasTpaRequest() {
		return this.tparequest != null;
	}

	public boolean askTpa() {
		return this.tpa;
	}

	public void setRequest(Player from) {
		this.tparequest = from;
	}

	public void setSendTpa(boolean tpa) {
		this.tpa = tpa;
	}

	public Player getRequest() {
		return this.tparequest;
	}

	public void setRequestType(int type) {
		switch (type) {
		case 0:
			this.requestType = "TPA";
			break;
		case 1:
			this.requestType = "TPAHERE";
			break;
		default:
			this.requestType = null;
			break;
		}
	}

	public String getRequestType() {
		return this.requestType;
	}

	public boolean hasNickname() {
		return MainPlugin.getDataBase().hasNick(this.user);
	}

	public void setNickname(String nick) {
		if (MainPlugin.getDataBase().hasNick(this.user)) {
			MainPlugin.getDataBase().setNick(this.user, nick);
		} else {
			MainPlugin.getDataBase().insertNick(this.user, nick);
		}
	}

	public String getNickname() {
		return MainPlugin.getDataBase().getNick(this.user);
	}

	public void removeNickname() {
		MainPlugin.getDataBase().removeNick(this.user);
	}

	public void showMotd() {
		Motd motd = new Motd();

		this.getPlayer()
				.sendMessage(motd.showMotd().replaceAll("&", "§").replace("{player}", this.getName())
						.replace("{displayname}", this.getPlayer().getDisplayName())
						.replace("{onlineplayers}", Bukkit.getOnlinePlayers().size() + "")
						.replace("{day}", "" + LocalDate.now().getDayOfMonth())
						.replace("{time}", LocalTime.now().getHour() + ":" + LocalTime.now().getMinute())
						.replace("{month}", "" + LocalDate.now().getMonthValue())
						.replace("{year}", "" + LocalDate.now().getYear()).replace("{listallplayers}", ""));
		if (motd.showMotd().contains("{listallplayers}")) {
			if (this.getPlayer().hasPermission("main.list.viewall")) {
				this.getPlayer().sendMessage(User.viewOpList());
			} else {
				this.getPlayer().sendMessage(User.viewList());
			}
		}
	}

	public int getHomeLimit() {
		Conf config = new Conf();
		if (this.getPlayer().isOp())
			return -1;
		if (this.getPlayer().hasPermission("main.sethome.vip") && this.getPlayer().hasPermission("main.sethome.staff"))
			return -1;
		if (this.getPlayer().hasPermission("main.sethome.vip"))
			return config.getVipHomeLimit();
		if (this.getPlayer().hasPermission("main.sethome.staff"))
			return config.getStaffHomeLimit();
		return config.getHomeLimit();
	}

	public Date getMuteExpire() {
		if (!MainPlugin.getDataBase().isTempMuted(this.user))
			return null;
		return new Date(MainPlugin.getDataBase().getMuteExpire(this.user));
	}

	public Date getBanExpire() {
		if (!MainPlugin.getDataBase().isTempBanned(this.user))
			return null;
		return new Date(MainPlugin.getDataBase().getBanExpire(this.user));
	}

	public void setMuteExpire(Date date) {
		MainPlugin.getDataBase().setExpire(this.user, date.toString());
	}

	public void setBanExpire(Date date) {
		MainPlugin.getDataBase().setBanExpire(this.user, date.toString());
	}

	public void setLastBanMotivation(String motiv) {
		super.get().set("userdata.last-ban", motiv.replaceAll(" ", "_"));
		super.save();
	}

	public void setLastMuteMotivation(String motiv) {
		super.get().set("userdata.last-mute", motiv.replaceAll(" ", "_"));
		super.save();
	}

	public void setBanAuthor(String author) {
		super.get().set("userdata.ban-author", author);
		super.save();
	}

	public void setMuteAuthor(String author) {
		super.get().set("userdata.mute-author", author);
		super.save();
	}

	public String getLastBanMotivation() {
		return super.getString("userdata.last-ban").replaceAll("&", "§").replaceAll("_", " ");
	}

	public String getLastMuteMotivation() {
		return super.getString("userdata.last-mute").replaceAll("&", "§").replaceAll("_", " ");
	}

	public void saveQuitLocation() {
		PluginLoc loc = new PluginLoc(this.getPlayer().getLocation());
		super.get().set("userdata.lastlocation", loc.toString() + " " + loc.getWorld());
		super.save();
	}

	public double getPing() {
		try {
			double ping = 0;
			Class<?> craftPlayer = Class
					.forName("org.bukkit.craftbukkit." + MainPlugin.getServerVersion() + ".entity.CraftPlayer");
			Object converted = craftPlayer.cast(this.getPlayer());
			java.lang.reflect.Method handle = converted.getClass().getMethod("getHandle", new Class[0]);
			Object entityPlayer = handle.invoke(converted, new Object[0]);
			Field pingField = entityPlayer.getClass().getField("ping");
			ping = pingField.getDouble(entityPlayer);
			return ping;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean isJailed() {
		return MainPlugin.getDataBase().isJailed(this.user);
	}

	public void setJails(JailData jail) {
		this.getPlayer().teleport(jail.getLocation());
		MainPlugin.getDataBase().setJail(this.user, jail.getName());
	}

	public void unJail() {
		SpawnData spawn = new SpawnData();
		if (spawn.thereIsSpawn())
			this.getPlayer().teleport(spawn.getSpawn());
		MainPlugin.getDataBase().removeJail(this.user);
	}

	public String getJail() {
		return MainPlugin.getDataBase().getJail(this.user);
	}

	public boolean hasKeyword() {
		return MainPlugin.getDataBase().hasKeyword(this.getPlayer().getUniqueId().toString());
	}

	public void setKeyword(String key) {
		MainPlugin.getDataBase().setLockAccount(this.getPlayer().getUniqueId().toString(), key);
	}

	public void updateKeyword(String key) {
		MainPlugin.getDataBase().updateKeyword(this.getPlayer().getUniqueId().toString(), key);
	}

	public void deleteKeyword() {
		MainPlugin.getDataBase().deleteKeyword(this.getPlayer().getUniqueId().toString());
	}

	public void setLocked(boolean locked) {
		MainPlugin.getDataBase().setLocked(this.getPlayer().getUniqueId().toString(), locked);
	}

	public boolean isLocked() {
		if (!hasKeyword())
			return false;
		return MainPlugin.getDataBase().isLocked(this.getPlayer().getUniqueId().toString());
	}

	public String getKeyword() {
		return MainPlugin.getDataBase().getKyword(this.getPlayer().getUniqueId().toString());
	}

	public static void sendAllMessage(String str) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(str);
		}
	}

	public static void kickAll(String motiv, String sender) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().equalsIgnoreCase(sender))
				continue;
			User user = new User(p.getName());
			if (user.isUntouch())
				continue;
			p.kickPlayer(motiv);
			Punishment punish = new Punishment(p.getName(), sender, motiv);
			punish.registerPunish(KickType.ALL, PunishType.KICK, null);
		}
	}

	public static String viewOpList() {
		Messages msg = new Messages();
		List<String> list = new ArrayList<String>();
		int nvanish = 0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (User.getUser(p.getName()).hasVanish()) {
				list.add("§c" + p.getName());
				nvanish++;
			} else {
				list.add("§e" + p.getName());
			}
		}
		return msg.getMessage("OpList").replaceAll("%count%", Bukkit.getOnlinePlayers().size() + "")
				.replaceAll("%vanishcount%", nvanish + "") + "\n" + String.join("§7, ", list.toArray(new String[0]));
	}

	public static String viewList() {
		Messages msg = new Messages();
		List<String> list = new ArrayList<String>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (User.getUser(p.getName()).hasVanish()) {
				continue;
			} else {
				list.add("§e" + p.getName());
			}
		}
		return msg.getMessage("List").replaceAll("%count%", Bukkit.getOnlinePlayers().size() + "") + "\n"
				+ String.join("§7, ", list.toArray(new String[0]));
	}

	public static List<String> getUserList() {
		List<String> fileList = new ArrayList<String>();
		File userdir = new File(MainPlugin.getInstance().getDataFolder() + "/data/users/");
		for (File f : userdir.listFiles()) {
			fileList.add(f.getName().replaceAll(".yml", ""));
		}
		return fileList;
	}

	public static List<User> getUsers() {
		List<User> fileList = new ArrayList<User>();
		File userdir = new File(MainPlugin.getInstance().getDataFolder() + "/data/users/");
		for (File f : userdir.listFiles()) {
			fileList.add(new User(f.getName().replaceAll(".yml", "")));
		}
		return fileList;
	}

	public static void tpaManager(User sender, User to) {
		Conf config = new Conf();
		Messages msg = new Messages();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin.plugin, new Runnable() {
			@Override
			public void run() {
				if (sender.isOnline() && to.isOnline()) {
					if (User.getUser(sender.getName()).askTpa()) {
						User.getUser(sender.getName()).setSendTpa(false);
						User.getUser(to.getName()).setRequest(null);
						User.getUser(to.getName()).setRequestType(2);
						sender.sendMessage(msg.getMessage("tpaExpired"));
					}
				} else {
					User.getUser(sender.getName()).setSendTpa(false);
					User.getUser(to.getName()).setRequest(null);
					;
				}
			}
		}, config.getTpaExipre() * 20);
	}
}
