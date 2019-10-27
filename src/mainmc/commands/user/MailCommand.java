package mainmc.commands.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.Mail;
import mainmc.nothing00.functions.User;

public class MailCommand implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"mail"};
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		
		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player) {

			if (cmd.getName().equalsIgnoreCase("mail")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.mail")) {
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("read")) {
							if (psender.hasPermission("main.mail.read")) {
								Mail mail = new Mail(null, sender.getName(), null);
								if (mail.hasMails()) {
									sender.sendMessage(
											msg.getMessage("MailRead").replaceAll("%player%", sender.getName())
													+ "\n§r§l"
													+ String.join("\n§r§l", mail.getMails().toArray(new String[0])));
									sender.sendMessage(msg.getMessage("MailtoClear"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("NoMail"));
									return true;
								}
							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}
						} else if (args[0].equalsIgnoreCase("clear")) {
							if (psender.hasPermission("main.mail.clear")) {
								Mail mail = new Mail(null, sender.getName(), null);
								if (mail.hasMails()) {
									mail.clearMails();
									sender.sendMessage(msg.getMessage("MailClear"));
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
							sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/mail <read/clear>"));
						}
					} else if (args.length >= 3) {
						if (args[0].equalsIgnoreCase("send")) {
							if (psender.hasPermission("main.mail.send")) {
								User user = new User(args[1]);
								if (user.exists()) {
									Conf config = new Conf();
									String mess = "";
									for (int i = 2; i < args.length; i++) {
										mess += " " + args[i];
									}
									Mail mail = new Mail(sender.getName(), user.getName(), mess);
									if (config.sendFullMail()) {
										mail.sendMail();
										sender.sendMessage(msg.getMessage("MailSent"));
										if (user.isOnline()) {
											user.sendMessage(msg.getMessage("newMail").replaceAll("%count%",
													mail.getMails().size() + ""));
										}
										return true;
									} else {
										if (mail.getMails().size() < config.getMaxBox()) {
											mail.sendMail();
											sender.sendMessage(msg.getMessage("MailSent"));
											if (user.isOnline()) {
												user.sendMessage(msg.getMessage("newMail").replaceAll("%count%",
														mail.getMails().size() + ""));
											}
											return true;
										} else {
											sender.sendMessage(msg.getMessage("MailFull"));
											return true;
										}
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
							sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/mail send <player> <message>"));
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/mail <send/read/clear> <player> <message>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

		} else {
			System.out.println("YOU MUST BE A PLAYER!");
		}
		return true;
	}

}
