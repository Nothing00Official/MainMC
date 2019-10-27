package mainmc.commands.vip;

import org.apache.commons.lang.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;

@SuppressWarnings("deprecation")
public class SpeedCommand implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"speed", "walkspeed", "flyspeed"};
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		
		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("speed")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.speed") && psender.hasPermission("main.speed.other")) {
					if (args.length == 3) {

						if (!NumberUtils.isNumber(args[2])) {
							sender.sendMessage(msg.getMessage("Args"));
							return true;
						}

						User user = new User(args[0]);
						if (user.isOnline()) {

							if (args[1].equalsIgnoreCase("walk")) {
								user.setWalkSpeed(Integer.parseInt(args[2]));
								user.sendMessage(msg.getMessage("Speed").replaceAll("%type%", "WALK")
										.replaceAll("%count%", args[2]));
								sender.sendMessage(msg.getMessage("DONE"));
								return true;
							} else if (args[1].equalsIgnoreCase("fly")) {
								user.setFlySpeed(Integer.parseInt(args[2]));
								user.sendMessage(msg.getMessage("Speed").replaceAll("%type%", "FLY")
										.replaceAll("%count%", args[2]));
								sender.sendMessage(msg.getMessage("DONE"));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/speed <player> <walk/fly> <speed>"));
							}

						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/speed <player> <walk/fly> <speed>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("walkspeed")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.speed")) {
					if (args.length == 1) {

						if (!(sender instanceof Player))
							return true;

						if (!NumberUtils.isNumber(args[0])) {
							sender.sendMessage(msg.getMessage("Args"));
							return true;
						}

						User user = new User(sender.getName());
						user.setWalkSpeed(Integer.parseInt(args[0]));
						user.sendMessage(
								msg.getMessage("Speed").replaceAll("%type%", "WALK").replaceAll("%count%", args[0]));
						return true;
					} else if (args.length == 2) {
						if (psender.hasPermission("main.speed.other")) {
							if (!NumberUtils.isNumber(args[0])) {
								sender.sendMessage(msg.getMessage("Args"));
								return true;
							}
							User user = new User(args[1]);
							if (user.isOnline()) {
								user.setWalkSpeed(Integer.parseInt(args[0]));
								user.sendMessage(msg.getMessage("Speed").replaceAll("%type%", "WALK")
										.replaceAll("%count%", args[0]));
								sender.sendMessage(msg.getMessage("DONE"));
							} else {
								sender.sendMessage(msg.getMessage("NoPlayer"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					}else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/walkspeed <speed> [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}
			
			if (cmd.getName().equalsIgnoreCase("flyspeed")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.speed")) {
					if (args.length == 1) {

						if (!(sender instanceof Player))
							return true;

						if (!NumberUtils.isNumber(args[0])) {
							sender.sendMessage(msg.getMessage("Args"));
							return true;
						}

						User user = new User(sender.getName());
						user.setFlySpeed(Integer.parseInt(args[0]));
						user.sendMessage(
								msg.getMessage("Speed").replaceAll("%type%", "FLY").replaceAll("%count%", args[0]));
						return true;
					} else if (args.length == 2) {
						if (psender.hasPermission("main.speed.other")) {
							if (!NumberUtils.isNumber(args[0])) {
								sender.sendMessage(msg.getMessage("Args"));
								return true;
							}
							User user = new User(args[1]);
							if (user.isOnline()) {
								user.setFlySpeed(Integer.parseInt(args[0]));
								user.sendMessage(msg.getMessage("Speed").replaceAll("%type%", "FLY")
										.replaceAll("%count%", args[0]));
								sender.sendMessage(msg.getMessage("DONE"));
							} else {
								sender.sendMessage(msg.getMessage("NoPlayer"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					}else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/flyspeed <speed> [player]"));
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
