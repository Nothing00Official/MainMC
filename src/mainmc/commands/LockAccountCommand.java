package mainmc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;

public class LockAccountCommand implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"setlockaccount", "unlockaccount", "resetkey"};
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		if (sender instanceof Player) {

			if (cmd.getName().equalsIgnoreCase("setlockaccount")) {
				Messages msg = new Messages();
				Conf config = new Conf();
				if (config.lockAccountEnabled()) {
					if (args.length == 1) {
						User user = new User(sender.getName());
						if (user.hasKeyword()) {
							user.updateKeyword(args[0]);
							sender.sendMessage(config.getKeyChangeMessage().replace("{keyword}", "§m" + args[0]));
							return true;
						} else {
							user.setKeyword(args[0]);
							sender.sendMessage(config.getKeySetMessage().replace("{keyword}", "§m" + args[0]));
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/setlockaccount <keyword>"));
					}
				} else {
					sender.sendMessage(config.getUnLockDisableMessage());
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("unlockaccount")) {
				Conf config = new Conf();
				Messages msg = new Messages();
				if (config.lockAccountEnabled()) {
					if (args.length == 1) {
						User user = new User(sender.getName());
						if (user.hasKeyword()) {
							if (args[0].equals(user.getKeyword())) {

								if (user.isLocked()) {
									user.setLocked(false);
									sender.sendMessage(config.getUnLockedMessage());
									return true;
								} else {
									sender.sendMessage(config.getUnLockedMessage());
									return true;
								}

							} else {
								sender.sendMessage(config.getWrongKeyMessage());
								return true;
							}
						} else {
							sender.sendMessage(config.getSetLockMessage());
							return true;
						}
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/unlockaccount <keyword>"));
					}
				} else {
					sender.sendMessage(config.getUnLockDisableMessage());
					return true;
				}
			}

		} else if (sender instanceof ConsoleCommandSender) {
            if(cmd.getName().equalsIgnoreCase("resetkey")) {
             Messages msg = new Messages();
           	 if(args.length==1) {
           		 User user = new User(args[0]);
           		 if(user.isOnline()) {
           			 if(user.hasKeyword()) {
           				 user.deleteKeyword();
           				 System.out.println("Keyword reset for "+user.getName()+"!");
           			 }else {
           				 System.out.println("This Player has not a keyword!"); 
           			 }
           		 }else {
           			 System.out.println("Player not found!");
           		 }
           	 }else {
           		 System.out.println(msg.getMessage("Usage").replaceAll("%command%", " /resetket <player>"));
           	 }
            }
		}else {
			sender.sendMessage("§rRun this command by CONSOLE...");
		}
		
		return false;
	}

}
