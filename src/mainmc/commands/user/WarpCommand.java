package mainmc.commands.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.functions.Warp;
import mainmc.nothing00.functions.gui.WarpGui;

public class WarpCommand implements CommandExecutor {

	public static String[] getCommands() {
		String[] array = { "warp", "delwarp", "setwarp", "warps" };
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("warps")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.warp") && psender.hasPermission("main.warps")) {

					if (Warp.getWarps().isEmpty()) {
						sender.sendMessage(msg.getMessage("NoWarps"));
						return true;
					}

					Conf config = new Conf();
					if (config.warpGui()) {
						if (sender instanceof Player) {
							WarpGui warp = new WarpGui((Player) sender);
							warp.openGui();
						}
					} else {
						sender.sendMessage(msg.getMessage("Warps") + "\n§e"
								+ String.join("§7, §e", Warp.getWarps().toArray(new String[0])));
					}
					return true;
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("setwarp")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.setwarp")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						Warp warp = new Warp(args[0]);
						if (!warp.exists()) {
							warp.setWarp(((Player) sender).getLocation());
							sender.sendMessage(msg.getMessage("WarpCreated").replaceAll("%warp%", args[0]));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("WarpExist"));
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /setwarp <warp>");
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("delwarp")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.delwarp")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						Warp warp = new Warp(args[0]);
						if (warp.exists()) {
							warp.delWarp();
							sender.sendMessage(msg.getMessage("WarpRemoved").replaceAll("%warp%", args[0]));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("WarpNoExist"));
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /delwarp <warp>");
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("warp")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.warp")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						Warp warp = new Warp(args[0]);
						if (warp.hasWarpPermissions((Player) sender)) {
							if (warp.exists()) {
								if (warp.getWarp() != null) {

									warp.onTeleport((Player) sender);

									return true;
								} else {
									sender.sendMessage(msg.getMessage("InvalidWarp"));
									return true;
								}
							} else {
								sender.sendMessage(msg.getMessage("WarpNoExist"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else if (args.length == 2) {
						if (psender.hasPermission("main.warp.other")) {
							Warp warp = new Warp(args[0]);
							if (warp.exists()) {
								if (warp.getWarp() != null) {
									User user = new User(args[1]);
									if (user.isOnline()) {
										user.teleported(warp.getWarp());
										user.sendMessage(msg.getMessage("Warp").replaceAll("%warp%", args[0]));
										sender.sendMessage(msg.getMessage("DONE"));
										return true;
									} else {
										sender.sendMessage(msg.getMessage("NoPlayer"));
										return true;
									}
								} else {
									sender.sendMessage(msg.getMessage("InvalidWarp"));
									return true;
								}
							} else {
								sender.sendMessage(msg.getMessage("WarpNoExist"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else if (args.length == 0) {
						if (sender.hasPermission("main.warps")) {
							if (Warp.getWarps().isEmpty()) {
								sender.sendMessage(msg.getMessage("NoWarps"));
								return true;
							}
							Conf config = new Conf();
							if (config.warpGui()) {
								if (sender instanceof Player) {
									WarpGui warp = new WarpGui((Player) sender);
									warp.openGui();
								}
							} else {
								sender.sendMessage(msg.getMessage("Warps") + "\n§e"
										+ String.join("§7, §e", Warp.getWarps().toArray(new String[0])));
							}

							return true;
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /warp <warp> [player]");
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
