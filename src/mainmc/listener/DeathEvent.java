package mainmc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import mainmc.folders.Conf;
import mainmc.folders.JailData;
import mainmc.folders.Messages;
import mainmc.folders.SpawnData;
import mainmc.nothing00.functions.Home;
import mainmc.nothing00.functions.User;

public class DeathEvent implements Listener {

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onDeath(PlayerDeathEvent e) {
		Conf config = new Conf();

		if (!config.deathMessage())
			e.setDeathMessage(null);

		User.getUser(e.getEntity().getName()).saveLastLocation();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSpawn(PlayerRespawnEvent e) {
		Conf config = new Conf();
		Messages msg = new Messages();
		User user = new User(e.getPlayer().getName());

		// IN JAIL
		if (user.isJailed()) {
			JailData jail = new JailData(user.getJail());
			if (jail.exists())
				e.getPlayer().teleport(jail.getLocation());
			e.setRespawnLocation(jail.getLocation());
			e.getPlayer().sendMessage(msg.getMessage("jailed"));
			return;
		}

		Home home = new Home(e.getPlayer().getName());
		SpawnData spawn = new SpawnData();
		if (config.homeRespawn() && home.hasHomes()) {
			e.getPlayer().teleport(home.getHome(home.getHomeNames().get(0)));
			e.setRespawnLocation(home.getHome(home.getHomeNames().get(0)));
		} else {
			if (spawn.thereIsSpawn()) {
				e.getPlayer().teleport(spawn.getSpawn());
				e.setRespawnLocation(spawn.getSpawn());
			} else {
				e.getPlayer().sendMessage(msg.getMessage("NoSpawn"));
			}
		}
	}

}
