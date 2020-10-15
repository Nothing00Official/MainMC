package mainmc.nothing00.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.PermissionsPlus;
import mainmc.nothing00.functions.User;


public class ChatManager {

	private Player p;
	private String msg;

	public ChatManager(Player p, String msg) {
		this.p = p;
		this.msg = msg;
	}

	public ChatManager(Player p) {
		this.p = p;
		this.msg = null;
	}

	public ChatManager(String msg) {
		this.p = null;
		this.msg = msg;
	}

	public int sendToStaff() {
		Messages msg = new Messages();
		int staff = 0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!p.hasPermission("main.helpstaff.receive"))
				continue;
			p.sendMessage(msg.getMessage("HelpStaff").replaceAll("%message%", this.msg).replaceAll("%player%",
					this.p.getName()));
			staff++;
		}
		return staff;
	}

	public void sendPrivateMessage(Player to) {
		Conf config = new Conf();
		Messages msg = new Messages();
		User user = new User(this.p.getName());
		if (user.isMuted()) {
			if (user.getMuteExpire() != null) {
				Time time = new Time(user.getMuteExpire());
				if (time.isPastOrPresent()) {
					user.setMute(false);
				} else {
					user.sendMessage(msg.getMessage("onChat"));
					return;
				}

			} else {
				user.sendMessage(msg.getMessage("onChat"));
				return;
			}
		}
		this.p.sendMessage(config.getSendMessage().replaceAll("%to%", to.getName()).replaceAll("%message%", this.msg));
		to.sendMessage(
				config.getReceivedMessage().replaceAll("%from%", this.p.getName()).replaceAll("%message%", this.msg));
		User.getUser(to.getName()).setReply(this.p.getName());
		sendSpyMessage(to, this.p, this.msg);
	}

	public String colorize(boolean color, boolean format, boolean magic) {
		if (color) {
			this.msg = this.msg.replaceAll("&1", "§1").replaceAll("&2", "§2").replaceAll("&3", "§3")
					.replaceAll("&4", "§4").replaceAll("&5", "§5").replaceAll("&6", "§6").replaceAll("&7", "§7")
					.replaceAll("&8", "§8").replaceAll("&9", "§9").replaceAll("&0", "§0").replaceAll("&a", "§a")
					.replaceAll("&b", "§b").replaceAll("&c", "§c").replaceAll("&d", "§d").replaceAll("&e", "§e")
					.replaceAll("&f", "§f");
		}
		if (format) {
			this.msg = this.msg.replaceAll("&l", "§l").replaceAll("&m", "§m").replaceAll("&n", "§n")
					.replaceAll("&o", "§o").replaceAll("&m", "§m").replaceAll("&n", "§n");
		}
		if (magic) {
			this.msg = this.msg.replaceAll("&k", "§k");
		}
		return this.msg;
	}

	public String colorizeNick() {
		boolean chat = false, format = false, magic = false;
		if (this.p.hasPermission("main.nick.color")) {
			chat = true;
		}
		if (this.p.hasPermission("main.nick.format")) {
			format = true;
		}
		if (this.p.hasPermission("main.nick.magic")) {
			magic = true;
		}
		return colorize(chat, format, magic);
	}

	public void serverBroadcast() {
		Messages msg = new Messages();
		String desc = msg.getMessage("Broadcast").replaceAll("%message%", this.msg);
		Bukkit.broadcastMessage(desc.replaceAll("&", "§"));
	}

	public static String getRealname(String nick) {
		return MainPlugin.getDataBase().getReal(nick);
	}

	public static String getAnnounce(String name) {
		Conf config = new Conf();
		if (!config.getAnnounces().contains(name.toLowerCase()))
			return null;
		List<String> anns = config.getStringList("Announcement.messages." + name);
		String ann = anns.get(0);
		for (int i = 1; i < anns.size(); i++) {
			if (anns.get(i).isEmpty()) {
				ann += "§r\n                     \n";
				continue;
			}
			ann += "\n§r" + anns.get(i);
		}
		return ann.replaceAll("&", "§");
	}

	public static boolean isUsed(String nick) {
		return MainPlugin.getDataBase().isUsed(nick);
	}

	public static void sendSpyMessage(Player to, Player from, String msg) {
		Conf config = new Conf();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (User.getUser(p.getName()).hasSocialspy()) {
				p.sendMessage(config.getSpyMessage().replaceAll("%from%", from.getName())
						.replaceAll("%to%", to.getName()).replaceAll("%message%", msg));
			}
		}
	}

	public static void sendAnnounce() {

		Conf config = new Conf();
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(MainPlugin.plugin, new Runnable() {

			int i = 0;

			@Override
			public void run() {

				if (i >= config.getAnnounces().size()) {
					i = 0;
					return;
				}

				User.sendAllMessage(ChatManager.getAnnounce(config.getAnnounces().get(i)));
				i++;

			}
		}, 60, config.getAnnounceDelay() * 20);
	}

	public static void setDisplay(User user) {
		// SET DISPLAY NAME
		Conf config = new Conf();
		PermissionsPlus perm = new PermissionsPlus(user.getPlayer());
		String display = "";
		if (user.getPlayer().isOp()) {
			if (!config.getOpChar().isEmpty())
				display += "§" + config.getOpChar();
		}
		if (user.hasNickname()) {
			ChatManager cm = new ChatManager(user.getPlayer(), user.getNickname());
			display += config.getNickCharPrefix() + cm.colorizeNick();
		} else {
			display += user.getPlayer().getName();
		}
		if (config.useChat())
			user.getPlayer().setDisplayName(display + "§r");
		else
			user.getPlayer().setDisplayName(perm.getPrefix() + "" + display + "§r" + perm.getSuffix());
	}

}
