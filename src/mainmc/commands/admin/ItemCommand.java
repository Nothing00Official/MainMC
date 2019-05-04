package mainmc.commands.admin;

import org.apache.commons.lang.NumberUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.ItemPlugin;
import mainmc.nothing00.functions.User;

@SuppressWarnings("deprecation")
public class ItemCommand implements CommandExecutor {

	public static String[] getCommands() {
		String[] array = { "give", "item", "enchant", "icadd" };
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			MainPermissions psender = new MainPermissions(sender);

			if (cmd.getName().equalsIgnoreCase("give")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.give")) {
					if (args.length >= 3) {
						User user = new User(args[0]);
						if (user.isOnline()) {
							ItemStack item = ItemPlugin.getItemString(args[1]);
							if (NumberUtils.isNumber(args[2])) {
								item.setAmount(Integer.parseInt(args[2]));
								ItemPlugin it = new ItemPlugin(item);

								if (args.length > 3) {
									for (int i = 3; i < args.length; i++) {
										if (args[i].contains("name:")) {
											it.setName(args[i]);
										} else if (args[i].contains(":")) {
											it.enchant(args[i], it.getItem());
										}
									}
								}

								user.addItem(it.getItem());

								user.sendMessage(msg.getMessage("Give").replaceAll("%n%", "" + it.getItem().getAmount())
										.replaceAll("%player%", sender.getName())
										.replaceAll("%item%", it.getItem().getType().toString()));

								if (!user.getName().equals(sender.getName())) {
									sender.sendMessage(msg.getMessage("DONE"));
								}

								return true;
							}
							sender.sendMessage(msg.getMessage("Args"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage(
								"§rUsage: /give <player> <item> <amount> [enchant:level] [name:displayname]");
						return true;
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("item")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.item")) {
					if (args.length >= 2) {

						if (!(sender instanceof Player))
							return true;

						User user = new User(sender.getName());

						ItemStack item = ItemPlugin.getItemString(args[0]);
						if (NumberUtils.isNumber(args[1])) {
							item.setAmount(Integer.parseInt(args[1]));
							ItemPlugin it = new ItemPlugin(item);

							if (args.length > 2) {
								for (int i = 2; i < args.length; i++) {
									if (args[i].contains("name:")) {
										it.setName(args[i]);
									} else if (args[i].contains(":")) {
										it.enchant(args[i], it.getItem());
									}
								}
							}

							user.addItem(it.getItem());

							user.sendMessage(msg.getMessage("Give").replaceAll("%n%", "" + it.getItem().getAmount())
									.replaceAll("%player%", sender.getName())
									.replaceAll("%item%", it.getItem().getType().toString()));

							return true;
						}
						sender.sendMessage(msg.getMessage("Args"));
						return true;

					} else {
						sender.sendMessage("§rUsage: /item <item> <amount> [enchant:level] [name:displayname]");
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("enchant")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.enchant")) {
					if (args.length == 2) {
						if (!(sender instanceof Player))
							return true;

						User user = new User(sender.getName());

						if (user.getItem() == null) {
							sender.sendMessage(msg.getMessage("ItemHand"));
							return true;
						}

						ItemPlugin it = new ItemPlugin(user.getItem());
						it.enchant(args[0] + ":" + args[1], it.getItem());
						user.getPlayer().updateInventory();
						return true;
					} else if (args.length == 3) {
						if (psender.hasPermission("main.enchant.other")) {
							User user = new User(args[2]);
							if (user.isOnline()) {
								if (user.getItem() == null) {
									sender.sendMessage(msg.getMessage("ItemHand"));
									return true;
								}

								ItemPlugin it = new ItemPlugin(user.getItem());
								it.enchant(args[0] + ":" + args[1], it.getItem());
								user.getPlayer().updateInventory();
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
						sender.sendMessage("§rUsage: /enchant <enchantment> <level> [player]");
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("icadd")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.item.addcommand")) {
					if (args.length > 2) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						if (user.getItem() != null && !user.getItem().getType().equals(Material.AIR)) {

							String com = args[1];
							for (int i = 2; i < args.length; i++) {
								com += " " + args[i];
							}
							ItemPlugin item = new ItemPlugin(user.getItem());
							if (item.isBankCheck())
								return true;
							if (item.isSpawner())
								return true;
							if (args[0].equalsIgnoreCase("console")) {

								user.setItem(item.addCommand(com, true));
								sender.sendMessage(msg.getMessage("DONE"));
								return true;
							} else if (args[0].equalsIgnoreCase("player")) {
								user.setItem(item.addCommand(com, false));
								sender.sendMessage(msg.getMessage("DONE"));
								return true;
							} else {
								sender.sendMessage("§rUsage: /icadd <console/player> <command>");
							}
						} else {
							sender.sendMessage(msg.getMessage("NoItem"));
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /icadd <console/player> <command>");
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
