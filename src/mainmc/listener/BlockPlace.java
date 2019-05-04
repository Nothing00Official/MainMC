package mainmc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.ItemPlugin;
import mainmc.nothing00.functions.User;

public class BlockPlace implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlace(BlockPlaceEvent e) {

		Messages msg = new Messages();
		Conf config = new Conf();
		User user = new User(e.getPlayer().getName());
		MainPermissions s = new MainPermissions(e.getPlayer());

		if (!s.hasPermission("main.build")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(msg.getMessage("No-Perm"));
			return;
		}

		if (!s.hasPermission("main.build.place")) {
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

		// SPAWNER
		if (user.getItem() != null) {
			ItemPlugin item = new ItemPlugin(user.getItem());
			if (item.isSpawner()) {
				item.setSpawner(e.getBlockPlaced());
				return;
			}
		}

		// BLACKLIST
		if (config.getPlaceBlackList().contains("" + e.getBlock().getTypeId())
				&& !s.hasPermission("main.build.place.bypassblacklist")) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(msg.getMessage("BuildPlace"));
			return;
		}

	}

}
