package mainmc.commands.admin;

import org.apache.commons.lang.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.utils.Time;

@SuppressWarnings("deprecation")
public class WeatherTimeCommand implements CommandExecutor {

	public static String[] getCommands() {
		String[] array = { "day", "night", "storm", "sun", "thunder" };
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			MainPermissions psender = new MainPermissions(sender);

			if (cmd.getName().equalsIgnoreCase("day")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.day")) {
					Time.setDay();
					sender.sendMessage(msg.getMessage("setTime").replaceAll("%time%", "Day"));
					return true;
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("night")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.night")) {
					Time.setNight();
					sender.sendMessage(msg.getMessage("setTime").replaceAll("%time%", "Night"));
					return true;
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("storm")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.weather")) {
					Time.setStorming();
					sender.sendMessage(msg.getMessage("setWeather").replaceAll("%time%", "Stormy"));
					return true;
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("sun")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.weather")) {
					Time.clearWeather();
					sender.sendMessage(msg.getMessage("setWeather").replaceAll("%time%", "Sunny"));
					return true;
				} else {
					sender.sendMessage(msg.getMessage("No-Perm"));
					return true;
				}
			}

			if (cmd.getName().equalsIgnoreCase("thunder")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.weather")) {
					if (args.length == 0) {
						Time.thunder();
						sender.sendMessage(msg.getMessage("setWeather").replaceAll("%time%", "Thundering"));
						return true;
					} else if (args.length == 1) {

						if (!NumberUtils.isNumber(args[0])) {
							sender.sendMessage(msg.getMessage("Args"));
							return true;
						}

						Time.thunder();
						Time.thunder(Integer.parseInt(args[0]));
						sender.sendMessage(msg.getMessage("setWeather").replaceAll("%time%", "Thundering"));
						return true;
					} else {
						sender.sendMessage(msg.getMessage("Usage").replaceAll("%command%", "/thunder [time]"));
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
