package mainmc.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import mainmc.MainPermissions;
import mainmc.SpigotUpdater;
import mainmc.folders.Conf;
import mainmc.folders.JailData;
import mainmc.folders.Messages;
import mainmc.folders.SpawnData;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.functions.Kit;
import mainmc.nothing00.functions.Mail;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.ChatManager;

public class JoinEvent implements Listener {	

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	public void onJoin(PlayerJoinEvent e) {
		Messages msg = new Messages();
		Conf config = new Conf();
		MainPermissions s = new MainPermissions(e.getPlayer());
		User user = new User(e.getPlayer().getName());

		if (config.getUserBlackList().contains(e.getPlayer().getName())) {
			e.getPlayer().kickPlayer(config.getUserBlacklistMessage());
			return;
		}

		if (user.fileExists() && config.caseSensitive() && !user.isCorrectCase()) {
			e.getPlayer().kickPlayer(config.getCaseMessage().replace("{player}", user.getOriginalName()));
			return;
		}

		// USER CREATION
		if (!user.exists()) {
			user.newUser();
			User.sendAllMessage(config.getNewPlayerMessage().replace("{player}", user.getName())
					.replace("{displayname}", e.getPlayer().getDisplayName()));
			if (config.getFirstKit() != null) {
				Kit kit = new Kit(config.getFirstKit(), e.getPlayer());
				if (kit.exists()) {
					kit.giveKit();
				}
			}
		}
		if (!config.economyAddon())
			user.createBalance();
		User.getUser(e.getPlayer().getName()).setJoinDate();
		user.onLogin();
		user.banIfIpIsBanned();
		ChatManager.setDisplay(user);

		if (config.sameIp() && user.getCurrentIp().equals(Bukkit.getServer().getIp())) {
			e.getPlayer().kickPlayer(config.getSameIPMessage());
			return;
		}

		if (config.uuid() && !user.getUUID().equals(e.getPlayer().getUniqueId().toString())) {
			e.getPlayer().kickPlayer(config.getUuidMessage());
			return;
		}

		// CHANGE IP
		if (user.changedHost(e.getPlayer().getAddress().getHostString())) {
			user.onChangeIp(e.getPlayer().getAddress().getHostString() + ":" + e.getPlayer().getAddress().getPort());

			// LOCKACCOUNT
			if (config.lockAccountEnabled()) {
				if (user.hasKeyword()) {
					if (!user.isLocked())
						user.setLocked(true);
					e.getPlayer().sendMessage(config.getUnLockMessage());
				} else {
					e.getPlayer().sendMessage(config.getSetLockMessage());
				}
			}
		}

		// IN JAIL
		if (user.isJailed()) {
			JailData jail = new JailData(user.getJail());
			if (jail.exists())
				e.getPlayer().teleport(jail.getLocation());
			e.getPlayer().sendMessage(msg.getMessage("jailed"));
			return;
		}

		// SPAWN TP
		SpawnData spawn = new SpawnData();
		if (config.spawnTp()) {
			if (spawn.thereIsSpawn()) {
				User.getUser(e.getPlayer().getName()).teleported(spawn.getSpawn());
			} else {
				e.getPlayer().sendMessage(msg.getMessage("NoSpawn"));
			}
		}

		// SILENT JOIN
		if (!config.getJoinMessage().equals("none") && config.getJoinMessage() != null
				&& !config.getJoinMessage().isEmpty()) {
			e.setJoinMessage(config.getJoinMessage().replace("{player}", e.getPlayer().getName())
					.replace("{displayname}", e.getPlayer().getDisplayName()));
		}
		if (config.silentQuitJoin()) {
			if (s.hasPermission("main.silentjoin")) {
				e.setJoinMessage(null);
			}
			if (s.hasPermission("main.silentjoin.vanish")) {
				User.getUser(e.getPlayer().getName()).setVanish(true);
			}
		}

		// SHOW MOTD
		if (s.hasPermission("main.motd")) {
			user.showMotd();
		}

		// SEND NEW MAILS
		Mail mail = new Mail(null, user.getName(), null);
		if (mail.hasMails()) {
			user.sendMessage(msg.getMessage("newMail").replaceAll("%count%", "" + mail.getMails().size()));
		}
		User.vanish(e.getPlayer());

		// UPDATE
		if (e.getPlayer().isOp()) {
			SpigotUpdater updater = new SpigotUpdater(MainPlugin.getInstance(), 55455);
			try {
				if (updater.checkForUpdates()) {
					e.getPlayer().sendMessage("§6§m=====================================================");
					e.getPlayer()
							.sendMessage("§6A MainMC update was found! New version: §cv" + updater.getLatestVersion());
					e.getPlayer().sendMessage("§6Download update: §e" + updater.getResourceURL());
					e.getPlayer().sendMessage("§6§m=====================================================");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
