package mainmc.commands;

import org.apache.commons.lang.NumberUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.PluginLoc;

@SuppressWarnings("deprecation")
public class TeleportCommand implements CommandExecutor {

	public static String[] getCommands() {
		String[] array = { "tp", "tphere", "tptoggle", "tpahere", "tpa", "tpaccept", "tpdeny", "tppos", "tpall",
				"world" };
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("tp")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.tp")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						User suser = new User(sender.getName());
						User tpuser = new User(args[0]);
						if (tpuser.isOnline()) {
							if (User.getUser(tpuser.getName()).isToggled()) {
								sender.sendMessage(msg.getMessage("NoTP"));
								return true;
							}
							suser.teleported(tpuser.getPlayer());
							suser.sendMessage(msg.getMessage("Teleport").replaceAll("%player%", args[0]));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else if (args.length == 2) {
						if (psender.hasPermission("main.tp.other")) {

							User tpuser1 = new User(args[0]);
							User tpuser2 = new User(args[1]);
							if (tpuser1.isOnline() && tpuser2.isOnline()) {
								if (User.getUser(args[0]).isToggled() || User.getUser(args[1]).isToggled()) {
									sender.sendMessage(msg.getMessage("NoTP"));
									return true;
								}
								tpuser1.teleported(tpuser2.getPlayer());
								tpuser1.sendMessage(msg.getMessage("Teleport").replaceAll("%player%", args[1]));
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
						sender.sendMessage("§rUsage: /tp <player> [player]");
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("tphere")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.tphere")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						User suser = new User(sender.getName());
						User tpuser = new User(args[0]);
						if (tpuser.isOnline()) {
							if (User.getUser(args[0]).isToggled()) {
								sender.sendMessage(msg.getMessage("NoTP"));
								return true;
							}
							tpuser.teleported(suser.getPlayer());
							tpuser.sendMessage(msg.getMessage("Teleport").replaceAll("%player%", sender.getName()));
							sender.sendMessage(msg.getMessage("DONE"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /tphere <player>");
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("tpall")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.tpall")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						user.onTpall();
						User.sendAllMessage(msg.getMessage("Teleport").replaceAll("%player%", sender.getName()));
						sender.sendMessage(msg.getMessage("DONE"));
						return true;
					} else if (args.length == 1) {
						if (psender.hasPermission("main.tpall.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								if (User.getUser(args[0]).isToggled()) {
									sender.sendMessage(msg.getMessage("NoTP"));
									return true;
								}
								user.onTpall();
								User.sendAllMessage(msg.getMessage("Teleport").replaceAll("%player%", user.getName()));
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
						sender.sendMessage("§rUsage: /tpall [player]");
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("tppos")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.tppos")) {
					if (args.length == 3) {
						if (NumberUtils.isNumber(args[0]) && NumberUtils.isNumber(args[1])
								|| NumberUtils.isNumber(args[2])) {
							if (!(sender instanceof Player))
								return true;
							User user = new User(sender.getName());
							Location loc = new Location(user.getPlayer().getLocation().getWorld(),
									Double.parseDouble(args[0]), Double.parseDouble(args[1]),
									Double.parseDouble(args[2]));
							user.teleported(loc);
							user.sendMessage(msg.getMessage("Teleport").replaceAll("%player%",
									args[0] + "/" + args[1] + "/" + args[2]));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("Args"));
							return true;
						}
					} else if (args.length == 4) {
						if (psender.hasPermission("main.tppos.other")) {
							if (NumberUtils.isNumber(args[0]) && NumberUtils.isNumber(args[1])
									|| NumberUtils.isNumber(args[2])) {
								User user = new User(args[3]);
								if (User.getUser(args[3]).isToggled()) {
									sender.sendMessage(msg.getMessage("NoTP"));
									return true;
								}
								if (user.isOnline()) {
									Location loc = new Location(user.getPlayer().getLocation().getWorld(),
											Double.parseDouble(args[0]), Double.parseDouble(args[1]),
											Double.parseDouble(args[2]));
									user.teleported(loc);
									user.sendMessage(msg.getMessage("Teleport").replaceAll("%player%",
											args[0] + "/" + args[1] + "/" + args[2]));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("NoPlayer"));
									return true;
								}
							} else {
								sender.sendMessage(msg.getMessage("Args"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /tppos <x> <y> <z> [player]");
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}

			}

			if (cmd.getName().equalsIgnoreCase("tptoggle")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.tptoggle")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						if (User.getUser(sender.getName()).isToggled()) {
							User.getUser(sender.getName()).setToggled(false);
							sender.sendMessage(msg.getMessage("onTP"));
						} else {
							User.getUser(sender.getName()).setToggled(true);
							sender.sendMessage(msg.getMessage("offTP"));
						}
						return true;
					} else if (args.length == 1) {
						if (psender.hasPermission("main.tptoggle.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								if (User.getUser(args[0]).isToggled()) {
									User.getUser(args[0]).setToggled(false);
									User.getUser(args[0]).sendMessage(msg.getMessage("onTP"));
								} else {
									User.getUser(args[0]).setToggled(true);
									User.getUser(args[0]).sendMessage(msg.getMessage("offTP"));
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
						sender.sendMessage("§rUsage /tptoggle [player]");
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}

			}

			if (cmd.getName().equalsIgnoreCase("tpa")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.tpa")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;

						if (sender.getName().equalsIgnoreCase(args[0])) {
							sender.sendMessage(msg.getMessage("SelfTeleport"));
							return true;
						}

						User user = new User(args[0]);
						if (user.isOnline()) {

							if (User.getUser(args[0]).isToggled()) {
								sender.sendMessage(msg.getMessage("NoTP"));
								return true;
							}

							if (User.getUser(sender.getName()).askTpa()) {
								sender.sendMessage(msg.getMessage("Alreadytpa"));
								return true;
							}

							if (User.getUser(args[0]).hasTpaRequest()) {
								sender.sendMessage(msg.getMessage("Alreadytpa"));
								return true;
							}

							User.getUser(args[0]).setRequest(User.getUser(sender.getName()).getPlayer());
							User.getUser(args[0]).setRequestType(0);
							User.getUser(sender.getName()).setSendTpa(true);
							sender.sendMessage(msg.getMessage("tpaRequest").replaceAll("%player%", args[0]));
							User.getUser(args[0])
									.sendMessage(msg.getMessage("tpaReceive").replaceAll("%player%", sender.getName()));
							User.tpaManager(User.getUser(sender.getName()), User.getUser(args[0]));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /tpa <player>");
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("tpahere")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.tpahere")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;

						if (sender.getName().equalsIgnoreCase(args[0])) {
							sender.sendMessage(msg.getMessage("SelfTeleport"));
							return true;
						}

						User user = new User(args[0]);
						if (user.isOnline()) {

							if (User.getUser(args[0]).isToggled()) {
								sender.sendMessage(msg.getMessage("NoTP"));
								return true;
							}

							if (User.getUser(sender.getName()).askTpa()) {
								sender.sendMessage(msg.getMessage("Alreadytpa"));
								return true;
							}

							if (User.getUser(args[0]).hasTpaRequest()) {
								sender.sendMessage(msg.getMessage("Alreadytpa"));
								return true;
							}

							User.getUser(args[0]).setRequest(User.getUser(sender.getName()).getPlayer());
							User.getUser(args[0]).setRequestType(1);
							User.getUser(sender.getName()).setSendTpa(true);
							sender.sendMessage(msg.getMessage("hereRequest").replaceAll("%player%", args[0]));
							User.getUser(args[0]).sendMessage(
									msg.getMessage("hereReceive").replaceAll("%player%", sender.getName()));
							User.tpaManager(User.getUser(sender.getName()), User.getUser(args[0]));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /tpahere <player>");
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("tpaccept")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.tpaccept")) {
					if (!(sender instanceof Player))
						return true;
					if (User.getUser(sender.getName()).hasTpaRequest()) {

						User user = new User(User.getUser(sender.getName()).getRequest().getName());
						if (User.getUser(sender.getName()).getRequestType().equals("TPA")) {
							user.teleported(User.getUser(sender.getName()).getPlayer());
							user.sendMessage(msg.getMessage("Teleport").replaceAll("%player%", sender.getName()));
						} else if (User.getUser(sender.getName()).getRequestType().equals("TPAHERE")) {
							User.getUser(sender.getName()).teleported(user.getPlayer());
							sender.sendMessage(msg.getMessage("Teleport").replaceAll("%player%", user.getName()));
						}
						User.getUser(user.getName()).setSendTpa(false);
						User.getUser(sender.getName()).setRequest(null);
						User.getUser(sender.getName()).setRequestType(2);
						return true;

					} else {
						sender.sendMessage(msg.getMessage("Notpa"));
						return true;
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("tpdeny")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.tpdeny")) {
					if (!(sender instanceof Player))
						return true;
					if (User.getUser(sender.getName()).hasTpaRequest()) {
						User from = new User(User.getUser(sender.getName()).getRequest().getName());
						User.getUser(from.getName()).setSendTpa(false);
						User.getUser(sender.getName()).setRequest(null);
						User.getUser(sender.getName()).setRequestType(2);
						User.getUser(from.getName()).sendMessage(msg.getMessage("Reject"));
						sender.sendMessage(msg.getMessage("DONE"));
						return true;
					} else {
						sender.sendMessage(msg.getMessage("Notpa"));
						return true;
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("world")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.world")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						if (PluginLoc.findWorld(args[0]) != null) {
							World world = PluginLoc.findWorld(args[0]);
							user.teleported(world.getSpawnLocation());
							sender.sendMessage(msg.getMessage("World").replaceAll("%world%", args[0]));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoWorld"));
							return true;
						}
					} else if (args.length == 2) {
						if (psender.hasPermission("main.world.other")) {
							User user = new User(args[1]);
							if (user.isOnline()) {
								if (User.getUser(args[1]).isToggled()) {
									sender.sendMessage(msg.getMessage("NoTP"));
									return true;
								}
								if (PluginLoc.findWorld(args[0]) != null) {
									World world = PluginLoc.findWorld(args[0]);
									user.teleported(world.getSpawnLocation());
									user.sendMessage(msg.getMessage("World").replaceAll("%world%", args[0]));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("NoWorld"));
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
						sender.sendMessage("§rUsage: /world <world> [player]");
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
