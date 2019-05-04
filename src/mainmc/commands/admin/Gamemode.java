package mainmc.commands.admin;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;

public class Gamemode implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"gamemode", "gmc", "gma", "gms"};
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("gmc")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.gamemode")) {
					if (args.length == 1) {
						if (psender.hasPermission("main.gamemode.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								user.setGamemode(GameMode.CREATIVE);
								user.sendMessage(
										msg.getMessage("GameMode").replaceAll("%gm%", GameMode.CREATIVE.toString()));
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
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						user.setGamemode(GameMode.CREATIVE);
						sender.sendMessage(msg.getMessage("GameMode").replaceAll("%gm%", GameMode.CREATIVE.toString()));
						return true;
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("gms")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.gamemode")) {
					if (args.length == 1) {
						if (psender.hasPermission("main.gamemode.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								user.setGamemode(GameMode.SURVIVAL);
								user.sendMessage(
										msg.getMessage("GameMode").replaceAll("%gm%", GameMode.SURVIVAL.toString()));
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
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						user.setGamemode(GameMode.SURVIVAL);
						sender.sendMessage(msg.getMessage("GameMode").replaceAll("%gm%", GameMode.SURVIVAL.toString()));
						return true;
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("gma")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.gamemode")) {
					if (args.length == 1) {
						if (psender.hasPermission("main.gamemode.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								user.setGamemode(GameMode.ADVENTURE);
								user.sendMessage(
										msg.getMessage("GameMode").replaceAll("%gm%", GameMode.ADVENTURE.toString()));
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
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						user.setGamemode(GameMode.ADVENTURE);
						sender.sendMessage(
								msg.getMessage("GameMode").replaceAll("%gm%", GameMode.ADVENTURE.toString()));
						return true;
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("gamemode")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.gamemode")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						switch (args[0]) {
						case "0":
							user.setGamemode(GameMode.SURVIVAL);
							sender.sendMessage(
									msg.getMessage("GameMode").replaceAll("%gm%", GameMode.SURVIVAL.toString()));
							break;
						case "1":
							user.setGamemode(GameMode.CREATIVE);
							sender.sendMessage(
									msg.getMessage("GameMode").replaceAll("%gm%", GameMode.CREATIVE.toString()));
							break;
						case "2":
							user.setGamemode(GameMode.ADVENTURE);
							sender.sendMessage(
									msg.getMessage("GameMode").replaceAll("%gm%", GameMode.ADVENTURE.toString()));
							break;
						case "3":
							user.setGamemode(GameMode.SPECTATOR);
							sender.sendMessage(
									msg.getMessage("GameMode").replaceAll("%gm%", GameMode.SPECTATOR.toString()));
							break;
						default:
							sender.sendMessage("§rUsage: /gamemode <GameMode> [player]");
							break;

						}
						return true;
					} else if (args.length == 2) {
						if (psender.hasPermission("main.gamemode.other")) {
							User user = new User(args[1]);
							if (user.isOnline()) {
								switch (args[0]) {
								case "0":
									user.setGamemode(GameMode.SURVIVAL);
									user.sendMessage(msg.getMessage("GameMode").replaceAll("%gm%",
											GameMode.SURVIVAL.toString()));
									break;
								case "1":
									user.setGamemode(GameMode.CREATIVE);
									user.sendMessage(msg.getMessage("GameMode").replaceAll("%gm%",
											GameMode.CREATIVE.toString()));
									break;
								case "2":
									user.setGamemode(GameMode.ADVENTURE);
									user.sendMessage(msg.getMessage("GameMode").replaceAll("%gm%",
											GameMode.ADVENTURE.toString()));
									break;
								case "3":
									user.setGamemode(GameMode.SPECTATOR);
									user.sendMessage(msg.getMessage("GameMode").replaceAll("%gm%",
											GameMode.SPECTATOR.toString()));
									break;
								default:
									sender.sendMessage("§rUsage: /gamemode <GameMode> [player]");
									break;

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
