package mainmc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;

public class BlockBreak implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBreak(BlockBreakEvent e) {

		Messages msg = new Messages();
		Conf config = new Conf();
		User user = new User(e.getPlayer().getName());
		MainPermissions s = new MainPermissions(e.getPlayer());

		if (!s.hasPermission("main.build")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(msg.getMessage("No-Perm"));
			return;
		}

		if (!s.hasPermission("main.build.break")) {
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

		// BLACKLIST
		if (config.getBreakBlackList().contains("" + e.getBlock().getTypeId())
				&& !s.hasPermission("main.build.break.bypassblacklist")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(msg.getMessage("BuildBreak"));
			return;
		}
	}

}
