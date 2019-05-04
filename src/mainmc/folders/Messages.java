package mainmc.folders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import mainmc.nothing00.MainPlugin;

public class Messages extends Config {

	public Messages() {
		super(new File(MainPlugin.getInstance().getDataFolder() + "/messages.yml"));
	}

	public String getMessage(String index) {
		return (super.getString("Messages.Prefix")+" "+super.getString("Messages."+index)).replaceAll("&", "§");
	}
	
	public String getLookupMessage() {
		
		String msg="";
		
		List<String> lookup = super.getStringList("Messages.Lookup");
		
		for(String s : lookup) {
			msg+=s+"\n";
		}
		
		return msg.replaceAll("&", "§");
	}
	
	public String getHistoryessage() {
		
		String msg="";
		
		List<String> lookup = super.getStringList("Messages.History");
		
		for(String s : lookup) {
			msg+=s+"\n";
		}
		
		return msg.replaceAll("&", "§");
	}
	
	public void onCreate() {		
		File file = new File(MainPlugin.getInstance().getDataFolder() + "/messages.yml");
		if(!file.exists()) {
			
			ArrayList<String> lookuplistmsg = new ArrayList<String>();
			lookuplistmsg.add("&6Lookup information about &c%player%");
			lookuplistmsg.add("&6Is muted: &c%ismuted%");
			lookuplistmsg.add("&6Mute Type: &c%mutetype%");
			lookuplistmsg.add("&6Mute motivation: &c%mutemotivation%");
			lookuplistmsg.add("&6Mute Author: &c%muteauthor%");
			lookuplistmsg.add("&6Is banned: &c%isbanned%");
			lookuplistmsg.add("&6Ban Type: &c%bantype%");
			lookuplistmsg.add("&6Ban motivation: &c%banmotivation%");
			lookuplistmsg.add("&6Ban Author: &c%banauthor%");
			lookuplistmsg.add("&6IP banned: &c%isIP%");
			lookuplistmsg.add("&6Ban-IP motivation: &c%banipmotivation%");
			
			ArrayList<String> history = new ArrayList<String>();
			history.add("&6History punishment of &c%player%&6:");
			history.add("&6Number of bans &c%ban%");
			history.add("&6Number of mutes &c%mute%");
			history.add("&6Number of kicks &c%kick%");
			history.add("&6Player is jailed: &c%jail%");
			history.add("&6Total punishment: &c%tot%");
			history.add("&6Check more information on:");
			history.add("&c%dir%");
			
			try {
				file.createNewFile();
				super.get().createSection("Messages");
				FileConfiguration msg = super.get();
				msg.set("Messages.Prefix", "&9Server>");
				msg.set("Messages.No-Perm", "&4You do not have permission!");
				msg.set("Messages.Reload", "&7Plugin reloaded!");
				msg.set("Messages.GameMode", "&7Your gamemode changed to &e%gm%");
				msg.set("Messages.DONE", "&7Succefully processed!");
				msg.set("Messages.NoPlayer", "&7Player not found!");
				msg.set("Messages.onVanish", "&7Vanish &aEnabled&7!");
				msg.set("Messages.offVanish", "&7Vanish &cDisabled&7!");
				msg.set("Messages.Give", "&7You received &e%n% &7of &e%item% &7from &e%player%");
				msg.set("Messages.NoID", "&7Unknow Block!");
				msg.set("Messages.Args", "&7Unknow args!");
			    msg.set("Messages.Get", "&7You got &e%n% &7of &e%item%");
			    msg.set("Messages.ItemHand", "&7You must be have an item in yout hand");
			    msg.set("Messages.NoEnchant", "&7Enchantment not applicable!");
			    msg.set("Messages.InvalidLevel", "&7Invalid level value!");				    
			    msg.set("Messages.Kick", "&7You have been kicked &e%player% &7!");	
			    msg.set("Messages.Kicked", "&cYou are kicked for");
			    msg.set("Messages.kicknotify", "&e%player% &7was kicked by &e%admin% &7for");	
			    msg.set("Messages.ShowXP", "&e%player% &7has &e%count% &7XP Levels!");	
			    msg.set("Messages.SetXP", "&7You have set &e%count% &7Xp level to &e%player%&7!");	
			    msg.set("Messages.GiveXP", "&7You received &e%count% &7Xp level from &e%player%&7!");	
			    msg.set("Messages.Feed", "&7you have been fed!");
			    msg.set("Messages.Heal", "&7you have been healed!");
			    msg.set("Messages.onGod", "&7GodMode &aEnabled&7!");
			    msg.set("Messages.offGod", "&7GodMode &cDisabled&7!");
			    msg.set("Messages.InvalidItem", "&7You must have a block in your hand!");
			    msg.set("Messages.Head", "&7Now You have an hat in your head!");
			    msg.set("Messages.RemoveHat", "&7Your hat has been removed!");
			    msg.set("Messages.onFly", "&7FlyMode &aEnabled&7!");
			    msg.set("Messages.offFly", "&7FlyMode &cDisabled&7!");
			    msg.set("Messages.Speed", "&7Your &e%type% &7Speed was set to &e%count%&7!");
			    msg.set("Messages.setTime", "&7You have set time to &e%time%&7!");
			    msg.set("Messages.setWeather", "&7You have set weather to &e%time%&7!");
			    msg.set("Messages.Repair", "&7All selected item has been repaired!");
			    msg.set("Messages.NoItem", "&7You must have an item in your hand!");
			    msg.set("Messages.Clear", "&7Your inventory now is void!");
			    msg.set("Messages.Teleport", "&7You was teleported to &e%player%&7!");
			    msg.set("Messages.InvSee", "&7You can not interact with this inventory!");
			    msg.set("Messages.EnderSee", "&7You can not interact with this EnderChest!");
			    msg.set("Messages.Me", "&e#%player% &7%message%");
			    msg.set("Messages.Broadcast", "&7%message%");
			    msg.set("Messages.Kill", "&7You have killed &e%player%!");
			    msg.set("Messages.NoPos", "&7Invalid location!");
			    msg.set("Messages.onTP", "&7Player-TP to you &aEnabled&7!");
			    msg.set("Messages.offTP", "&7Player-TP to you &cDisabled&7!");
			    msg.set("Messages.NoTP", "&7You can not teleport to this player!");
			    msg.set("Messages.Suicide", "&7You have been killed your self!");
			    msg.set("Messages.GetPos", "&7The Player &e%player% &7is at this coordinates: &e%location%&7!");
			    msg.set("Messages.NoSpawner", "&7You must have a spawner in hand!");
			    msg.set("Messages.InvalidMob", "&7Invalid mob type!");
			    msg.set("Messages.Spawner", "&7You have set spawner to &e%spawner%&7!");
			    msg.set("Messages.MobKill", "&7You have removed &e%count% &7Entitys of &e%type%&7!");
			    msg.set("Messages.Strike", "&7you have unleashed the wrath of Zeus!");
			    msg.set("Messages.Striked", "&7you were thunderstrucked!");
			    msg.set("Messages.List", "&7There are &e%count% &7Players online:");
			    msg.set("Messages.OpList", "&7There are &e%count% &7Players online (&c%vanishcount% &7in VanishMode):");
			    msg.set("Messages.onAFK", "&e%player% &7is now AFK");
			    msg.set("Messages.offAFK", "&e%player% &7is not now AFK");
			    msg.set("Messages.HelpStaff", "&b#HELP: &7(&e%player%&7) &b%message%");
			    msg.set("Messages.Received", "&7Your message should be received by &e%count% &7staff memebers!");
			    msg.set("Messages.NoStaff", "&7Sorry, but at the moment, there is not staff online!");
			    msg.set("Messages.Reply", "&7You can give any replys!");
			    msg.set("Messages.NoBack", "&7You do not have a back position!");
			    msg.set("Messages.Back", "&7Welcome back to your last position!");
			    msg.set("Messages.tpaRequest", "&7You have sent request to tpa to &e%player%&7!");
			    msg.set("Messages.tpaReceive", "&e%player% &7have asked to teleport to you! Use &e/tpaccept &7to accept o &e/tpdeny &7for deny!");
			    msg.set("Messages.tpaExpired", "&7Your teleport request was expired!");
			    msg.set("Messages.Notpa", "&7You aren't waiting any teleport request!");
			    msg.set("Messages.Reject", "&7Your request was rejected!");
			    msg.set("Messages.Alreadytpa", "&7You are already waiting to teleport!");
			    msg.set("Messages.hereRequest", "&7You have sent request to tpa &e%player% &7to you!");
			    msg.set("Messages.hereReceive", "&e%player% &7have asked to teleport to him! Use &e/tpaccept &7to accept o &e/tpdeny &7for deny!");
			    msg.set("Messages.SelfTeleport", "Really? do you want really teleport your self? It is impossible!");
			    msg.set("Messages.setSpawn", "&7You have set spawn location!");
			    msg.set("Messages.Spawn", "&7Welcome back to spawn!");
			    msg.set("Messages.NoSpawn", "&7There is not a spawn set!");
			    msg.set("Messages.InvalidSpawn", "&7Invalid spawn location!");
			    msg.set("Messages.WarpExist", "&7This warp already exist!");
			    msg.set("Messages.WarpCreated", "&7Warp &e%warp% &7was created!");
			    msg.set("Messages.WarpRemoved", "&7Warp &e%warp% &7was removed!");
			    msg.set("Messages.WarpNoExist", "&7This warp not exist!");
			    msg.set("Messages.InvalidWarp", "&7Invalid warp location!");
			    msg.set("Messages.Warp", "&7You was teleported to warp &e%warp%&7!");
			    msg.set("Messages.NoWarps", "&7Any warp was still created!");
			    msg.set("Messages.Warps", "&7List of all server warps:");
			    msg.set("Messages.Countdown", "&7You will teleport to &e%location% &7after &e%time%  &7seconds!");
			    msg.set("Messages.NoKits", "&7There are any kits...");
			    msg.set("Messages.KitExist", "&7This kit do not exist!");
			    msg.set("Messages.KitAlreadyExist", "&7This kit already exists!");
			    msg.set("Messages.Kit", "&7You got kit &e%kit%&7!");
			    msg.set("Messages.KitCountdown", "&7You must still wait &e%time%&7!");
			    msg.set("Messages.KitList", "&7List of all avaible kits for you:");
			    msg.set("Messages.NickLength", "&7you have exceeded the maximum length available!");
			    msg.set("Messages.Nick", "&7Your nickname was changed to &e%nick%&7!");
			    msg.set("Messages.NoNick", "&7You do not have a nickname!");
			    msg.set("Messages.RemoveNick", "&7Your nickname was removed!");
			    msg.set("Messages.NickUsed", "&7This nickname is already used!");
			    msg.set("Messages.Realname", "&7The real name of &e%nick% &7is &e%name%!");
			    msg.set("Messages.HealCountdown", "&7You will healed after &e%time%  &7seconds!");
			    msg.set("Messages.FeedCountdown", "&7You will fed after &e%time%  &7seconds!");
			    msg.set("Messages.Ignore", "&7You are ignoring &e%player%&7!");
			    msg.set("Messages.unIgnore", "&7Now you are not ignoring &e%player%&7!");
			    msg.set("Messages.IgnoreDenied", "&7You can not ignore this player!");
			    msg.set("Messages.NoIgnores", "&7This list is empty!");
			    msg.set("Messages.Ignores", "&7All player ignored by &e%player%&7:");
			    msg.set("Messages.IgnoreMSG", "&7This player is ignoring you!");
			    msg.set("Messages.onSpy", "&7ChatSpy &aEnabled&7!");
			    msg.set("Messages.offSpy", "&7ChatSpy &cDisabled&7!");
			    msg.set("Messages.NoMail", "&7The mailbox is empty!");
			    msg.set("Messages.MailRead", "&7This is the list of all mail to &e%player%&7:");
			    msg.set("Messages.MailClear", "&7Your mailbox was cleared!");
			    msg.set("Messages.MailFull", "&7Sorry, but this mailbox is full, your message will not send!");
			    msg.set("Messages.MailSent", "&7Mail succefully sent!");
			    msg.set("Messages.newMail", "&7You have &e%count% &7new mail in your mailbox! Use &e/mail read");
			    msg.set("Messages.MailtoClear", "&7Clear your mailbox with &e/mail clear&7!");
			    msg.set("Messages.NoIndex", "&7Invalid index!");
			    msg.set("Messages.RemovedIndex", "&7Succefully removed index &e%index%&7!");
			    msg.set("Messages.GodWorld", "&7GodMode disabled in this world!");
			    msg.set("Messages.RepairEnchanted", "&7Impossible to repair an enchanted items!");
			    msg.set("Messages.Untouchable", "&7You can give any sanction to this player!");
			    msg.set("Messages.MaxHome", "&7You have reached the maximum number of home!");
			    msg.set("Messages.setHome", "&7Home succefully set!");
			    msg.set("Messages.NoHome", "&7Any home set in list!");
			    msg.set("Messages.HomeList", "&7You have more than one home:");
			    msg.set("Messages.Home", "&7Welcome back to home!");
			    msg.set("Messages.InvalidHome", "&7This home not exist!");
			    msg.set("Messages.ExistingHome", "&7This home already exist!");
			    msg.set("Messages.delHome", "&7Home succefully removed!");
			    msg.set("Messages.onChat", "&7Sorry, but you are muted in this moment!");
			    msg.set("Messages.Muted", "&7You are permanently muted!");
			    msg.set("Messages.tempMuted", "&7You are temporarily muted for &e%time%&7!");
			    msg.set("Messages.Mute", "&7You have muted &e%player%");
			    msg.set("Messages.MuteNotify", "&e%player% &7was muted for &e%motivation% &7by &e%admin%");
			    msg.set("Messages.AlreadyMuted", "&7this player is already muted!");
			    msg.set("Messages.NoMute", "&7This player is not muted!");
			    msg.set("Messages.unMute", "&7You have been unmuted &e%player%&7!");
			    msg.set("Messages.unMuted", "&7Now you can talk in chat!");
			    msg.set("Messages.AlreadyBanned", "&7this player is already banned!");
			    msg.set("Messages.Banned", "&7You are permanently banned for &e%motivation%");
			    msg.set("Messages.Ban", "&7You have banned &e%player% &7!");
			    msg.set("Messages.BanNotify", "&e%player% &7was banned for &e%motivation% &7by &e%admin%");
			    msg.set("Messages.Unban", "&7You have unbanned &e%player% &7!");
			    msg.set("Messages.NoBanned", "&7This player is not banned!");
			    msg.set("Messages.tempBan", "&7You have banned &e%player% &7for &e%time%&7!");
			    msg.set("Messages.tempBanned", "&7You are temporarily banned for &e%motivation% &7until &e%time%");
			    msg.set("Messages.BuildInteract", "&7You can not interact with this block!");
			    msg.set("Messages.BuildBreak", "&7You can not break this block!");
			    msg.set("Messages.BuildPlace", "&7You can not place this block!");
			    msg.set("Messages.NoWorld", "&7Invalid World name!");
			    msg.set("Messages.Top", "&7You are been teleported to top location!");
			    msg.set("Messages.World", "&7You are been teleported to &e%world%&7!");
			    msg.set("Messages.JailExist", "&7This Jail already exist!");
			    msg.set("Messages.JailCreated", "&7You have been created jail &e%jail%&7!");
			    msg.set("Messages.NoJails", "&7There is any jail!");
			    msg.set("Messages.Jails", "&7List of all existing jails:");
			    msg.set("Messages.JailRemoved", "&7You have been removed jail &e%jail%&7!");
			    msg.set("Messages.JailNoExist", "&7This Jail not exist!");
			    msg.set("Messages.Jail", "&7You are been jailed from &e%admin%&7!");
			    msg.set("Messages.AlreadyJailed", "&7This player is already jailed!");
			    msg.set("Messages.inJail", "&7This player is in Jail!");
			    msg.set("Messages.jailed", "&7You are in jail!");
			    msg.set("Messages.NoJailed", "&7This player is not in jail!");
			    msg.set("Messages.unJail", "&7You are been unjailed from &e%admin%&7!");
			    msg.set("Messages.Lookup", lookuplistmsg);
			    msg.set("Messages.NoLookup", "&7This player is totally clean from punishments");
			    msg.set("Messages.BanIP", "&7You have banned &e%ip% Ip address!");
			    msg.set("Messages.BanIPUser", "&7So You have banned: &e%players%");
			    msg.set("Messages.BanIPNoUser", "&7But any registred player have this IP");
			    msg.set("Messages.NoIP", "&7Invalid IP address!");
			    msg.set("Messages.BAN-NOTE", "&cYour name is Blacklisted! Contact us to name@server.com for Support");
			    msg.set("Messages.UnBanIP", "&7You have unbanned %ip% Ip address!");
			    msg.set("Messages.UnBanIPUser", "&7So You have unbanned: &e%players%");
			    msg.set("Messages.UnBanIPNoUser", "&7But any registred player have this IP");
			    msg.set("Messages.NoBanIP", "&7This Player or IP is not banned!");
			    msg.set("Messages.AlreadyBannedIP", "&7This IP is already banned!");
			    msg.set("Messages.AnyIP", "&7Any registred player has this IP address!");
			    msg.set("Messages.SeenIP", "&7The user that have IP &e%ip% &7are:");
			    msg.set("Messages.SeenUser", "&7The user &e%player% &7have this address/es: &e%ip%");
			    msg.set("Messages.History", history);
			    msg.set("Messages.NoHistory", "&7For use this set &ecreate-file-history: true &7on &econfig.yml &7file!");
			    msg.set("Messages.NoEco", "&7Economy system is disabled!");
			    msg.set("Messages.Balance", "&7%player%: &e%balance%");
			    msg.set("Messages.Self", "&7Sorry but you can not give money to your self!");
			    msg.set("Messages.NoMoney", "&7Sorry but you do not have enough money!");
			    msg.set("Messages.givedMoney", "&e%player% &7gave you &e%balance%&7!");
			    msg.set("Messages.takedMoney", "&e%balance% &7were taken from &e%player%&7!");
			    msg.set("Messages.giveMoney", "&7You gave &e%balance% &7to &e%player%&7!");
			    msg.set("Messages.takeMoney", "&7You taked &e%balance% &7from &e%player%&7!");
			    msg.set("Messages.Sell", "&7You must have &e%count%x%item%&7!");
			    msg.set("Messages.NoPage", "&7Page not found!");
			    msg.set("Messages.setDelay", "&7Delay set for this kit!");
			    msg.set("Messages.setAlias", "&7Alias set in this kit!");
			    msg.set("Messages.setBook", "&7Book set in this kit!");
			    msg.set("Messages.setCmd", "&7Command set in this kit!");
			    msg.set("Messages.KitCreate", "&7kit &e%kit% &7created!");
			    msg.set("Messages.NoKitCreate", "&7Failed! This kit already exists or is empty!");
			    msg.set("Messages.Deleted", "&7Kit deleted!");
			    msg.set("Messages.Ping", "&f%player%: &a%ping%&fms");
			    msg.set("Messages.Last", "&e%player%:&7's Last Location: &e%location%");
			    msg.set("Messages.NoBook", "&7This book does not exist!");
			    msg.set("Messages.getBook", "&7You received a book!");
			    msg.set("Messages.Time", "&e%player%&7's online time is &e%time%");
			    msg.set("Messages.Buy", "&7You have bought &e%item%(x%amount%)");
			    msg.set("Messages.burn", "&7You have burned &e%player%");
			    msg.set("Messages.unburn", "&7You have been unburned");
			    msg.set("Messages.nvOff", "&7NightVision &cDisabled");
			    msg.set("Messages.nvOn", "&7NightVision &aEnabled");
			    msg.set("Messages.NoNear", "&7There is any player near location");
			    msg.set("Messages.Near", "&7All Player near actual location: &e%players%");
			    msg.set("Messages.withdraw", "&7You have been withdrawed &e%money%");
			    super.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
