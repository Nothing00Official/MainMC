package mainmc.commands.user;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.Kit;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.functions.gui.KitGui;
import mainmc.nothing00.utils.Date;
import mainmc.nothing00.utils.Time;

public class KitCommand implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"kit", "kits", "kitcreator"};
		return array;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		
		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("kit")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.kit")) {
					if (args.length == 0) {
						if (psender.hasPermission("main.kits")) {
							if (!(sender instanceof Player))
								return true;
							Conf config = new Conf();
							if (config.kitGui()) {
								KitGui gui = new KitGui((Player) sender);
								gui.openGui();
								return true;
							} else {
								Kit kit = new Kit("", (Player) sender);
								if (kit.getPermittendKits().isEmpty()) {
									sender.sendMessage(msg.getMessage("NoKits"));
									return true;
								}
								sender.sendMessage(msg.getMessage("KitList") + "\n§e"
										+ String.join("§7, §e", kit.getPermittendKits().toArray(new String[0])));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						Kit kit = new Kit(args[0], (Player) sender);
						if (kit.hasPermissionKit()) {
							if (kit.exists()) {
								if (kit.hasDelay()) {

									Time date = new Time(kit.getExpire());

									if (date.isPastOrPresent() || sender.hasPermission("main.kits.bypassdelay")) {
										kit.giveKit();
										if (!sender.hasPermission("main.kits.bypassdelay"))
											kit.setDelay();
										sender.sendMessage(msg.getMessage("Kit").replaceAll("%kit%", args[0]));
										return true;
									} else {
										Time expire = new Time(kit.getExpire());
										Time time = new Time(new Date());
										String datetime = Time.getStringTimeFromSecond(
												(int) (expire.getDateInSeconds() - time.getDateInSeconds()));
										sender.sendMessage(
												msg.getMessage("KitCountdown").replaceAll("%time%", datetime));
										return true;
									}

								} else {
									kit.giveKit();
									if (!sender.hasPermission("main.kits.bypassdelay"))
										kit.setDelay();
									sender.sendMessage(msg.getMessage("Kit").replaceAll("%kit%", args[0]));
									return true;
								}
							} else {
								sender.sendMessage(msg.getMessage("KitExist"));
								return true;
							}

						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else if (args.length == 2) {
						if (psender.hasPermission("main.kits.other")) {

							User user = new User(args[1]);

							if (user.isOnline()) {
								Kit kit = new Kit(args[0], user.getPlayer());
								if (kit.exists()) {
									kit.giveKit();
									sender.sendMessage(msg.getMessage("DONE"));
									user.sendMessage(msg.getMessage("Kit").replaceAll("%kit%", args[0]));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("KitExist"));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/kit <kit> [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("kits")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.kits")) {
					if (!(sender instanceof Player))
						return true;
					Conf config = new Conf();
					if (config.kitGui()) {
						KitGui gui = new KitGui((Player) sender);
						gui.openGui();
						return true;
					} else {
						Kit kit = new Kit("", (Player) sender);
						if (kit.getPermittendKits().isEmpty()) {
							sender.sendMessage(msg.getMessage("NoKits"));
							return true;
						}
						sender.sendMessage(msg.getMessage("KitList") + "\n§e"
								+ String.join("§7, §e", kit.getPermittendKits().toArray(new String[0])));
						return true;
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("kitcreator")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.kits.creator")) {
					if (args.length == 2) {
						if (args[0].equalsIgnoreCase("create")) {
							if (!(sender instanceof Player))
								return true;
							Kit kit = new Kit(args[1], (Player) sender);
							if (kit.exists()) {
								sender.sendMessage(msg.getMessage("KitAlreadyExist"));
								return true;
							}
							kit.openCreatorInventory();
						} else if (args[0].equalsIgnoreCase("delete")) {
							Kit kit = new Kit(args[1], null);
							if (!kit.exists()) {
								sender.sendMessage(msg.getMessage("KitExist"));
								return true;
							}
							kit.deleteKit();
							sender.sendMessage(msg.getMessage("Deleted"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/kitcreator <create/delete> <kit>"));
						}
					} else if (args.length == 3) {
						if (args[0].equalsIgnoreCase("setdelay")) {
							Kit kit = new Kit(args[1], null);
							if (!kit.exists()) {
								sender.sendMessage(msg.getMessage("KitExist"));
								return true;
							}
							if (!NumberUtils.isNumber(args[2])) {
								sender.sendMessage(msg.getMessage("Args"));
								return true;
							}
							kit.setDelay(Integer.parseInt(args[2]));
							sender.sendMessage(msg.getMessage("setDelay"));
							return true;
						}
						if (args[0].equalsIgnoreCase("setbook")) {
							Kit kit = new Kit(args[1], null);
							if (!kit.exists()) {
								sender.sendMessage(msg.getMessage("KitExist"));
								return true;
							}
							if (NumberUtils.isNumber(args[2])) {
								sender.sendMessage(msg.getMessage("Args"));
								return true;
							}
							kit.setBook(args[2]);
							sender.sendMessage(msg.getMessage("setBook"));
							return true;
						} else if (args[0].equalsIgnoreCase("setalias")) {
							Kit kit = new Kit(args[1], null);
							Kit kit2 = new Kit(args[2], null);
							if (!kit.exists() || !kit2.exists()) {
								sender.sendMessage(msg.getMessage("KitExist"));
								return true;
							}

							kit.setAlias(kit2);
							sender.sendMessage(msg.getMessage("setAlias"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/kitcreator <setdelay/setalias> <kit> <delay/alias>"));
						}
					} else if (args.length > 4) {
						if (args[0].equalsIgnoreCase("setcmd")) {
							Kit kit = new Kit(args[1], null);
							if (!kit.exists()) {
								sender.sendMessage(msg.getMessage("KitExist"));
								return true;
							}
							String comm = "";
							for (int i = 2; i < args.length; i++) {
								comm += " " + args[i];
							}
							kit.setCmd(comm);
							sender.sendMessage(msg.getMessage("setCmd"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/kitcreator <setcmd> <kit> <command>"));
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/kitcreator create <kit>"));
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/kitcreator setdelay <kit> <delay>"));
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/kitcreator setalias <kit> <aliasKit>"));
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/kitcreator setcmd <kit> <cmd>"));
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/kitcreator setbook <kit> <book>"));
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/kitcreator delete <kit>"));
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
