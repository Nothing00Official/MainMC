package mainmc.commands.admin;

import java.util.HashSet;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.functions.ItemPlugin;
import mainmc.nothing00.functions.Mob;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.PluginLoc;

public class Utility implements CommandExecutor {

	public static String[] getCommands() {
		String[] array = { "vanish", "xp", "kill", "mobkill", "top", "firstlogin", "spawner", "thor", "near", "fireball"};
		return array;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("vanish")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.vanish")) {

					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;

						if (User.getUser(sender.getName()).hasVanish()) {
							User.getUser(sender.getName()).setVanish(false);
							User.getUser(sender.getName()).sendMessage(msg.getMessage("offVanish"));
						} else {
							User.getUser(sender.getName()).setVanish(true);
							User.getUser(sender.getName()).sendMessage(msg.getMessage("onVanish"));
						}
						return true;
					} else if (args.length == 1) {

						User user = new User(args[0]);
						if (user.isOnline()) {
							if (psender.hasPermission("main.vanish.other")) {
								if (User.getUser(args[0]).hasVanish()) {
									User.getUser(args[0]).setVanish(false);
									User.getUser(args[0]).sendMessage(msg.getMessage("offVanish"));
								} else {
									User.getUser(args[0]).setVanish(true);
									User.getUser(args[0]).sendMessage(msg.getMessage("onVanish"));
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
							if (User.getUser(sender.getName()).hasVanish())
								return true;
							User.getUser(sender.getName()).setVanish(true);
							sender.sendMessage(msg.getMessage("onVanish"));
							return true;
						} else if (args[0].equalsIgnoreCase("disable")) {
							if (!(sender instanceof Player))
								return true;
							if (!User.getUser(sender.getName()).hasVanish())
								return true;
							User.getUser(sender.getName()).setVanish(false);
							sender.sendMessage(msg.getMessage("offVanish"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}

					} else if (args.length == 2) {
						if (psender.hasPermission("main.vanish.other")) {

							User user = new User(args[1]);
							if (user.isOnline()) {

								if (args[0].equalsIgnoreCase("enable")) {
									if (User.getUser(args[1]).hasVanish())
										return true;
									User.getUser(args[1]).setVanish(true);
									user.sendMessage(msg.getMessage("onVanish"));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else if (args[0].equalsIgnoreCase("disable")) {
									if (!User.getUser(args[1]).hasVanish())
										return true;
									User.getUser(args[1]).setVanish(false);
									user.sendMessage(msg.getMessage("offVanish"));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/vanish enable/disable [player]"));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/vanish [player]"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("xp")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.xp")) {
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("show")) {
							if (psender.hasPermission("main.xp.show")) {
								User user = new User(sender.getName());
								sender.sendMessage(msg.getMessage("ShowXP").replaceAll("%player%", user.getName())
										.replaceAll("%count%", user.getExp() + ""));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("No-Perm"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/xp show [player]"));
						}
					} else if (args.length == 2) {
						if (args[0].equalsIgnoreCase("show")) {
							if (psender.hasPermission("main.xp.show.other")) {

								User user = new User(args[1]);
								if (user.isOnline()) {
									sender.sendMessage(msg.getMessage("ShowXP").replaceAll("%player%", user.getName())
											.replaceAll("%count%", "" + user.getExp()));
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
							sender.sendMessage(msg.getMessage("Args"));
							return true;
						}
					} else if (args.length == 3) {
						if (args[0].equalsIgnoreCase("give")) {
							if (psender.hasPermission("main.xp.give")) {

								User user = new User(args[1]);

								if (user.isOnline()) {
									if (!NumberUtils.isNumber(args[2])) {
										sender.sendMessage(msg.getMessage("Args"));
										return true;
									}

									user.setExp(Integer.parseInt(args[2]) + user.getExp());
									user.sendMessage(msg.getMessage("GiveXP").replaceAll("%player%", user.getName())
											.replaceAll("%count%", args[2]));
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
						} else if (args[0].equalsIgnoreCase("set")) {
							if (psender.hasPermission("main.xp.set")) {
								User user = new User(args[1]);

								if (user.isOnline()) {
									if (!NumberUtils.isNumber(args[2])) {
										sender.sendMessage(msg.getMessage("Args"));
										return true;
									}

									user.setExp(Integer.parseInt(args[2]));
									sender.sendMessage(msg.getMessage("SetXP").replaceAll("%player%", user.getName())
											.replaceAll("%count%", args[2]));
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
							sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/xp set/give <player> <amount>"));
						}

					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/xp set/give <player> <amount>"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("kill")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.kill")) {
					if (args.length == 1) {
						User user = new User(args[0]);
						if (user.isOnline()) {
							user.setHealth(0);
							sender.sendMessage(msg.getMessage("Kill").replaceAll("%player%", args[0]));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/kill <player>"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}

			}

			if (cmd.getName().equalsIgnoreCase("mobkill")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.mobkill")) {
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("all")) {
							sender.sendMessage(msg.getMessage("MobKill").replaceAll("%count%", "" + Mob.killAll())
									.replaceAll("%type%", "All"));
							return true;
						} else {
							Mob mob = new Mob(args[0]);
							if (mob.exists()) {
								sender.sendMessage(msg.getMessage("MobKill").replaceAll("%count%", "" + mob.kill())
										.replaceAll("%type%", mob.getType()));
								return true;
							} else {
								sender.sendMessage(msg.getMessage("InvalidMob"));
								return true;
							}

						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/mobkill <all/entity>"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("top")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.top")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						PluginLoc loc = new PluginLoc(((Player) sender).getLocation());
						User user = new User(sender.getName());
						user.teleported(loc.getTop());
						sender.sendMessage(msg.getMessage("Top"));
						return true;
					} else if (args.length == 1) {
						if (psender.hasPermission("main.top.other")) {
							User user = new User(args[0]);
							if (user.isOnline()) {
								PluginLoc loc = new PluginLoc(user.getPlayer().getLocation());
								user.teleported(loc.getTop());
								user.sendMessage(msg.getMessage("Top"));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/top [player]"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("firstlogin")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.firstlogin")) {
					if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						sender.sendMessage("§9" + sender.getName() + "> §e" + user.getFirstJoin());
						return true;
					} else if (args.length == 1) {
						if (psender.hasPermission("main.firstlogin.other")) {
							User user = new User(args[0]);
							if (user.exists()) {
								sender.sendMessage("§9" + sender.getName() + "> §e" + user.getFirstJoin());
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/firstjoin [player]"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("spawner")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.spawner")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						User user = new User(sender.getName());
						if (user.getItem() != null) {
							if (!user.getItem().getType().equals(Material.MOB_SPAWNER)) {
								sender.sendMessage(msg.getMessage("NoSpawner"));
								return true;
							}
							Mob mob = new Mob(args[0]);
							if (!mob.exists()) {
								sender.sendMessage(msg.getMessage("InvalidMob"));
								return true;
							}
							ItemPlugin item = new ItemPlugin(user.getItem());
							user.setItem(item.getSpawner(mob.getMob()));
							sender.sendMessage(msg.getMessage("Spawner").replaceAll("%spawner%", args[0]));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoItem"));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/spawner [mobType]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("thor")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.thor")) {
					if (args.length == 1) {
						User user = new User(args[0]);
						if (user.isOnline()) {
							PluginLoc loc = new PluginLoc(user.getPlayer().getLocation());
							loc.strike();
							sender.sendMessage(msg.getMessage("Strike"));
							user.sendMessage(msg.getMessage("Striked"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoPlayer"));
							return true;
						}
					} else if (args.length == 0) {
						if (!(sender instanceof Player))
							return true;
						PluginLoc loc;

						if (MainPlugin.getServerVersion().contains("1_11")
								|| MainPlugin.getServerVersion().contains("1_12") 
								|| MainPlugin.getServerVersion().contains("1_13")) {
							loc = new PluginLoc(((Player) sender).getEyeLocation());
						} else {
							loc = new PluginLoc(
									((Player) sender).getTargetBlock((HashSet<Byte>) null, 200).getLocation());
						}

						loc.strike();
						sender.sendMessage(msg.getMessage("Strike"));
						return true;
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/thor [player]"));
					}
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("near")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.near")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						if (!NumberUtils.isNumber(args[0])) {
							sender.sendMessage(msg.getMessage("Args"));
							return true;
						}
						User user = new User(sender.getName());
						if (user.getNearPlayers(Integer.parseInt(args[0])).isEmpty()) {
							sender.sendMessage(msg.getMessage("NoNear"));
							return true;
						}
						sender.sendMessage(msg.getMessage("Near").replaceAll("%players%", String.join("§7, §e",
								user.getNearPlayers(Integer.parseInt(args[0])).toArray(new String[0]))));
						return true;
					} else if (args.length == 2) {
						if (psender.hasPermission("main.near.other")) {

							User user = new User(args[1]);
							if (user.isOnline()) {
								if (!NumberUtils.isNumber(args[0])) {
									sender.sendMessage(msg.getMessage("Args"));
									return true;
								}
								if (user.getNearPlayers(Integer.parseInt(args[0])).isEmpty()) {
									sender.sendMessage(msg.getMessage("NoNear"));
									return true;
								}
								sender.sendMessage(msg.getMessage("Near").replaceAll("%players%", String.join("§7, §e",
										user.getNearPlayers(Integer.parseInt(args[0])).toArray(new String[0]))));
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
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/near <radius> [player]"));
					}

				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}
			
			if(cmd.getName().equalsIgnoreCase("fireball")) {
				Messages msg = new Messages();
				if(psender.hasPermission("main.fireball")) {
					if (!(sender instanceof Player))
						return true;
					User user = new User(sender.getName());
					user.launchFireBall();
					return true;					
				}else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}
		}
		return false;
	}

}
