package mainmc.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.Ip;
import mainmc.nothing00.functions.User;

public class AddressCommand implements CommandExecutor {

	public static String[] getCommands() {
		String[] array = { "banip", "unbanip", "seen", "resethost" };
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("banip")) {
				Messages msg = new Messages();
				if (sender.hasPermission("main.banip")) {
					Conf config = new Conf();
					if (args.length == 1) {
						Ip ip = new Ip(args[0]);
						if (ip.isAddress()) {

							if (ip.isBanned()) {
								sender.sendMessage(msg.getMessage("AlreadyBannedIP"));
								return true;
							}

							ip.banIp(config.getBanDefaultMotiv());
							sender.sendMessage(msg.getMessage("BanIP").replaceAll("%ip%", ip.toString()));
							if (ip.getUsersWithThisAddress().isEmpty()) {
								sender.sendMessage(msg.getMessage("BanIPNoUser"));
							} else {
								sender.sendMessage(msg.getMessage("BanIPUser").replaceAll("%players%", "\n§e"
										+ String.join("§7, §e", ip.getUsersWithThisAddress().toArray(new String[0]))));
							}
							return true;
						} else {
							User user = new User(args[0]);
							if (user.exists()) {

								if (user.isUntouch() && !(sender instanceof ConsoleCommandSender)) {
									sender.sendMessage(msg.getMessage("Untouchable"));
									return true;
								}

								Ip ipuser = new Ip(user.getCurrentIp());

								if (ipuser.isBanned()) {
									sender.sendMessage(msg.getMessage("AlreadyBannedIP"));
									return true;
								}

								ipuser.banIp(config.getBanDefaultMotiv());
								sender.sendMessage(msg.getMessage("BanIP").replaceAll("%ip%", ipuser.toString()));
								if (ipuser.getUsersWithThisAddress().isEmpty()) {
									sender.sendMessage(msg.getMessage("BanIPNoUser"));
								} else {
									sender.sendMessage(msg.getMessage("BanIPUser").replaceAll("%players%",
											"\n§e" + String.join("§7, §e",
													ipuser.getUsersWithThisAddress().toArray(new String[0]))));
								}
								return true;
							} else {
								sender.sendMessage(msg.getMessage("NoIP"));
								return true;
							}
						}
					} else if (args.length > 2) {
						String motiv = "";
						for (int i = 1; i < args.length; i++) {
							motiv += args[i].replaceAll("&", "§");
						}
						Ip ip = new Ip(args[0]);
						if (ip.isAddress()) {

							if (ip.isBanned()) {
								sender.sendMessage(msg.getMessage("AlreadyBannedIP"));
								return true;
							}

							ip.banIp(motiv);
							sender.sendMessage(msg.getMessage("BanIP").replaceAll("%ip%", ip.toString()));
							if (ip.getUsersWithThisAddress().isEmpty()) {
								sender.sendMessage(msg.getMessage("BanIPNoUser"));
							} else {
								sender.sendMessage(msg.getMessage("BanIPUser").replaceAll("%players%", "\n§e"
										+ String.join("§7, §e", ip.getUsersWithThisAddress().toArray(new String[0]))));
							}
							return true;
						} else {
							User user = new User(args[0]);
							if (user.exists()) {

								if (user.isUntouch() && !(sender instanceof ConsoleCommandSender)) {
									sender.sendMessage(msg.getMessage("Untouchable"));
									return true;
								}

								Ip ipuser = new Ip(user.getCurrentIp());

								if (ipuser.isBanned()) {
									sender.sendMessage(msg.getMessage("AlreadyBannedIP"));
									return true;
								}

								ipuser.banIp(motiv);
								sender.sendMessage(msg.getMessage("BanIP").replaceAll("%ip%", ipuser.toString()));
								if (ipuser.getUsersWithThisAddress().isEmpty()) {
									sender.sendMessage(msg.getMessage("BanIPNoUser"));
								} else {
									sender.sendMessage(msg.getMessage("BanIPUser").replaceAll("%players%",
											"\n§e" + String.join("§7, §e",
													ipuser.getUsersWithThisAddress().toArray(new String[0]))));
								}
								return true;
							} else {
								sender.sendMessage(msg.getMessage("NoIP"));
								return true;
							}
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/banip <player/address> [motivation]"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("unbanip")) {
				Messages msg = new Messages();
				if (sender.hasPermission("main.unbanip")) {
					if (args.length == 1) {
						Ip ip = new Ip(args[0]);
						if (ip.isAddress()) {

							if (!ip.isBanned()) {
								sender.sendMessage(msg.getMessage("NoBanIP"));
								return true;
							}

							ip.unBanIp();
							sender.sendMessage(msg.getMessage("UnBanIP").replaceAll("%ip%", ip.toString()));
							if (ip.getUsersWithThisAddress().isEmpty()) {
								sender.sendMessage(msg.getMessage("UnBanIPNoUser"));
							} else {
								sender.sendMessage(msg.getMessage("UnBanIPUser").replaceAll("%players%", "\n§e"
										+ String.join("§7, §e", ip.getUsersWithThisAddress().toArray(new String[0]))));
							}
							return true;
						} else {
							User user = new User(args[0]);
							if (user.exists()) {

								Ip ipuser = new Ip(user.getCurrentIp());

								if (!ipuser.isBanned()) {
									sender.sendMessage(msg.getMessage("NoBanIP"));
									return true;
								}

								ipuser.unBanIp();
								sender.sendMessage(msg.getMessage("UnBanIP").replaceAll("%ip%", ipuser.toString()));
								if (ipuser.getUsersWithThisAddress().isEmpty()) {
									sender.sendMessage(msg.getMessage("UnBanIPNoUser"));
								} else {
									sender.sendMessage(msg.getMessage("UnBanIPUser").replaceAll("%players%",
											"\n§e" + String.join("§7, §e",
													ipuser.getUsersWithThisAddress().toArray(new String[0]))));
								}
								return true;
							} else {
								sender.sendMessage(msg.getMessage("NoIP"));
								return true;
							}
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/unbanip <player/address>"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("seen")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.seen")) {
					if (args.length == 1) {
						Ip ip = new Ip(args[0]);
						User user = new User(args[0]);
						if (ip.isAddress()) {
							if (ip.getUsersWithThisAddress().isEmpty()) {
								sender.sendMessage(msg.getMessage("AnyIP"));
							} else {
								sender.sendMessage(msg.getMessage("SeenIP").replaceAll("%ip%", ip.toString())
										.replaceAll("%player%", sender.getName()) + "§e"
										+ String.join("§7, §e", ip.getUsersWithThisAddress().toArray(new String[0])));
							}
							return true;
						} else if (user.exists()) {
							sender.sendMessage(msg.getMessage("SeenUser").replaceAll("%ip%", user.getCurrentHost())
									.replaceAll("%player%", user.getName()) + "\n§e"
									+ String.join("§7, §e", user.getOldAdresses().toArray(new String[0])));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoIP"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/seen <player/address>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("resethost")) {
				Messages msg = new Messages();
				if (sender instanceof ConsoleCommandSender) {
					if (args.length == 1) {
						User user = new User(args[0]);
						if (user.exists()) {
							user.resetHost();
							sender.sendMessage(msg.getMessage("DONE"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/resethost <player>"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm") + " (only CONSOLE)");
					return true;
				}
			}

		}
		return false;
	}

}
