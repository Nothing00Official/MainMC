package mainmc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import mainmc.folders.Conf;
import mainmc.folders.JailData;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.AfkManager;

public class MoveEvent implements Listener {

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onMove(PlayerMoveEvent e) {

		Messages msg = new Messages();
		Conf config = new Conf();
		User user = new User(e.getPlayer().getName());

		// LOCKED
		if (user.isLocked()) {
			e.getPlayer().teleport(e.getFrom());
			e.getPlayer().sendMessage(config.getUnLockMessage());
			return;
		}

		// IN JAIL
		if (user.isJailed()) {
			JailData jail = new JailData(user.getJail());
			if (jail.exists())
				e.getPlayer().teleport(jail.getLocation());
			e.getPlayer().sendMessage(msg.getMessage("jailed"));
			return;
		}

		// CHECK AFK
		AfkManager afk = new AfkManager(e.getPlayer());
		if (!afk.isAfk()) {
			afk.autoAfk(e.getPlayer().getLocation());
			return;
		}
		if (config.cancelAfkonMove()) {
			if (afk.isAfk()) {
				afk.removeAfk();
				User.sendAllMessage(msg.getMessage("offAFK").replaceAll("%player%", e.getPlayer().getName()));
			}
		}
		if (config.freezeAfk()) {
			if (afk.isAfk()) {
				afk.freeze();
			}
		}

	}

}
