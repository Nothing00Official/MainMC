package mainmc.commands.admin;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.Mail;
import mainmc.nothing00.functions.User;

public class SpyMailCommand implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"spymail"};
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		
		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("spymail")) {
				Messages msg = new Messages();
				if (psender.hasPermission("mail.spymail")) {
					if (args.length == 2) {
						User user = new User(args[0]);
						if (user.exists()) {
							if (args[1].equalsIgnoreCase("read")) {
								if (psender.hasPermission("main.spymail.read")) {
									Mail mail = new Mail(null, user.getName(), null);
									if (mail.hasMails()) {
										sender.sendMessage(
												msg.getMessage("MailRead").replaceAll("%player%", user.getName()));
										for (int i = 0; i < mail.getMails().size(); i++) {
											int index = i + 1;
											sender.sendMessage(
													"§r§l" + index + ")" + mail.getMails().get(i).split(":")[1]);
										}
										return true;
									} else {
										sender.sendMessage(msg.getMessage("NoMail"));
										return true;
									}
								} else {
									sender.sendMessage(msg.getMessage("No-Perm"));
									return true;
								}
							} else if (args[1].equalsIgnoreCase("clear")) {
								if (psender.hasPermission("main.spymail.clear")) {
									Mail mail = new Mail(null, user.getName(), null);
									if (mail.hasMails()) {
										mail.clearMails();
										user.sendMessage(msg.getMessage("MailClear"));

										sender.sendMessage(msg.getMessage("DONE"));
										return true;
									} else {
										sender.sendMessage(msg.getMessage("NoMail"));
										return true;
									}
								} else {
									sender.sendMessage(msg.getMessage("No-Perm"));
									return true;
								}
							} else {
								sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/spymail <player> <read/clear/clearindex> [index]"));
							}
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else if (args.length == 3) {
						if (args[1].equalsIgnoreCase("clearindex")) {
							if (psender.hasPermission("main.spymail.clearindex")) {
								User user = new User(args[0]);
								if (user.exists()) {
									if (!NumberUtils.isNumber(args[2])) {
										sender.sendMessage(msg.getMessage("Args"));
										return true;
									}
									Mail mail = new Mail(null, user.getName(), null);
									if (mail.hasMails()) {

										if (Integer.parseInt(args[2]) > mail.getMails().size()) {
											sender.sendMessage(msg.getMessage("NoIndex"));
											return true;
										}

										mail.removeMailAt(Integer.parseInt(args[2]) - 1);
										user.sendMessage(msg.getMessage("RemovedIndex").replaceAll("%index%", args[2]));

										sender.sendMessage(msg.getMessage("DONE"));
										return true;
									} else {
										sender.sendMessage(msg.getMessage("NoMail"));
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
							sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/spymail <player> <read/clear/clearindex> [index]"));
						}

					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/spymail <player> <read/clear/clearindex> [index]"));
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
