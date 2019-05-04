package mainmc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import mainmc.MainPermissions;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.Book;
import mainmc.nothing00.functions.ItemPlugin;
import mainmc.nothing00.functions.User;

public class BookCommand implements CommandExecutor {
	
	public static String[] getCommands() {
		String[] array = {"book"};
		return array;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
			
			MainPermissions psender = new MainPermissions(sender);

			if (cmd.getName().equalsIgnoreCase("book")) {
				Messages msg = new Messages();
				if (psender.hasPermission("main.book")) {
					if (args.length == 1) {
						if (!(sender instanceof Player))
							return true;
						Book book = new Book(args[0]);
						if (book.exists()) {
							User user = new User(sender.getName());
							user.addItem(ItemPlugin.getBook(book.getTitle(), book.getAuthor(), book.getPages()));
							sender.sendMessage(msg.getMessage("getBook"));
							return true;
						} else {
							sender.sendMessage(msg.getMessage("NoBook"));
							return true;
						}
					} else if (args.length == 2) {
						if (psender.hasPermission("main.book.other")) {
							Book book = new Book(args[0]);
							if (book.exists()) {
								User user = new User(args[1]);
								if (user.isOnline()) {
									user.addItem(
											ItemPlugin.getBook(book.getTitle(), book.getAuthor(), book.getPages()));
									user.sendMessage(msg.getMessage("getBook"));
									sender.sendMessage(msg.getMessage("DONE"));
									return true;
								} else {
									sender.sendMessage(msg.getMessage("NoPlayer"));
									return true;
								}

							} else {
								sender.sendMessage(msg.getMessage("NoBook"));
								return true;
							}
						} else {
							sender.sendMessage(msg.getMessage("No-Perm"));
							return true;
						}
					} else {
						sender.sendMessage("§rUsage: /book <book> [player]");
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
