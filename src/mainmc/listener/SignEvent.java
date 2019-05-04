package mainmc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import mainmc.nothing00.functions.PluginSign;
import mainmc.MainPermissions;
import mainmc.SignType;

public class SignEvent implements Listener {

	@EventHandler
	public void onChange(SignChangeEvent e) {

		PluginSign sign = new PluginSign(e.getLine(0), e.getLine(1), e.getLine(2), e.getLine(3));
		sign.colorizeSign(e.getPlayer());

		e.setLine(0, sign.getLine1());
		e.setLine(1, sign.getLine2());
		e.setLine(2, sign.getLine3());
		e.setLine(3, sign.getLine4());

		if (!sign.isSign())
			return;
		if (sign.getType() == null)
			return;
		MainPermissions p = new MainPermissions(e.getPlayer());

		if (p.hasPermission("main.sign.*")) {
			sign.setSign();
			sign.setEconomy();

		} else if (sign.getType().equals(SignType.BALANCE) && p.hasPermission("main.sign.balance.create")) {
			sign.setSign();

		} else if (sign.getType().equals(SignType.BUY) && p.hasPermission("main.sign.buy.create")) {
			sign.setSign();

		} else if (sign.getType().equals(SignType.SELL) && p.hasPermission("main.sign.sell.create")) {
			sign.setSign();

		} else if (sign.getType().equals(SignType.FREE) && p.hasPermission("main.sign.free.create")) {
			sign.setSign();

		} else if (sign.getType().equals(SignType.DISPOSAL) && p.hasPermission("main.sign.disposal.create")) {
			sign.setSign();

		} else if (sign.getType().equals(SignType.WARP) && p.hasPermission("main.sign.warp.create")) {
			sign.setSign();
			sign.setEconomy();

		} else if (sign.getType().equals(SignType.KIT) && p.hasPermission("main.sign.kit.create")) {
			sign.setSign();
			sign.setEconomy();

		} else if (sign.getType().equals(SignType.ENCHANT) && p.hasPermission("main.sign.enchant.create")) {
			sign.setSign();
			sign.setEconomy();

		} else if (sign.getType().equals(SignType.GAMEMODE) && p.hasPermission("main.sign.gamemode.create")) {
			sign.setSign();
			sign.setEconomy();

		} else if (sign.getType().equals(SignType.HEAL) && p.hasPermission("main.sign.heal.create")) {
			sign.setSign();
			sign.setEconomy();

		} else if (sign.getType().equals(SignType.REPAIR) && p.hasPermission("main.sign.repair.create")) {
			sign.setSign();
			sign.setEconomy();

		} else if (sign.getType().equals(SignType.TIME) && p.hasPermission("main.sign.time.create")) {
			sign.setSign();
			sign.setEconomy();

		} else if (sign.getType().equals(SignType.WEATHER) && p.hasPermission("main.sign.weather.create")) {
			sign.setSign();
			sign.setEconomy();

		}
		e.setLine(0, sign.getLine1());
		e.setLine(1, sign.getLine2());
		e.setLine(2, sign.getLine3());
		e.setLine(3, sign.getLine4());
	}

}
