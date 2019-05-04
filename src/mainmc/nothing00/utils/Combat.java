package mainmc.nothing00.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import mainmc.folders.Conf;
import mainmc.nothing00.MainPlugin;
import mainmc.nothing00.functions.User;
public class Combat {

	private static HashMap<String, Combat> hashCombat = new HashMap<String, Combat>();

	public Player sender;
	public Player victim;

	public Combat(Player sender, Player victim) {
		this.sender = sender;
		this.victim = victim;
	}

	public void onCombat() {

		User senderu = new User(this.sender.getName());
		User victimu = new User(this.victim.getName());
		senderu.setFly(false);
		victimu.setFly(false);

		Conf config = new Conf();

		if (!hashCombat.containsKey(this.sender.getName())) {
			hashCombat.put(this.sender.getName(), this);
			this.sender.sendMessage(config.getEnterSenderMessage());
		}
		if (!hashCombat.containsKey(this.victim.getName())) {
			hashCombat.put(this.victim.getName(), this);
			this.victim.sendMessage(config.getEnterCombatMessage().replace("{player}", this.sender.getName()));
		}

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin.plugin, new Runnable() {
			@Override
			public void run() {
				if (sender != null && hashCombat.containsKey(sender.getName())) {
					sender.sendMessage(config.getExitCombatMessage());
					hashCombat.remove(sender.getName());

				}
				if (victim != null && hashCombat.containsKey(victim.getName())) {
					hashCombat.remove(victim.getName());
					victim.sendMessage(config.getExitCombatMessage());
				}
			}
		}, config.combatDelay() * 20);
	}

	public boolean isCombat() {
		return hashCombat.containsKey(this.sender.getName()) || hashCombat.containsKey(this.victim.getName());
	}

	public static Player onQuit(Player p) {
		if (hashCombat.containsKey(p.getName())) {
			p.setHealth(0);
			Combat combat = hashCombat.get(p.getName());
			hashCombat.remove(p.getName());
			if (p.equals(combat.sender))
				return combat.victim;
			if (p.equals(combat.victim))
				return combat.sender;
		}
		return null;
	}

}
