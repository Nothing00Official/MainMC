package mainmc.nothing00;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import mainmc.SpigotUpdater;
import mainmc.commands.MainCommand;
import mainmc.folders.Conf;
import mainmc.folders.Config;
import mainmc.folders.DataBase;
import mainmc.folders.Help;
import mainmc.folders.JailData;
import mainmc.folders.KitData;
import mainmc.folders.Messages;
import mainmc.folders.SpawnData;
import mainmc.folders.Sql;
import mainmc.folders.WarpData;
import mainmc.listener.BanEvent;
import mainmc.listener.BlockBreak;
import mainmc.listener.BlockPlace;
import mainmc.listener.ChatEvent;
import mainmc.listener.CommandEvent;
import mainmc.listener.DamageEvent;
import mainmc.listener.DeathEvent;
import mainmc.listener.InteractEvent;
import mainmc.listener.InventoryInteractEvent;
import mainmc.listener.JoinEvent;
import mainmc.listener.MoveEvent;
import mainmc.listener.QuitEvent;
import mainmc.listener.SignEvent;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.ChatManager;
import mainmc.nothing00.utils.punishments.Punishment;

public class MainPlugin extends JavaPlugin {

	public static MainPlugin plugin;
	public static ExecutorSystem executor;
	public static DataBase sql;
	public static int version = getIntVersion()[0];
	public static int release = getIntVersion()[1];

	public void onEnable() {
		plugin = this;

		genereteFolders();

		loadEvents();

		getCommand("MainMC").setExecutor(new MainCommand());
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin.plugin, new Runnable() {
			@Override
			public void run() {
				ExecutorSystem cmdload = new ExecutorSystem();
				cmdload.loadCommands();
				executor = cmdload;
				SpigotUpdater updater = new SpigotUpdater(MainPlugin.plugin, 55455);
				 try {
			        if (updater.checkForUpdates()) {
			        	getLogger().info("=========================================================");  
			            getLogger().info("An update was found! New version: v" + updater.getLatestVersion());
			            getLogger().info("Download update: " + updater.getResourceURL());
			            getLogger().info("=========================================================");
			        }
			    } catch (Exception e) {
			        getLogger().warning("Could not check for updates! Caused:");
			        e.printStackTrace();
			    }
			}
		}, 20);

		sql = new DataBase(Sql.getDatabase().getAbsolutePath());
		sql.open();
		if (!sql.checkConnection()) {
			sql.closeConnection(sql.getConnection());
		} else {
			System.out.println("SQLite connection open!");
		}

		Conf config = new Conf();
		if (config.punishDivided()) {
			Punishment.exporting();
		} else {
			Punishment.createFile();
		}

		if (config.economyDisabled()) {
			if (getEconomyAddon() != null) {
				getEconomyAddon().getServer().getPluginManager().disablePlugin(getEconomyAddon());
			}
		}

		if (config.announceEnable()) {
			ChatManager.sendAnnounce();
		}
	
	}

	public void onDisable() {
		sql.closeConnection(sql.getConnection());
		System.out.print("MainMC disabled!");
		if (getEconomyAddon() != null) {
			getEconomyAddon().getServer().getPluginManager().disablePlugin(getEconomyAddon());
		}
	}

	public static MainPlugin getInstance() {
		return plugin;
	}

	public static ExecutorSystem getExecutor() {
		return executor;
	}

	public static DataBase getDataBase() {
		return sql;
	}

	public static String getServerVersion() {

		String pkg = Bukkit.getServer().getClass().getPackage().getName();
		String version = pkg.substring(pkg.lastIndexOf('.') + 1);

		return version;
	}

	public static int[] getIntVersion() {
		int version = Integer.valueOf(Bukkit.getServer().getClass().getName().split("\\.")[3].split("_")[1]);
		int release = Integer.valueOf(Bukkit.getServer().getClass().getName().split("\\.")[3].split("R")[1]);
		int[] v = { version, release };
		return v;
	}

	private void loadEvents() {
		Bukkit.getPluginManager().registerEvents(new InventoryInteractEvent(), this);
		Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
		Bukkit.getPluginManager().registerEvents(new QuitEvent(), this);
		Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
		Bukkit.getPluginManager().registerEvents(new BanEvent(), this);
		Bukkit.getPluginManager().registerEvents(new DamageEvent(), this);
		Bukkit.getPluginManager().registerEvents(new MoveEvent(), this);
		Bukkit.getPluginManager().registerEvents(new CommandEvent(), this);
		Bukkit.getPluginManager().registerEvents(new DeathEvent(), this);
		Bukkit.getPluginManager().registerEvents(new InteractEvent(), this);
		Bukkit.getPluginManager().registerEvents(new BlockPlace(), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);
		Bukkit.getPluginManager().registerEvents(new SignEvent(), this);
	}

	private void genereteFolders() {
		saveDefaultConfig();
		Messages msg = new Messages();
		SpawnData spawn = new SpawnData();
		WarpData warp = new WarpData();
		KitData kit = new KitData();
		JailData jail = new JailData();
		msg.onCreate();
		spawn.onCreate();
		warp.onCreate();
		kit.onCreate();
		Help.onCreate();
		jail.onCreate();
		Config.folders();
		Config.setDatabase();
	}

	public static void reloadPlugin() {
		plugin.reloadConfig();
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			User.userhash.get(p.getName().toLowerCase()).saveTime();
		}
		User.userhash.clear();
		plugin.getServer().getPluginManager().disablePlugin(plugin);
		plugin.getServer().getPluginManager().enablePlugin(plugin);

		for (Player p : plugin.getServer().getOnlinePlayers()) {
			User.userhash.put(p.getName().toLowerCase(), new User(p.getName()));
			User.userhash.get(p.getName().toLowerCase()).setJoinDate();
		}

		if (getEconomyAddon() != null) {
			getEconomyAddon().getServer().getPluginManager().disablePlugin(getEconomyAddon());
			getEconomyAddon().getServer().getPluginManager().enablePlugin(getEconomyAddon());
		}
	}

	public static Plugin getEconomyAddon() {
		return plugin.getServer().getPluginManager().getPlugin("MainMCEconomy");
	}

	public static String showMainMCInfo(String page) {
		switch (page) {
		case "1":
			return "§6/MainMC §rmain command\n" + "§6/MainMC reload §rreload plugin\n"
					+ "§6/MainMC help §rshow list of commands\n" + "§6/gmc §rset player gamemode to creative\n"
					+ "§6/gms §rset player gamemode to survival\n" + "§6/gma §rset player gamemode to adventure\n"
					+ "§6/vanish §rset player to invisible\n" + "§6/give §rgive items to a player\n"
					+ "§6Use §c/main help 2 §6to show the next page.";
		case "2":
			return "§6/item §rget items\n" + "§6/enchant §rrenchant item in your hand\n"
					+ "§6/kick §rkick a player form server\n" + "§6/kickall §rkick all players online\n"
					+ "§6/xp show §rshow player xp value\n" + "§6/xp give §rgive xp value to a player\n"
					+ "§6/xp set §rset new xp value to a player\n" + "§6/feed §rfeed hunger bar to a player\n"
					+ "§6Use §c/main help 3 §6to show the next page.";
		case "3":
			return "§6/heal §rset healthy to a player\n" + "§6/god §rset a player invincible\n"
					+ "§6/hat §rset a block as hat to a player\n" + "§6/fly §rset fly mod to a player\n"
					+ "§6/speed §rset speed fly or walk to a player\n" + "§6/walkspeed §rset walk speed to a player\n"
					+ "§6/flyspeed §rset fly speed to a player\n" + "§6/day §rset day in all worlds to a player\n"
					+ "§6Use §c/main help 4 §6to show the next page.";
		case "4":
			return "§6/night §rset night in all worlds\n" + "§6/sun §rset weather to sunny\n"
					+ "§6/storm §rset weather to stormy\n" + "§6/thunder §rset weather to thundering\n"
					+ "§6/repair hand §rreapir item in your hand\n"
					+ "§6/repair all  §rreapir all items in your inventory\n" + "§6/workbench §rOpen the workbench\n"
					+ "§6/me §rsend a messagga to all server\n" + "§6Use §c/main help 5 §6to show the next page.";
		case "5":
			return "§6/clear §rclar a player inventory\n" + "§6/tp §rteleport to a player\n"
					+ "§6/tphere §rteleport a player to you\n" + "§6/tpall §rteleport all players to player\n"
					+ "§6/tppos §rteleport a player to a specific location\n"
					+ "§6/tptoggle §renable/disable teleport request to a player\n"
					+ "§6/invsee §rsee a player inventory\n" + "§6/endersee §rsee a player enderchest\n"
					+ "§6Use §c/main help 6 §6to show the next page.";
		case "6":
			return "§6/broadcast §rsend a broadcast messagge\n" + "§6/kill §rkill a player\n"
					+ "§6/suicide §rkill your self\n" + "§6/getpos §rget a player location\n"
					+ "§6/spawner §rset mob spawner type\n" + "§6/mobkill §rkill mobs in the worlds\n"
					+ "§6/thor §rthunderstruck a player\n" + "§6/list §rsee list of all players online\n"
					+ "§6Use §c/main help 7 §6to show the next page.";
		case "7":
			return "§6/afk §rset a player afk\n" + "§6/helpstaff §rsend a message to staff\n"
					+ "§6/msg §rsend private message to a player\n" + "§6/reply §rreply to a private message\n"
					+ "§6/back §rgo to your last location\n" + "§6/tpa §rsend teleport request to a player\n"
					+ "§6/tpaccept §raccept a teleport request\n" + "§6/tpdeny §rreject a teleport request\n"
					+ "§6Use §c/main help 8 §6to show the next page.";
		case "8":
			return "§6/tpahere §rrequest to a player to teleport to you\n" + "§6/setspawn §rset spawn location\n"
					+ "§6/spawn §rgo to spawn location\n" + "§6/setwarp §rset a warp location\n"
					+ "§6/warp §rgo to a warp\n" + "§6/warps §rsee all avaible warps\n"
					+ "§6/delwarp §rdelete an existing warp\n" + "§6/kit §rgive a kit to a player\n"
					+ "§6Use §c/main help 9 §6to show the next page.";
		case "9":
			return "§6/kits §rsee all avaible kits\n" + "§6/nick §rset a custom display name in chat\n"
					+ "§6/realname §rsee real username form custom nickname\n"
					+ "§6/ignore §rignore a player when write in chat\n"
					+ "§6/ignores §rsee list of all players ignored\n" + "§6/socialspy §rsee all private message\n"
					+ "§6/mail send §rsend a mail to a player\n" + "§6/mail read §rread your mails\n"
					+ "§6Use §c/main help 10 §6to show the next page.";
		case "10":
			return "§6/mail clear §rdlete your mails\n" + "§6/spymail §rshow admin commands mail\n"
					+ "§6/motd §rsee main motd\n" + "§6/sethome §rset an home\n" + "§6/home §rgo to an home\n"
					+ "§6/delhome §rdelete an home\n" + "§6/adminhome §rshow admin commands home\n"
					+ "§6/mute §rmute a player\n" + "§6Use §c/main help 11 §6to show the next page.";
		case "11":
			return "§6/unmute §rallow to write in chat to a player\n"
					+ "§6/tempmute §rmute a player for a specific time in minutes or hours\n"
					+ "§6/ban §rban a player from server\n" + "§6/unban §runban a player from server\n"
					+ "§6/unbanall §runban all banned players\n"
					+ "§6/tempban §rban a player for a specific time in hours, deays or years\n"
					+ "§6/world §rteleport a player to a world\n" + "§6/top §rteleport a player to top block location\n"
					+ "§6Use §c/main help 12 §6to show the next page.";
		case "12":
			return "§6/createjail §rcreate a jail in your location\n" + "§6/jails §rsee all avaible jails\n"
					+ "§6/deljail §rdelete an existing jail\n" + "§6/jail §rset a player in a specific jail\n"
					+ "§6/unjail §rremove a player to jail\n" + "§6/lookup §rcheck if a player is banned or muted\n"
					+ "§6/banip §rban an IP or a player IP\n" + "§6/unbanip §runban an ip or a player IP\n"
					+ "§6Use §c/main help 13 §6to show the next page.";
		case "13":
			return "§6/seen §rsee all player IP or all users than have the same IP\n"
					+ "§6/setlockaccount §rset a keyword to unlock your account on IP change\n"
					+ "§6/unlockaccount §rif you change IP you can unlock your account\n"
					+ "§6/history §rsee al bans, kicks and mutes of a player\n" + "§6/balance §rsee money balance\n"
					+ "§6/pay §rpay money to a player\n" + "§6/eco give §rgive money to a player\n"
					+ "§6/eco take §rremove money to a player\n" + "§6/eco set §rset new money balance to a player\n"
					+ "§6Use §c/main help 14 §6to show the next page.";
		case "14":
			return "§6/firstlogin §rsee when player join at first time\n" + "§6/kitcreator §rcreate a kit\n"
					+ "§6/skull §rgive a player skull\n" + "§6/ping §rget player ping\n"
					+ "§6/book §rgive a custom book\n" + "§6/last §rget last player location\n"
					+ "§6/resethost §rcancel all player's ip data\n" + "§r/resetkeyword §rdelete player's keyword\n"
					+ "§6/icadd §rAdd command to item in your hand\n" + "§6/ptime §rGet total online player time";
		default:
			return "§6/MainMC §rmain command\n" + "§6/MainMC reload §rreload plugin\n"
					+ "§6/MainMC help §rshow list of commands\n" + "§6/gmc §rset player gamemode to creative\n"
					+ "§6/gms §rset player gamemode to survival\n" + "§6/gma §rset player gamemode to adventure\n"
					+ "§6/vanish §rset player to invisible\n" + "§6/give §rgive items to a player\n"
					+ "§6Use §c/main help 2 §6to show the next page.";
		}
	}
}
