package mainmc.folders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mainmc.nothing00.MainPlugin;

public class Conf extends Config {

	public Conf() {
		super(MainPlugin.getInstance().getConfig(), new File(MainPlugin.getInstance().getDataFolder() + "/config.yml"));
	}

	public boolean getBoolean(String index) {
		return Boolean.parseBoolean(super.getString(index));
	}

	public int getInt(String index) {
		return Integer.parseInt(super.getString(index));
	}

	public double getDouble(String index) {
		return Double.parseDouble(super.getString(index));
	}

	public String getDBName() {
		return super.getString("database-name") + ".db";
	}

	public boolean bypassEnchant() {
		return Boolean.parseBoolean(super.getString("enchantments.bypassmaxlevel"));
	}

	public boolean thereIsHistory() {
		return Boolean.parseBoolean(super.getString("create-file-history"));
	}

	public boolean punishDivided() {
		return Boolean.parseBoolean(super.getString("divide-per-file"));
	}

	public String getKickDefaultMotiv() {
		return super.getString("Kick.Motivation").replaceAll("&", "§");
	}

	public String getMuteDefaultMotiv() {
		return super.getString("Mute.Motivation").replaceAll("&", "§");
	}

	public String getBanDefaultMotiv() {
		return super.getString("Ban.Motivation").replaceAll("&", "§");
	}

	public boolean feedDelay() {
		return Boolean.parseBoolean(super.getString("Heal.enabled-delay"));
	}

	public int getFeedDelay() {
		return Integer.parseInt(super.getString("Heal.feed-delay"));
	}

	public int getHealDelay() {
		return Integer.parseInt(super.getString("Heal.heal-delay"));
	}

	public boolean repairEnchanted() {
		return Boolean.parseBoolean(super.getString("enchantments.repair-enchanted"));
	}

	public String getSendMessage() {
		return super.getString("Message.sent").replaceAll("&", "§");
	}

	public String getReceivedMessage() {
		return super.getString("Message.received").replaceAll("&", "§");
	}

	public String getSpyMessage() {
		return super.getString("Message.spy").replaceAll("&", "§");
	}

	public int getTpaExipre() {
		return Integer.parseInt(super.getString("Teleport.tpa-expire-time"));
	}

	public boolean perWarpPermission() {
		return Boolean.parseBoolean(super.getString("per-warp-permission"));
	}

	public boolean warpDelay() {
		return Boolean.parseBoolean(super.getString("Teleport.warp-delay"));
	}

	public boolean spawnDelay() {
		return Boolean.parseBoolean(super.getString("Teleport.spawn-delay"));
	}

	public boolean backDelay() {
		return Boolean.parseBoolean(super.getString("Teleport.back-delay"));
	}

	public int getWarpDelay() {
		return Integer.parseInt(super.getString("Teleport.warp-delay-time"));
	}

	public int getSpawnDelay() {
		return Integer.parseInt(super.getString("Teleport.spawn-delay-time"));
	}

	public int getBackDelay() {
		return Integer.parseInt(super.getString("Teleport.back-delay-time"));
	}

	public int getNickLength() {
		return Integer.parseInt(super.getString("nickname-length"));
	}

	public boolean sendFullMail() {
		return Boolean.parseBoolean(super.getString("not-send-full-mailbox"));
	}

	public int getMaxBox() {
		return Integer.parseInt(super.getString("mailboxlimit"));
	}

	public int getVipHomeLimit() {
		return Integer.parseInt(super.getString("sethome.vip"));
	}

	public int getStaffHomeLimit() {
		return Integer.parseInt(super.getString("sethome.staff"));
	}

	public int getHomeLimit() {
		return Integer.parseInt(super.getString("sethome.default"));
	}

	public String getOpChar() {
		return super.getString("ops-color-name");
	}

	public String getNickCharPrefix() {
		return super.getString("nickname-prefix");
	}

	public boolean teleportSafe() {
		return Boolean.parseBoolean(super.getString("teleport-safety.enabled"));
	}

	public int safeTime() {
		return Integer.parseInt(super.getString("teleport-safety.duration"));
	}

	public int loginAttackDelay() {
		return Integer.parseInt(super.getString("login-attack-delay"));
	}

	public List<String> getBlackListWorldsGod() {
		return super.getStringList("no-god-in-worlds");
	}

	public boolean disbleGodOnQuit() {
		return Boolean.parseBoolean(super.getString("god-disable-quit"));
	}

	public boolean autoKick() {
		return Boolean.parseBoolean(super.getString("AFK.AutoKick"));
	}

	public boolean autoAfk() {
		return Boolean.parseBoolean(super.getString("AFK.AutoAFK"));
	}

	public int autoAfkTime() {
		return Integer.parseInt(super.getString("AFK.time"));
	}

	public int autoKickTime() {
		return Integer.parseInt(super.getString("AFK.Kick-time"));
	}

	public String getAutoKickMessage() {
		return super.getString("AFK.Kick-Message").replaceAll("&", "§");
	}

	public boolean cancelAfkonMove() {
		return Boolean.parseBoolean(super.getString("cancel-afk-on-move"));
	}

	public boolean freezeAfk() {
		return Boolean.parseBoolean(super.getString("freeze-afk-players"));
	}

	public boolean deathMessage() {
		return Boolean.parseBoolean(super.getString("death-messages"));
	}

	public boolean homeRespawn() {
		return Boolean.parseBoolean(super.getString("spawn-at-home-on-death"));
	}

	public boolean silentQuitJoin() {
		return Boolean.parseBoolean(super.getString("allow-silent-join-quit"));
	}

	public String getJoinMessage() {
		return super.getString("custom-join-message").replaceAll("&", "§");
	}

	public String getQuitMessage() {
		return super.getString("custom-quit-message").replaceAll("&", "§");
	}

	public boolean spawnTp() {
		return Boolean.parseBoolean(super.getString("spawn-TP"));
	}

	public List<String> getJailWhitelist() {
		return super.getStringList("jail-whitelist-commands");
	}

	public boolean lockAccountEnabled() {
		return Boolean.parseBoolean(super.getString("lockaccount-protection"));
	}

	public String getSetLockMessage() {
		return super.getString("setlockaccount-message").replaceAll("&", "§");
	}

	public String getUnLockMessage() {
		return super.getString("unlockaccount-message").replaceAll("&", "§");
	}

	public String getKeySetMessage() {
		return super.getString("set-keyword-message").replaceAll("&", "§");
	}

	public String getKeyChangeMessage() {
		return super.getString("change-keyword-message").replaceAll("&", "§");
	}

	public String onUnLockMessage() {
		return super.getString("unlocked-message").replaceAll("&", "§");
	}

	public String getWrongKeyMessage() {
		return super.getString("unlock-wrong-message").replaceAll("&", "§");
	}

	public String getUnLockDisableMessage() {
		return super.getString("no-active-protect-message").replaceAll("&", "§");
	}

	public String getUnLockedMessage() {
		return super.getString("unlocked-message").replaceAll("&", "§");
	}

	public List<String> getUnLockWhitelist() {
		List<String> white = super.getStringList("whitelist-locked-cmds");
		white.add("unlockaccount");
		white.add("keyword");
		return white;
	}

	public boolean kitGui() {
		return Boolean.parseBoolean(super.getString("kit-Gui.enabled"));
	}
	
	public boolean warpGui() {
		return Boolean.parseBoolean(super.getString("warp-Gui.enabled"));
	}

	public String getPreviewLabel() {
		return super.getString("kit-Gui.preview-label").replaceAll("&", "§");
	}

	public String getLockedPreviewLabel() {
		return super.getString("kit-Gui.locked-preview-label").replaceAll("&", "§");
	}

	public String getGiveLabel() {
		return super.getString("kit-Gui.get-lable").replaceAll("&", "§");
	}

	public String getDeniedLabel() {
		return super.getString("kit-Gui.locked-label").replaceAll("&", "§");
	}
	
	public String getDenyWarpLabel() {
		return super.getString("warp-Gui.locked-label").replaceAll("&", "§");
	}
	
	public String getWarpTpLabel() {
		return super.getString("warp-Gui.admin-teleport").replaceAll("&", "§");
	}
	
	public String getWarpIconLabel() {
		return super.getString("warp-Gui.change-icon").replaceAll("&", "§");
	}
	
	public String getShowWarpLabel() {
		return super.getString("warp-Gui.show-warp").replaceAll("&", "§");
	}
	
	public String getHideWarpLabel() {
		return super.getString("warp-Gui.hide-warp").replaceAll("&", "§");
	}

	public boolean announceEnable() {
		return Boolean.parseBoolean(super.getString("Announcement.enable"));
	}

	public int getAnnounceDelay() {
		return Integer.parseInt(super.getString("Announcement.delay"));
	}

	public List<String> getAnnounces() {
		List<String> ann = new ArrayList<String>();
		ann.addAll(super.getConfiguration("Announcement.messages"));
		return ann;
	}

	public List<String> getGroupsFormat() {
		List<String> ann = new ArrayList<String>();
		ann.addAll(super.getConfiguration("Groups"));
		return ann;
	}

	public String getFormat() {
		return super.getString("format").replaceAll("&", "§");
	}

	public List<String> getBreakBlackList() {
		return super.getStringList("non-breakable-blocks");
	}

	public List<String> getPlaceBlackList() {
		return super.getStringList("non-placeable-blocks");
	}

	public List<String> getInteractBlackList() {
		return super.getStringList("non-usable-blocks");
	}

	public String getCaseMessage() {
		return super.getString("case-sensitive-deny-message").replaceAll("&", "§");
	}

	public boolean caseSensitive() {
		return Boolean.parseBoolean(super.getString("case-sensitive-protection"));
	}

	public boolean sameIp() {
		return Boolean.parseBoolean(super.getString("server-Player-IP-protection"));
	}

	public boolean uuid() {
		return Boolean.parseBoolean(super.getString("UUID-protection"));
	}

	public String getUuidMessage() {
		return super.getString("UUID-deny-message").replaceAll("&", "§");
	}

	public String getSameIPMessage() {
		return super.getString("server-Player-IP-deny-message").replaceAll("&", "§");
	}

	public String getBlackListCommandMessage() {
		return super.getString("blacklist-commands-message").replaceAll("&", "§");
	}

	public List<String> getCommandBlackList() {
		return super.getStringList("blacklist-commands");
	}

	public List<String> getUserBlackList() {
		return super.getStringList("blacklist-usernames");
	}

	public String getUserBlacklistMessage() {
		return super.getString("blacklist-usernames-message").replaceAll("&", "§");
	}

	public String getNewPlayerMessage() {
		return super.getString("new-player-join-message").replaceAll("&", "§");
	}

	public String getFirstKit() {
		return super.getString("first-kit").replaceAll("&", "§");
	}

	public List<String> getSigns() {
		return super.getStringList("enabledSigns");
	}

	public boolean usePermissionsPlugin() {
		return Boolean.parseBoolean(super.getString("use-permissions"));
	}

	public List<String> getPermissions() {
		return super.getStringList("user-permissions");
	}

	public boolean economyAddon() {
		return Boolean.parseBoolean(super.getString("addon.enabled"));
	}

	public double startBalance() {
		return Double.parseDouble(super.getString("balance"));
	}

	public String getValuta() {
		return super.getString("valuta-format");
	}

	public boolean economyDisabled() {
		return Boolean.parseBoolean(super.getString("disable-economy"));
	}

	public boolean useCombat() {
		return Boolean.parseBoolean(super.getString("CombatMode.enable"));
	}
	
	public boolean useChat() {
		return Boolean.parseBoolean(super.getString("use-chat-format"));
	}

	public int combatDelay() {
		return Integer.parseInt(super.getString("CombatMode.exit-delay"));
	}

	public String getEnterCombatMessage() {
		return super.getString("CombatMode.enter-message").replaceAll("&", "§");
	}

	public String getEnterSenderMessage() {
		return super.getString("CombatMode.sender-message").replaceAll("&", "§");
	}

	public String getExitCombatMessage() {
		return super.getString("CombatMode.exit-message").replaceAll("&", "§");
	}

	// ECONOMY ADDON

	public boolean useEcoGui() {
		return Boolean.parseBoolean(super.getString("addon.use-gui"));
	}

	public String economyGiveLabel() {
		return super.getString("addon.give").replaceAll("&", "§");
	}

	public String economyTakeLabel() {
		return super.getString("addon.take").replaceAll("&", "§");
	}

	public String economySetLabel() {
		return super.getString("addon.set").replaceAll("&", "§");
	}

	public String economyAddLabel() {
		return super.getString("addon.add").replaceAll("&", "§");
	}

	public String economyRemoveLabel() {
		return super.getString("addon.remove").replaceAll("&", "§");
	}

	public String economyCloseLabel() {
		return super.getString("addon.close").replaceAll("&", "§");
	}

	public String economySaveLabel() {
		return super.getString("addon.save").replaceAll("&", "§");
	}

	public String economyTop1Label() {
		return super.getString("addon.top1").replaceAll("&", "§");
	}

	public String economyTop2Label() {
		return super.getString("addon.top2").replaceAll("&", "§");
	}

	public String economyTop3Label() {
		return super.getString("addon.top3").replaceAll("&", "§");
	}

	public String economyTop10Label() {
		return super.getString("addon.top10").replaceAll("&", "§");
	}

	public String economyTop5Label() {
		return super.getString("addon.top5").replaceAll("&", "§");
	}

	public String economyTop20Label() {
		return super.getString("addon.top20").replaceAll("&", "§");
	}

}
