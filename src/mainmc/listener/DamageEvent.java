package mainmc.listener;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.nothing00.functions.User;
import mainmc.nothing00.utils.Combat;

public class DamageEvent implements Listener {

	public static HashSet<String> safe = new HashSet<String>();

	@EventHandler
	public void onDamage(EntityDamageEvent e) {

		Messages msg = new Messages();
		Conf config = new Conf();

		if (e.getEntity() instanceof Player && e.getEntity() instanceof LivingEntity) {
			Player p = (Player) e.getEntity();
			User user = new User(p.getName());

			// LOCKED
			if (user.isLocked()) {
				e.setCancelled(true);
				p.sendMessage(config.getUnLockMessage());
				return;
			}

			// IN JAIL
			if (user.isJailed()) {
				e.setCancelled(true);
				p.sendMessage(msg.getMessage("jailed"));
				return;
			}

			if (safe.contains(p.getName())) {
				e.setCancelled(true);
			}

			if (User.getUser(p.getName()).isGod()
					&& !config.getBlackListWorldsGod().contains(p.getLocation().getWorld().getName())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {

		Conf config = new Conf();
		Messages msg = new Messages();

		if (config.useCombat() && e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();

			if (!p.hasPermission("main.bypasscombat") && !d.hasPermission("main.bypasscombat")) {
				Combat combat = new Combat(d, p);
				combat.onCombat();
			}
		}

		if (e.getEntity() instanceof Player) {

			Player p = (Player) e.getEntity();
			
			if(!Bukkit.getServer().getOnlinePlayers().contains(p) || p.getUniqueId()==null)
				return;
			
			User user = new User(p.getName());

			// LOCKED
			if (user.isLocked()) {
				e.setCancelled(true);
				p.sendMessage(config.getUnLockMessage());
				return;
			}

			// IN JAIL
			if (user.isJailed()) {
				e.setCancelled(true);
				p.sendMessage(msg.getMessage("jailed"));
				return;
			}

			if (safe.contains(p.getName())) {
				e.setCancelled(true);
			}

			if (User.getUser(p.getName()).isGod()
					&& !config.getBlackListWorldsGod().contains(p.getLocation().getWorld().getName())) {
				e.setCancelled(true);
			}
		}

		if (e.getDamager() instanceof Player) {
			Player damager = (Player) e.getDamager();

			User user = new User(damager.getName());
			MainPermissions s = new MainPermissions(damager);

			// LOCKED
			if (user.isLocked()) {
				e.setCancelled(true);
				damager.sendMessage(config.getUnLockMessage());
				return;
			}

			// IN JAIL
			if (user.isJailed()) {
				e.setCancelled(true);
				damager.sendMessage(msg.getMessage("jailed"));
				return;
			}

			if (User.getUser(damager.getName()).isGod() && !s.hasPermission("main.god.pvp")) {
				User.getUser(damager.getName()).setGod(false);
			}

			if (User.getUser(damager.getName()).hasVanish() && !s.hasPermission("main.vanish.pvp")) {
				User.getUser(damager.getName()).setVanish(false);
			}
		}

	}

}
