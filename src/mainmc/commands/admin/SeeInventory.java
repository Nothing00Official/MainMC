package mainmc.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;

public class SeeInventory implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"invsee", "endersee", "enderchest"};
		return array;
	}


	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		
		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player) {

			if (cmd.getName().equalsIgnoreCase("invsee")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.invsee")) {

					if (args.length == 1) {

						User user = new User(args[0]);
						if (user.isOnline()) {

							((Player) sender).openInventory(user.getPlayer().getInventory());

						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/invsee <player>"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("endersee")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.enderchest.other")) {

					if (args.length == 1) {

						User user = new User(args[0]);
						if (user.isOnline()) {

							((Player) sender).openInventory(user.getPlayer().getEnderChest());

						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/endersee <player>"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("enderchest")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.enderchest")) {
					((Player) sender).openInventory(((Player) sender).getEnderChest());
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

		} else {
			System.out.print("YOU MUST BE A PLAYER!");
		}
		return false;
	}

}
