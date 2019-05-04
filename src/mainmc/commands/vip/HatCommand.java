package mainmc.commands.vip;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;

public class HatCommand implements CommandExecutor {

	public static String[] getCommands() {
		String[] array = {"hat"};
		return array;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		
		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("hat")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.hat")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						if (user.setHat(user.getItem())) {
							sender.sendMessage(msg.getMessage("Head"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("InvalidItem"));
							return true;
						}
					} else if (args.length == 1) {
						if (args[0].equalsIgnoreCase("remove")) {
							if (psender.hasPermission("main.hat.remove")) {
								User user = new User(sender.getName());
								user.removeHat();
								sender.sendMessage(msg.getMessage("RemoveHat"));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}
						} else {
							if (psender.hasPermission("main.hat.other")) {
								User user = new User(args[0]);
								if (user.isOnline()) {
									if (user.setHat(user.getItem())) {
										user.sendMessage(msg.getMessage("Head"));
										sender.sendMessage(msg.getMessage("DONE"));
										return true;
									} else {
										sender.sendMessage(msg.getMessage("InvalidItem"));
										return true;
									}
								} else {
									sender.sendMessage(msg.getMessage("NoPlayer"));
									return true;
								}
							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}
						}
					} else if (args.length == 2) {
						if (psender.hasPermission("main.hat.remove.other")) {
							if (args[0].equalsIgnoreCase("remove")) {
								User user = new User(args[1]);
								if (user.isOnline()) {
									user.removeHat();
									user.sendMessage(msg.getMessage("RemoveHat"));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("NoPlayer"));
									return true;
								}
							} else {
								sender.sendMessage("§rUsage: /hat remove [player] | /hat [player]");
							}
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
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
