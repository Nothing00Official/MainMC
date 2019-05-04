package mainmc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.MainPlugin;

public class MainCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		MainPermissions psender = new MainPermissions(sender);

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
			if (cmd.getName().equalsIgnoreCase("MainMC")) {
				Messages msg = new Messages();
				if (args.length == 0) {
					sender.sendMessage("§9MainMC> §ev"+MainPlugin.getInstance().getDescription().getVersion());
					sender.sendMessage("§9MainMC> §7Plugin made by Nothing00");
					sender.sendMessage("§9MainMC> §7https://www.youtube.com/c/Nothing00");
					sender.sendMessage("§9MainMC> §7try §e/main help");
					return true;
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reload")) {
						if (sender.hasPermission("main.reload")) {
							MainPlugin.reloadPlugin();
							sender.sendMessage(msg.getMessage("Reload"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else if (args[0].equalsIgnoreCase("help")) {
						if (psender.hasPermission("main.mainmc")) {
							sender.sendMessage(MainPlugin.showMainMCInfo("default"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /main help <page>");
						return true;
					}
				} else if (args.length == 2) {
					if (args[0].equalsIgnoreCase("help")) {
						if (psender.hasPermission("main.mainmc")) {
							sender.sendMessage(MainPlugin.showMainMCInfo(args[1]));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else if (args[0].equalsIgnoreCase("findpl")) {
						if (!(sender instanceof ConsoleCommandSender))
							return true;
						System.out.println("- " + String.join("\n- ",
								MainPlugin.getExecutor().getPluginsByCommand(args[1]).toArray(new String[0])));
						return true;
					} else {
						sender.sendMessage("§rUsage: /main help <page>");
						return true;
					}
				} else {
					sender.sendMessage(msg.getMessage("Args"));
					return true;
				}
			}
		}

		return false;
	}

}
