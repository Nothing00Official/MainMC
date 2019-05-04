package mainmc.nothing00.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import mainmc.MainPermissions;
import mainmc.folders.Conf;
import mainmc.folders.Messages;
import mainmc.folders.WarpData;
import mainmc.nothing00.MainPlugin;

public class Warp extends WarpData {

	private String warp;

	public Warp(String name) {
		this.warp = name.toLowerCase();
	}

	public String getName() {
		return this.warp;
	}

	public static List<String> getWarps() {
		List<String> warps = new ArrayList<String>();
		WarpData data = new WarpData();
		Set<String> warpdata = data.getConfiguration("Warps");
		warps.addAll(warpdata);
		return warps;
	}

	public void setWarp(Location loc) {
		super.setWarp(this.warp, loc);
	}

	public boolean isHidden() {
		return super.isHidden(this.warp);
	}

	public ItemStack getIcon() {
		return ItemPlugin.getItemString(super.getIcon(this.warp));
	}

	@SuppressWarnings("deprecation")
	public void setIcon(ItemStack item) {
		if (item.getData().getData() != 0)
			super.setIcon(this.warp, item.getType().toString() + ":" + item.getData().getData());
		else
			super.setIcon(this.warp, item.getType().toString() + "");
	}

	public void setHidden(boolean hidden) {
		super.setHidden(this.warp, hidden);
	}

	public Location getWarp() {
		return super.getWarp(this.warp);
	}

	public void delWarp() {
		super.get().set("Warps." + this.warp, null);
		super.save();
	}

	public boolean exists() {
		return getWarps().contains(this.warp);
	}

	public boolean hasWarpPermissions(Player p) {
		Conf config = new Conf();
		MainPermissions sender = new MainPermissions(p);
		if (config.perWarpPermission()) {
			return sender.hasPermission("main.warp." + this.warp);
		}
		return true;
	}

	public void onTeleport(Player p) {
		Messages msg = new Messages();
		Conf config = new Conf();
		User user = new User(p.getName());
		if (config.warpDelay() && !p.hasPermission("main.warps.bypassdelay")) {
			p.sendMessage(msg.getMessage("Countdown").replaceAll("%location%", this.warp).replaceAll("%time%",
					config.getWarpDelay() + ""));
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin.plugin, new Runnable() {
				@Override
				public void run() {
					user.teleported(getWarp());
					user.sendMessage(msg.getMessage("Warp").replaceAll("%warp%", warp));
				}
			}, config.getWarpDelay() * 20);
		} else {
 			user.teleported(getWarp());
			user.sendMessage(msg.getMessage("Warp").replaceAll("%warp%", this.warp));
		}
	}

}
