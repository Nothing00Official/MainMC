package mainmc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.ChatManager;

public class ChatCommand implements CommandExecutor {

	public static String[] getCommands() {
		String[] array = { "me", "broadcast", "helpstaff", "tell", "reply", "ignores", "ignore", "unignore",
				"socialspy" };
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			MainPermissions psender = new MainPermissions(sender);

			if (cmd.getName().equalsIgnoreCase("me")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.me")) {
					if (!(sender instanceof Player))
						return true;
					if (args.length > 0) {
						String mess = "";
						for (int i = 0; i < args.length; i++) {
							mess += " " + args[i];
						}

						boolean chat = false, format = false, magic = false;
						if (psender.hasPermission("main.me.color")) {
							chat = true;
						}
						if (psender.hasPermission("main.me.format")) {
							format = true;
						}
						if (psender.hasPermission("main.me.magic")) {
							magic = true;
						}
						ChatManager cm = new ChatManager(mess);
						User.sendAllMessage(
								msg.getMessage("Me").replaceAll("%message%", cm.colorize(chat, format, magic))
										.replaceAll("%player%", sender.getName()));
						return true;
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/me <message>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("broadcast")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.broadcast")) {
					if (args.length > 0) {
						String mess = "";
						for (int i = 0; i < args.length; i++) {
							mess += " " + args[i];
						}

						ChatManager cm = new ChatManager(mess);
						cm.serverBroadcast();
						return true;
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/broadcast <message>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("helpstaff")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.helpstaff")) {
					if (args.length > 0) {
						if (!(sender instanceof Player))
							return true;
						String mess = "";
						for (int i = 0; i < args.length; i++) {
							mess += " " + args[i];
						}
						ChatManager cm = new ChatManager((Player) sender, mess);
						sender.sendMessage(msg.getMessage("Received").replaceAll("%count%", cm.sendToStaff() + ""));
						return true;
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/helpstaff <message>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("tell")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.msg")) {
					if (args.length > 1) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(args[0]);
						if (user.isOnline()) {
							String mess = "";
							for (int i = 1; i < args.length; i++) {
								mess += " " + args[i];
							}
							ChatManager cm = new ChatManager((Player) sender, mess);
							cm.sendPrivateMessage(user.getPlayer());
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/msg <player> <message>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("reply")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.reply")) {
					if (args.length > 1) {
						if (!(sender instanceof Player))
							return true;
						if (User.getUser(sender.getName()).getReply() != null) {
							String mess = "";
							for (int i = 0; i < args.length; i++) {
								mess += " " + args[i];
							}
							ChatManager cm = new ChatManager((Player) sender, mess);
							cm.sendPrivateMessage(User.getUser(sender.getName()).getReply());
							return true;
						} else {
							sender.sendMessage(msg.getMessage("Reply"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/reply <message>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("ignores")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.ignores")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						if (user.hasIgnoredUser()) {
							sender.sendMessage(msg.getMessage("Ignores").replaceAll("%player%", user.getName()) + "\n§e"
									+ String.join("§7, §e", user.getIgnores().toArray(new String[0])));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoIgnores"));
							return true;
						}
					} else if (args.length == 1) {
						if (psender.hasPermission("main.ignores.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								if (user.hasIgnoredUser()) {
									sender.sendMessage(msg.getMessage("Ignores").replaceAll("%player%", user.getName())
											+ "\n§e" + String.join("§7, §e", user.getIgnores().toArray(new String[0])));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("NoIgnores"));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/ignores [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("ignore")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.ignore")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						User ignored = new User(args[0]);
						if (ignored.isOnline()) {
							if (!user.getIgnores().contains(ignored.getName())) {

								if (ignored.getPlayer().hasPermission("main.ignore.bypassignore")) {
									sender.sendMessage(msg.getMessage("IgnoreDenied"));
									return true;
								}

								user.addIgnored(args[0]);
								sender.sendMessage(msg.getMessage("Ignore").replaceAll("%player%", ignored.getName()));
								ignored.sendMessage(msg.getMessage("IgnoreMSG").replaceAll("%player%", user.getName()));
								return true;
							} else {
								user.removeIgnored(args[0]);
								sender.sendMessage(
										msg.getMessage("unIgnore").replaceAll("%player%", ignored.getName()));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else if (args.length == 2) {
						if (psender.hasPermission("main.ignore.other")) {
							User user = new User(args[0]);
							User ignored = new User(args[1]);
							if (ignored.isOnline() && user.isOnline()) {
								if (!user.getIgnores().contains(ignored.getName())) {

									if (ignored.getPlayer().hasPermission("main.ignore.bypassignore")) {
										sender.sendMessage(msg.getMessage("IgnoreDenied"));
										return true;
									}

									user.addIgnored(args[0]);
									user.sendMessage(
											msg.getMessage("Ignore").replaceAll("%player%", ignored.getName()));
									ignored.sendMessage(
											msg.getMessage("IgnoreMSG").replaceAll("%player%", user.getName()));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else {
									user.removeIgnored(args[0]);
									user.sendMessage(
											msg.getMessage("unIgnore").replaceAll("%player%", ignored.getName()));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/ignore <player> [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("unignore")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.unignore")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						User ignored = new User(args[0]);
						if (ignored.isOnline()) {
							if (user.getIgnores().contains(ignored.getName())) {
								user.removeIgnored(args[0]);
								sender.sendMessage(
										msg.getMessage("unIgnore").replaceAll("%player%", ignored.getName()));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("NoIgnores"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else if (args.length == 2) {
						if (psender.hasPermission("main.unignore.other")) {
							User user = new User(args[0]);
							User ignored = new User(args[1]);
							if (ignored.isOnline() && user.isOnline()) {
								if (user.getIgnores().contains(ignored.getName())) {
									user.removeIgnored(args[0]);
									user.sendMessage(
											msg.getMessage("unIgnore").replaceAll("%player%", ignored.getName()));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("NoIgnores"));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/unignore <player> [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("socialspy")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.socialspy")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						if (User.getUser(sender.getName()).hasSocialspy()) {
							User.getUser(sender.getName()).setSocialspy(false);
							sender.sendMessage(msg.getMessage("offSpy"));
						} else {
							User.getUser(sender.getName()).setSocialspy(true);
							sender.sendMessage(msg.getMessage("onSpy"));
						}
						return true;

					} else if (args.length == 1) {
						if (psender.hasPermission("main.socialspy.other")) {
							if (User.getUser(args[0]).isOnline()) {
								if (User.getUser(args[0]).hasSocialspy()) {
									User.getUser(args[0]).setSocialspy(false);
									User.getUser(args[0]).sendMessage(msg.getMessage("offSpy"));
								} else {
									User.getUser(args[0]).setSocialspy(true);
									User.getUser(args[0]).sendMessage(msg.getMessage("onSpy"));
								}
								sender.sendMessage(msg.getMessage("DONE"));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("NoPlayer"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/socialspy [player]"));
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
