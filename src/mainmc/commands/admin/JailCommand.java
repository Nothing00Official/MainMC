package mainmc.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.JailData;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;

public class JailCommand implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"jail", "jails", "unjail", "createjail", "deljail"};
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
			
			MainPermissions psender = new MainPermissions(sender);

			if (cmd.getName().equalsIgnoreCase("createjail")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.jail.create")) {
					if (args.length == 1) {
						JailData jail = new JailData(args[0]);

						if (!(sender instanceof Player))
							return true;

						if (jail.exists()) {
							sender.sendMessage(msg.getMessage("JailExist"));
							return true;
						}

						jail.setJail(((Player) sender).getLocation());
						sender.sendMessage(msg.getMessage("JailCreated").replaceAll("%jail%", jail.getName()));
						return true;

					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/createjail <id>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("jails")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.jails")) {
					JailData jail = new JailData();
					if (jail.getJails().isEmpty()) {
						sender.sendMessage(msg.getMessage("NoJails"));
						return true;
					}
					sender.sendMessage(msg.getMessage("Jails") + "\n§e"
							+ String.join("§7, §e", jail.getJails().toArray(new String[0])));
					return true;
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("deljail")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.jail.delete")) {
					if (args.length == 1) {
						JailData jail = new JailData(args[0]);

						if (!jail.exists()) {
							sender.sendMessage(msg.getMessage("JailNoExist"));
							return true;
						}

						jail.delJail();
						sender.sendMessage(msg.getMessage("JailRemoved").replaceAll("%jail%", jail.getName()));
						return true;

					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/deljail <id>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("jail")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.jail")) {
					if (args.length == 2) {
						User user = new User(args[0]);
						if (user.isOnline()) {
							JailData jail = new JailData(args[1]);
							if (jail.exists()) {
								if (user.isUntouch()) {
									sender.sendMessage(msg.getMessage("Untouchable"));
									return true;
								}
								if (user.isJailed()) {
									sender.sendMessage(msg.getMessage("AlreadyJailed"));
									return true;
								}
								user.setJails(jail);
								sender.sendMessage(msg.getMessage("inJail"));
								user.sendMessage(msg.getMessage("Jail").replaceAll("%admin%", sender.getName()));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("JailNoExist"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/jail <player> <id>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("unjail")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.unjail")) {
					if (args.length == 1) {
						User user = new User(args[0]);
						if (user.isOnline()) {
							if (!user.isJailed()) {
								sender.sendMessage(msg.getMessage("NoJailed"));
								return true;
							}
							user.unJail();
							sender.sendMessage(msg.getMessage("NoJailed"));
							user.sendMessage(msg.getMessage("unJail").replaceAll("%admin%", sender.getName()));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/unjail <player>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

		}
		return true;
	}

}
