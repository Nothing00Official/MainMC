package mainmc.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Help;
import mainmc.folders.Messages;
import mainmc.folders.Motd;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.AfkManager;
import mainmc.nothing00.utils.Time;

public class BasicCommand implements CommandExecutor {

	public static String[] getCommands() {
		String[] array = { "ptime", "list", "back", "ping", "help", "motd", "afk" };
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("ptime")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.ptime")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						User user = User.getUser(sender.getName());
						sender.sendMessage(msg.getMessage("Time").replaceAll("%player%", user.getName())
								.replaceAll("%time%", Time.getStringTimeFromSecond((user.getOnlineTime()))));
						return true;
					} else if (args.length == 1) {
						if (psender.hasPermission("main.ptime.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								sender.sendMessage(msg.getMessage("Time").replaceAll("%player%", user.getName())
										.replaceAll("%time%",
												Time.getStringTimeFromSecond((User.getUser(args[0]).getOnlineTime()))));
							} else if (user.exists()) {
								sender.sendMessage(msg.getMessage("Time").replaceAll("%player%", user.getName())
										.replaceAll("%time%", Time.getStringTimeFromSecond((user.getOnlineTime()))));
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
						sender.sendMessage("§rUsage: /ptime [player]");
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("list")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.list")) {
					if (psender.hasPermission("main.list.viewall")) {
						sender.sendMessage(User.viewOpList());
						return true;
					} else {
						sender.sendMessage(User.viewList());
						return true;
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}

			}

			if (cmd.getName().equalsIgnoreCase("back")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.back")) {
					if (!(sender instanceof Player))
						return true;
					if (User.getUser(sender.getName()).getLastLocation() == null) {
						sender.sendMessage(msg.getMessage("NoBack"));
						return true;
					}

					Conf config = new Conf();

					if (config.backDelay() && !sender.hasPermission("main.back.bypassdelay")) {
						sender.sendMessage(msg.getMessage("Countdown").replaceAll("%location%", "BACK")
								.replaceAll("%time%", config.getBackDelay() + ""));
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin.plugin, new Runnable() {
							@Override
							public void run() {
								User.getUser(sender.getName())
										.teleported(User.getUser(sender.getName()).getLastLocation());
								sender.sendMessage(msg.getMessage("Back"));
							}
						}, config.getBackDelay() * 20);
					} else {
						User.getUser(sender.getName()).teleported(User.getUser(sender.getName()).getLastLocation());
						sender.sendMessage(msg.getMessage("Back"));
					}

					return true;
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("motd")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.motd")) {
					if (sender instanceof Player) {
						User user = new User(sender.getName());
						user.showMotd();
						return true;
					} else if (sender instanceof ConsoleCommandSender) {
						Motd motd = new Motd();
						System.out.println(motd.getMotd());
						System.out.println(motd.showMotd());
						return true;
					} else {
						return true;
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("ping")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.ping")) {
					if (args.length == 0) {

						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());

						sender.sendMessage(msg.getMessage("Ping").replaceAll("%player%", sender.getName())
								.replaceAll("%ping%", "" + user.getPing()));
						return true;
					} else if (args.length == 1) {
						if (psender.hasPermission("main.ping.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {

								sender.sendMessage(msg.getMessage("Ping").replaceAll("%player%", user.getName())
										.replaceAll("%ping%", "" + user.getPing()));
							} else {
								sender.sendMessage(msg.getMessage("NoPlayer"));
							}
						} else {
							sender.sendMessage("Pong :)");
						}
					} else {
						sender.sendMessage("§rUsage: /ping [player]");
					}
				} else {
					sender.sendMessage("Pong :)");
				}
			}

			if (cmd.getName().equalsIgnoreCase("help")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.help")) {
					if (args.length == 0) {
						Help help = new Help();
						if (help.isNull()) {
							sender.sendMessage(msg.getMessage("NoPage"));
							return true;
						}
						sender.sendMessage(help.showPage());
						return true;
					} else if (args.length == 1) {
						Help help = new Help(args[0]);
						if (help.isNull()) {
							sender.sendMessage(msg.getMessage("NoPage"));
							return true;
						}
						sender.sendMessage(help.showPage());
						return true;
					} else {
						sender.sendMessage("§rUsage: /help <page>");
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("afk")) {
				Messages msg = new Messages();
				Conf config = new Conf();
				if (psender.hasPermission("main.afk")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						AfkManager afk = new AfkManager(user.getPlayer());
						if (afk.isAfk()) {
							afk.removeAfk();
							if (!config.cancelAfkonMove())
								User.sendAllMessage(msg.getMessage("offAFK").replaceAll("%player%", user.getName()));
						} else {
							afk.setAfk();
							User.sendAllMessage(msg.getMessage("onAFK").replaceAll("%player%", user.getName()));
						}
						return true;
					} else if (args.length == 1) {
						User user = new User(args[0]);
						if (user.isOnline() && psender.hasPermission("main.afk.other")) {
							AfkManager afk = new AfkManager(user.getPlayer());
							if (afk.isAfk()) {
								afk.removeAfk();
								if (!config.cancelAfkonMove())
									User.sendAllMessage(
											msg.getMessage("offAFK").replaceAll("%player%", user.getName()));
							} else {
								afk.setAfk();
								User.sendAllMessage(msg.getMessage("onAFK").replaceAll("%player%", user.getName()));
							}
							sender.sendMessage(msg.getMessage("DONE"));
							return true;
						} else {
							if (!(sender instanceof Player))
								return true;
							user = new User(sender.getName());
							AfkManager afk = new AfkManager(user.getPlayer());
							if (afk.isAfk()) {
								afk.removeAfk();
								if (!config.cancelAfkonMove())
									User.sendAllMessage(
											msg.getMessage("offAFK").replaceAll("%player%", user.getName()));
							} else {
								afk.setAfk();
								User.sendAllMessage(msg.getMessage("onAFK").replaceAll("%player%",
										user.getName() + " - " + args[0]));
							}
							return true;
						}
					} else if (args.length > 1) {
						User user = new User(args[0]);
						if (user.isOnline() && psender.hasPermission("main.afk.other")) {
							AfkManager afk = new AfkManager(user.getPlayer());
							String cause = "";
							for (int i = 1; i < args.length; i++) {
								cause += " " + args[i];
							}
							if (afk.isAfk()) {
								afk.removeAfk();
								if (!config.cancelAfkonMove())
									User.sendAllMessage(
											msg.getMessage("offAFK").replaceAll("%player%", user.getName()));
							} else {
								afk.setAfk();
								User.sendAllMessage(
										msg.getMessage("onAFK").replaceAll("%player%", user.getName() + " -" + cause));
							}
							sender.sendMessage(msg.getMessage("DONE"));
							return true;
						} else {
							if (!(sender instanceof Player))
								return true;
							user = new User(sender.getName());
							AfkManager afk = new AfkManager(user.getPlayer());
							String cause = "";
							for (int i = 0; i < args.length; i++) {
								cause += " " + args[i];
							}
							if (afk.isAfk()) {
								afk.removeAfk();
								if (!config.cancelAfkonMove())
									User.sendAllMessage(
											msg.getMessage("offAFK").replaceAll("%player%", user.getName()));
							} else {
								afk.setAfk();
								User.sendAllMessage(
										msg.getMessage("onAFK").replaceAll("%player%", user.getName() + " -" + cause));
							}
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /afk [player] [cause]");
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
