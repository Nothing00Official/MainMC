package mainmc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.folders.SpawnData;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.functions.User;

public class SpawnCommand implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"setspawn", "spawn"};
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		
		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player) {

			if (cmd.getName().equalsIgnoreCase("setspawn")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.setspawn")) {
					SpawnData spawn = new SpawnData();
					spawn.setSpawn(((Player) sender).getLocation());
					sender.sendMessage(msg.getMessage("setSpawn"));
					return true;
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("spawn")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.spawn")) {
					SpawnData spawn = new SpawnData();
					if (spawn.thereIsSpawn()) {
						if (spawn.getSpawn() == null) {
							sender.sendMessage(msg.getMessage("InvalidSpawn"));
							return true;
						}
						User user = new User(sender.getName());

						Conf config = new Conf();

						if (config.spawnDelay() && !sender.hasPermission("main.spawn.bypassdelay")) {
							sender.sendMessage(msg.getMessage("Countdown").replaceAll("%location%", "Spawn")
									.replaceAll("%time%", config.getSpawnDelay() + ""));
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin.plugin,
									new Runnable() {
										@Override
										public void run() {
											user.teleported(spawn.getSpawn());
											sender.sendMessage(msg.getMessage("Spawn"));
										}
									}, config.getSpawnDelay() * 20);
						} else {
							user.teleported(spawn.getSpawn());
							sender.sendMessage(msg.getMessage("Spawn"));
						}

						return true;
					} else {
						sender.sendMessage(msg.getMessage("NoSpawn"));
						return true;
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

		} else {
			System.out.println("YOU MUST BE A PLAYER!");
		}
		return false;
	}

}
