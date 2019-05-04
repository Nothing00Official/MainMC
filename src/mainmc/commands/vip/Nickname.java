package mainmc.commands.vip;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.ChatManager;

public class Nickname implements CommandExecutor {

	public static String[] getCommands() {
		String[] array = { "nick", "realname" };
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("nick")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.nick")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						if (args[0].equalsIgnoreCase("off")) {
							User user = new User(sender.getName());

							if (user.hasNickname()) {
								user.removeNickname();
								ChatManager.setDisplay(user);
								sender.sendMessage(msg.getMessage("RemoveNick"));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("NoNick"));
								return true;
							}
						} else {
							Conf config = new Conf();

							if (ChatManager.isUsed(args[0])) {
								sender.sendMessage(msg.getMessage("NickUsed"));
								return true;
							}

							if (args[0].length() > config.getNickLength() || args[0].equalsIgnoreCase("off")) {
								sender.sendMessage(msg.getMessage("NickLength"));
								return true;
							}

							User user = new User(sender.getName());
							ChatManager cm = new ChatManager(user.getPlayer(), args[0]);
							user.setNickname(args[0]);
							ChatManager.setDisplay(user);
							sender.sendMessage(msg.getMessage("Nick").replaceAll("%nick%", cm.colorizeNick()));
							return true;
						}
					} else if (args.length == 2) {
						if (psender.hasPermission("main.nick.other")) {
							User user = new User(args[1]);
							if (user.isOnline()) {
								if (args[0].equalsIgnoreCase("off")) {
									if (user.hasNickname()) {
										user.removeNickname();
										ChatManager.setDisplay(user);
										user.sendMessage(msg.getMessage("RemoveNick"));
										sender.sendMessage(msg.getMessage("DONE"));
										return true;
									} else {
										sender.sendMessage(msg.getMessage("NoNick"));
										return true;
									}
								} else {

									if (ChatManager.isUsed(args[0])) {
										sender.sendMessage(msg.getMessage("NickUsed"));
										return true;
									}

									Conf config = new Conf();
									if (args[0].length() > config.getNickLength() || args[0].equalsIgnoreCase("off")) {
										sender.sendMessage(msg.getMessage("NickLength"));
										return true;
									}

									ChatManager cm = new ChatManager(user.getPlayer(), args[0]);
									user.setNickname(args[0]);
									ChatManager.setDisplay(user);
									user.sendMessage(msg.getMessage("Nick").replaceAll("%nick%", cm.colorizeNick()));
									sender.sendMessage(msg.getMessage("DONE"));
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
					} else {
						sender.sendMessage("§rUsage: /nick <off/nickname> [player]");
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("realname")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.realname")) {
					if (args.length == 1) {
						if (ChatManager.getRealname(args[0]) != null) {
							sender.sendMessage(msg.getMessage("Realname").replaceAll("%nick%", args[0])
									.replaceAll("%name%", ChatManager.getRealname(args[0])));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}

					} else {
						sender.sendMessage("§rUsage: /realnick <nickname>");
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
