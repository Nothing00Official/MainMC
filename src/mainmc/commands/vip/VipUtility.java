package mainmc.commands.vip;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.functions.ItemPlugin;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.PluginLoc;

public class VipUtility implements CommandExecutor {

	public static String[] getCommands() {
		String[] array = { "feed", "heal", "god", "repair", "workbench", "clear", "fly", "suicide", "getpos",
				"lastlocation", "skull", "burn", "ext", "nightvision", "compact" };
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("feed")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.feed")) {
					if (args.length == 0) {

						if (!(sender instanceof Player))
							return true;

						Conf config = new Conf();
						User user = new User(sender.getName());

						if (config.feedDelay() && !sender.hasPermission("main.feed.bypassdelay")) {
							user.sendMessage(
									msg.getMessage("FeedCountdown").replaceAll("%time%", "" + config.getFeedDelay()));
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin.plugin,
									new Runnable() {
										@Override
										public void run() {
											user.feed();
											user.sendMessage(msg.getMessage("Feed"));
										}
									}, config.getFeedDelay() * 20);
							return true;
						} else {
							user.feed();
							user.sendMessage(msg.getMessage("Feed"));
							return true;
						}
					} else if (args.length == 1) {
						if (psender.hasPermission("main.feed.other")) {
							User user = new User(args[0]);

							if (user.isOnline()) {
								user.feed();
								user.sendMessage(msg.getMessage("Feed"));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/feed [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}

			}

			if (cmd.getName().equalsIgnoreCase("heal")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.heal")) {
					if (args.length == 0) {

						if (!(sender instanceof Player))
							return true;

						Conf config = new Conf();
						User user = new User(sender.getName());

						if (config.feedDelay() && !sender.hasPermission("main.heal.bypassdelay")) {
							user.sendMessage(
									msg.getMessage("HealCountdown").replaceAll("%time%", "" + config.getHealDelay()));
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin.plugin,
									new Runnable() {
										@Override
										public void run() {
											user.heal();
											user.sendMessage(msg.getMessage("Heal"));
										}
									}, config.getHealDelay() * 20);
							return true;
						} else {
							user.heal();
							user.sendMessage(msg.getMessage("Heal"));
							return true;
						}
					} else if (args.length == 1) {
						if (psender.hasPermission("main.heal.other")) {
							User user = new User(args[0]);

							if (user.isOnline()) {
								user.heal();
								user.sendMessage(msg.getMessage("Heal"));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/heal [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("god")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.god")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						Conf config = new Conf();
						if (config.getBlackListWorldsGod()
								.contains(((Player) sender).getLocation().getWorld().getName())) {
							sender.sendMessage(msg.getMessage("GodWorld"));
							return true;
						}

						if (User.getUser(sender.getName()).isGod()) {
							User.getUser(sender.getName()).setGod(false);
							User.getUser(sender.getName()).sendMessage(msg.getMessage("offGod"));
						} else {
							User.getUser(sender.getName()).setGod(true);
							User.getUser(sender.getName()).sendMessage(msg.getMessage("onGod"));
						}
						return true;

					} else if (args.length == 1) {

						User user = new User(args[0]);
						if (user.isOnline()) {
							if (psender.hasPermission("main.god.other")) {
								Conf config = new Conf();
								if (config.getBlackListWorldsGod()
										.contains(user.getPlayer().getLocation().getWorld().getName())) {
									sender.sendMessage(msg.getMessage("GodWorld"));
									return true;
								}

								if (User.getUser(args[0]).isGod()) {
									User.getUser(args[0]).setGod(false);
									User.getUser(args[0]).sendMessage(msg.getMessage("offGod"));
								} else {
									User.getUser(args[0]).setGod(true);
									User.getUser(args[0]).sendMessage(msg.getMessage("onGod"));
								}
								sender.sendMessage(msg.getMessage("DONE"));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}
						} else if (args[0].equalsIgnoreCase("enable")) {
							if (!(sender instanceof Player))
								return true;
							if (User.getUser(sender.getName()).isGod())
								return true;
							User.getUser(sender.getName()).setGod(true);
							sender.sendMessage(msg.getMessage("onGod"));
							return true;
						} else if (args[0].equalsIgnoreCase("disable")) {
							if (!(sender instanceof Player))
								return true;
							if (!User.getUser(sender.getName()).isGod())
								return true;
							User.getUser(sender.getName()).setGod(false);
							sender.sendMessage(msg.getMessage("offGod"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}

					} else if (args.length == 2) {
						if (psender.hasPermission("main.god.other")) {
							User user = new User(args[1]);
							if (user.isOnline()) {
								if (args[0].equalsIgnoreCase("enable")) {

									if (User.getUser(args[1]).isGod())
										return true;
									User.getUser(args[1]).setGod(true);
									user.sendMessage(msg.getMessage("onGod"));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else if (args[0].equalsIgnoreCase("disable")) {

									if (!User.getUser(args[1]).isGod())
										return true;
									User.getUser(args[1]).setGod(false);
									user.sendMessage(msg.getMessage("offGod"));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%",
											"/god enable/disable [player]"));
								}
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

			if (cmd.getName().equalsIgnoreCase("fly")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.fly")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;

						if (User.getUser(sender.getName()).hasFly()) {
							User.getUser(sender.getName()).setFly(false);
							User.getUser(sender.getName()).sendMessage(msg.getMessage("offFly"));
						} else {
							User.getUser(sender.getName()).setFly(true);
							User.getUser(sender.getName()).sendMessage(msg.getMessage("onFly"));
						}
						return true;

					} else if (args.length == 1) {

						User user = new User(args[0]);
						if (user.isOnline()) {
							if (psender.hasPermission("main.fly.other")) {
								if (User.getUser(args[0]).hasFly()) {
									User.getUser(args[0]).setFly(false);
									User.getUser(args[0]).sendMessage(msg.getMessage("offFly"));
								} else {
									User.getUser(args[0]).setFly(true);
									User.getUser(args[0]).sendMessage(msg.getMessage("onFly"));
								}
								sender.sendMessage(msg.getMessage("DONE"));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}

						} else if (args[0].equalsIgnoreCase("enable")) {

							if (User.getUser(sender.getName()).hasFly())
								return true;
							User.getUser(sender.getName()).setFly(true);
							User.getUser(sender.getName()).sendMessage(msg.getMessage("onFly"));
							return true;

						} else if (args[0].equalsIgnoreCase("disable")) {

							if (!User.getUser(sender.getName()).hasFly())
								return true;
							User.getUser(sender.getName()).setFly(false);
							User.getUser(sender.getName()).sendMessage(msg.getMessage("offFly"));
							return true;

						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}

					} else if (args.length == 2) {
						if (psender.hasPermission("main.fly.other")) {

							User user = new User(args[1]);
							if (user.isOnline()) {

								if (args[0].equalsIgnoreCase("enable")) {
									if (User.getUser(args[1]).hasFly())
										return true;
									User.getUser(args[1]).setFly(true);
									User.getUser(args[1]).sendMessage(msg.getMessage("onFly"));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else if (args[0].equalsIgnoreCase("disable")) {
									if (!User.getUser(args[1]).hasFly())
										return true;
									User.getUser(args[1]).setFly(false);
									User.getUser(args[1]).sendMessage(msg.getMessage("offFly"));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%",
											"/fly enable/disable [player]"));
								}

							} else {
								sender.sendMessage(msg.getMessage("NoPlayer"));
								return true;
							}

						} else {
							sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/fly [player]"));
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/fly [player]"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("repair")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.repair")) {
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("hand")) {
							if (sender.hasPermission("main.repair.hand")) {
								if (!(sender instanceof Player))
									return true;
								User user = new User(sender.getName());
								ItemPlugin it = new ItemPlugin(user.getItem());
								if (it.repair()) {
									user.sendMessage(msg.getMessage("Repair"));
								} else {
									user.sendMessage(msg.getMessage("NoItem"));
								}
								return true;
							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}
						} else if (args[0].equalsIgnoreCase("all")) {
							if (psender.hasPermission("main.repair.all")) {
								if (!(sender instanceof Player))
									return true;
								User user = new User(sender.getName());
								ItemPlugin.RepairAll(user.getPlayer());
								user.sendMessage(msg.getMessage("Repair"));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/repair hand/all"));
						}
					} else if (args.length == 2) {
						if (args[0].equalsIgnoreCase("hand")) {
							if (psender.hasPermission("main.repair.hand.other")) {
								User user = new User(args[1]);
								if (user.isOnline()) {
									ItemPlugin it = new ItemPlugin(user.getItem());
									if (it.repair()) {
										user.sendMessage(msg.getMessage("Repair"));
										sender.sendMessage(msg.getMessage("DONE"));
									} else {
										sender.sendMessage(msg.getMessage("NoItem"));
									}
									return true;
								} else {
									sender.sendMessage(msg.getMessage("NoPlayer"));
									return true;
								}

							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}
						} else if (args[0].equalsIgnoreCase("all")) {
							if (psender.hasPermission("main.repair.all.other")) {
								User user = new User(args[1]);
								if (user.isOnline()) {
									ItemPlugin.RepairAll(user.getPlayer());
									user.sendMessage(msg.getMessage("Repair"));
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
							sender.sendMessage(
									msg.getMessage("Usage").replaceAll("%command%", "/repair hand/all [player]"));
						}
					} else {
						sender.sendMessage(
								msg.getMessage("Usage").replaceAll("%command%", "/repair hand/all [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("workbench")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.workbench")) {
					if (!(sender instanceof Player))
						return true;
					User user = new User(sender.getName());
					user.openCraft();
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("suicide")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.suicide")) {
					if (!(sender instanceof Player))
						return true;
					User user = new User(sender.getName());
					user.setHealth(0);
					user.sendMessage(msg.getMessage("Suicide"));
					return true;
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("getpos")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.getpos")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						PluginLoc pl = new PluginLoc(((Player) sender).getLocation());
						sender.sendMessage(msg.getMessage("GetPos").replaceAll("%player%", sender.getName())
								.replaceAll("%location%", pl.toString() + " " + pl.getWorld()));
						return true;
					} else if (args.length == 1) {
						if (psender.hasPermission("main.getpos.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								PluginLoc pl = new PluginLoc(user.getPlayer().getLocation());
								sender.sendMessage(msg.getMessage("GetPos").replaceAll("%player%", user.getName())
										.replaceAll("%location%", pl.toString() + " " + pl.getWorld()));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/getpos [player]"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("clear")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.clear")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						user.clear();
						sender.sendMessage(msg.getMessage("Clear"));
						return true;
					} else if (args.length == 1) {
						if (psender.hasPermission("main.clear.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								user.clear();
								user.sendMessage(msg.getMessage("Clear"));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/clear [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("lastlocation")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.lastlocation")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						User user = User.getUser(sender.getName());
						if (user.getLastLocation() == null) {
							return true;
						}

						PluginLoc loc = new PluginLoc(user.getLastLocation());
						sender.sendMessage(msg.getMessage("Last").replaceAll("%location%", loc.toString())
								.replaceAll("%player%", sender.getName()));
						return true;
					} else if (args.length == 1) {
						if (sender.hasPermission("psender.lastlocation.other")) {
							User user = new User(args[0]);
							if (user.exists()) {
								if (user.isOnline()) {
									user = User.getUser(args[0]);
									if (user.getLastLocation() == null)
										return true;
									PluginLoc loc = new PluginLoc(user.getLastLocation());
									sender.sendMessage(
											msg.getMessage("Last").replaceAll("%location%", loc.toStringComplete())
													.replaceAll("%player%", user.getName()));
								} else {
									sender.sendMessage(
											msg.getMessage("Last").replaceAll("%location%", user.getQuitLocation())
													.replaceAll("%player%", user.getName()));
								}

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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/lastlocation [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("skull")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.skull")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						user.addItem(ItemPlugin.getSkullByUser(args[0]));
						return true;
					} else if (args.length == 2) {
						if (psender.hasPermission("main.skull.other")) {
							User user = new User(args[1]);
							if (user.isOnline()) {
								user.addItem(ItemPlugin.getSkullByUser(args[0]));
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
						sender.sendMessage(
								msg.getMessage("Usage").replaceAll("%command%", "/skull <playername> [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("burn")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.burn")) {
					if (args.length == 1) {
						User user = new User(args[0]);
						if (user.isOnline()) {
							user.getPlayer().setFireTicks(100);
							sender.sendMessage(msg.getMessage("burn").replaceAll("%player%", user.getName()));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/burn [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("ext")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.ext")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						user.getPlayer().setFireTicks(0);
						sender.sendMessage(msg.getMessage("unburn"));
						return true;
					} else if (args.length == 1) {
						if (psender.hasPermission("main.ext.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								user.getPlayer().setFireTicks(0);
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/ext [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("nightvision")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.nightvision")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						if (user.hasNightVision()) {
							user.setNightVision(false);
							sender.sendMessage(msg.getMessage("nvOff"));
						} else {
							user.setNightVision(true);
							sender.sendMessage(msg.getMessage("nvOn"));
						}
						return true;
					} else if (args.length == 1) {
						if (psender.hasPermission("main.nightvision.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								if (user.hasNightVision()) {
									user.setNightVision(false);
									user.sendMessage(msg.getMessage("nvOff"));
								} else {
									user.setNightVision(true);
									user.sendMessage(msg.getMessage("nvOn"));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/nightvision [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("compact")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.compact")) {
					if (!(sender instanceof Player)) {
						System.out.println("YOU MUST BE A PLAYER!");
					} else {
						ItemPlugin.compact((Player) sender);
						sender.sendMessage(msg.getMessage("DONE"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

		}
		return true;
	}

}
