package mainmc.commands.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.Home;
import mainmc.nothing00.functions.User;

public class HomeCommand implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"home", "sethome", "delhome"};
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		
		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player) {

			if (cmd.getName().equalsIgnoreCase("sethome")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.sethome")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						Home home = new Home(user.getName());
						if (home.getHomes().size() < user.getHomeLimit() || user.getHomeLimit() == -1) {
							home.setHome(((Player) sender).getLocation());
							sender.sendMessage(msg.getMessage("setHome"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("MaxHome"));
							return true;
						}

					} else if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						Home home = new Home(user.getName());
						if (home.getHomes().size() < user.getHomeLimit() || user.getHomeLimit() == -1) {

							if (home.getHomeNames().contains(args[0].toLowerCase())) {
								sender.sendMessage(msg.getMessage("ExistingHome"));
								return true;
							}

							home.setHome(((Player) sender).getLocation(), args[0]);
							sender.sendMessage(msg.getMessage("setHome"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("MaxHome"));
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /sethome [name]");
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("home")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.home")) {
					if (!(sender instanceof Player))
						return true;
					User user = new User(sender.getName());
					if (args.length == 0) {
						Home home = new Home(user.getName());
						if (!home.hasHomes()) {
							sender.sendMessage(msg.getMessage("NoHome"));
							return true;
						}
						if (home.getHomes().size() > 1) {
							sender.sendMessage(msg.getMessage("HomeList") + "\n§e"
									+ String.join("§7, §e", home.getHomeNames().toArray(new String[0])));
							return true;
						}
						user.teleported(home.getHome(home.getHomeNames().get(0)));
						sender.sendMessage(msg.getMessage("Home"));
						return true;
					} else if (args.length == 1) {
						Home home = new Home(user.getName());
						if (!home.hasHomes()) {
							sender.sendMessage(msg.getMessage("NoHome"));
							return true;
						}
						if (!home.getHomeNames().contains(args[0].toLowerCase())) {
							sender.sendMessage(msg.getMessage("InvalidHome"));
							return true;
						}
						user.teleported(home.getHome(args[0]));
						sender.sendMessage(msg.getMessage("Home"));
						return true;
					} else {
						sender.sendMessage("§rUsage: /home [home]");
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}

			}

			if (cmd.getName().equalsIgnoreCase("delhome")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.delhome")) {
					if (!(sender instanceof Player))
						return true;
					User user = new User(sender.getName());
					if (args.length == 0) {
						Home home = new Home(user.getName());
						if (!home.hasHomes()) {
							sender.sendMessage(msg.getMessage("NoHome"));
							return true;
						}
						if (home.getHomes().size() > 1) {
							sender.sendMessage(msg.getMessage("HomeList") + "\n§e"
									+ String.join("§7, §e", home.getHomeNames().toArray(new String[0])));
							return true;
						}
						home.deleteHome(home.getHomeNames().get(0));
						sender.sendMessage(msg.getMessage("delHome"));
						return true;
					} else if (args.length == 1) {
						Home home = new Home(user.getName());
						if (!home.hasHomes()) {
							sender.sendMessage(msg.getMessage("NoHome"));
							return true;
						}
						if (!home.getHomeNames().contains(args[0].toLowerCase())) {
							sender.sendMessage(msg.getMessage("InvalidHome"));
							return true;
						}
						home.deleteHome(args[0]);
						sender.sendMessage(msg.getMessage("delHome"));
						return true;
					} else {
						sender.sendMessage("§rUsage: /delhome [home]");
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
