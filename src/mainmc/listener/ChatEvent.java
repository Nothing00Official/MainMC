package mainmc.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.PermissionsPlus;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.AfkManager;
import mainmc.nothing00.utils.ChatManager;
import mainmc.nothing00.utils.Time;

@SuppressWarnings("deprecation")
public class ChatEvent implements Listener {

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onChat(PlayerChatEvent e) {

		Messages msg = new Messages();
		Conf config = new Conf();
		User user = new User(e.getPlayer().getName());
		MainPermissions s = new MainPermissions(e.getPlayer());

		// CAN CHAT
		if (!s.hasPermission("main.chat")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(msg.getMessage("No-Perm"));
			return;
		}

		// LOCKED
		if (user.isLocked()) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(config.getUnLockMessage());
			return;
		}

		// IN JAIL
		if (user.isJailed()) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(msg.getMessage("jailed"));
			return;
		}

		// IS MUTED
		if (user.isMuted()) {
			if (user.getMuteExpire() != null) {
				Time time = new Time(user.getMuteExpire());
				if (time.isPastOrPresent()) {
					user.setMute(false);
				} else {
					e.setCancelled(true);
					e.getPlayer().sendMessage(msg.getMessage("onChat"));
					return;
				}

			} else {
				e.setCancelled(true);
				e.getPlayer().sendMessage(msg.getMessage("onChat"));
				return;
			}
		}

		// CHECK AFK
		AfkManager afk = new AfkManager(e.getPlayer());
		if (config.cancelAfkonMove()) {
			if (afk.isAfk()) {
				afk.removeAfk();
				User.sendAllMessage(msg.getMessage("offAFK").replaceAll("%player%", e.getPlayer().getName()));
			}
		}
		if (config.freezeAfk()) {
			if (afk.isAfk()) {
				e.setCancelled(true);
				return;
			}
		}

		// CHECK IGNORE
		for (Player p : Bukkit.getOnlinePlayers()) {
			User currentUser = new User(p.getName());
			if (currentUser.getIgnores().contains(e.getPlayer().getName())) {
				e.getRecipients().remove(p);
			}
		}

		if (!config.useChat())
			return;
		// SET FORMAT
		PermissionsPlus perm = new PermissionsPlus(e.getPlayer());
		e.setFormat(config.getFormat().replace("{player}", e.getPlayer().getName())
				.replace("{displayname}", e.getPlayer().getDisplayName()).replace("{prefix}", perm.getPrefix())
				.replace("{suffix}", perm.getSuffix()) + "%2$s");

		if (perm.hasFormat())
			e.setFormat(perm.getFormat().replace("{prefix}", perm.getPrefix()).replace("{suffix}", perm.getSuffix())
					+ "%2$s");

		boolean color = false, format = false, magic = false;
		if (s.hasPermission("main.chat.color"))
			color = true;
		if (s.hasPermission("main.chat.format"))
			format = true;
		if (s.hasPermission("main.chat.magic"))
			magic = true;
		ChatManager cm = new ChatManager(e.getPlayer(), e.getMessage());
		e.setMessage(cm.colorize(color, format, magic));

	}

}
