package mainmc.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.Home;
import mainmc.nothing00.functions.User;

public class AdminHomeCommand implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"adminhome"};
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		
		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("adminhome")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.adminhome")) {

					if (args.length == 1) {
						if (psender.hasPermission("main.home.other")) {
							User user = new User(args[0]);
							if (user.exists()) {
								Home home = new Home(user.getName());
								if (!home.hasHomes()) {
									sender.sendMessage(msg.getMessage("NoHome"));
									return true;
								}
								sender.sendMessage(msg.getMessage("HomeList") + "\n§e"
										+ String.join("§7, §e", home.getHomeNames().toArray(new String[0])));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("NoPlayer"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else if (args.length == 3) {
						if (!(sender instanceof Player))
							return true;
						if (args[1].equalsIgnoreCase("tp")) {
							if (psender.hasPermission("main.adminhome.teleport")) {

								User user = new User(args[0]);
								if (user.exists()) {
									Home home = new Home(user.getName());
									if (!home.hasHomes()) {
										sender.sendMessage(msg.getMessage("NoHome"));
										return true;
									}
									if (!home.getHomeNames().contains(args[2].toLowerCase())) {
										sender.sendMessage(msg.getMessage("InvalidHome"));
										return true;
									}
									User senderu = new User(sender.getName());
									senderu.teleported(home.getHome(args[2]));
									sender.sendMessage(msg.getMessage("Home"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("NoPlayer"));
									return true;
								}

							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}
						} else if (args[1].equalsIgnoreCase("delete")) {
							if (psender.hasPermission("main.delhome.other")) {

								User user = new User(args[0]);
								if (user.exists()) {
									Home home = new Home(user.getName());
									if (!home.hasHomes()) {
										sender.sendMessage(msg.getMessage("NoHome"));
										return true;
									}
									if (!home.getHomeNames().contains(args[2].toLowerCase())) {
										sender.sendMessage(msg.getMessage("InvalidHome"));
										return true;
									}
									home.deleteHome(args[2]);
									sender.sendMessage(msg.getMessage("Home"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("NoPlayer"));
									return true;
								}

							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}
						} else if (args[1].equalsIgnoreCase("set")) {
							if (psender.hasPermission("main.adminhome.set")) {

								User user = new User(args[0]);
								if (user.exists()) {
									Home home = new Home(user.getName());
									if (home.getHomeNames().contains(args[2].toLowerCase())) {
										sender.sendMessage(msg.getMessage("ExistingHome"));
										return true;
									}
									home.setHome(((Player) sender).getLocation(), args[2]);
									sender.sendMessage(msg.getMessage("setHome"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("NoPlayer"));
									return true;
								}

							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}
						}else {
							sender.sendMessage("§rUsage: /admihome <player> <tp/delete/set> <home>");
						}
					}else {
						sender.sendMessage("§rUsage: /admihome <player> <tp/delete/set> <home>");
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

		}
		return false;
	}

}
