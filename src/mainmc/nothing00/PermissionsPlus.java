package mainmc.nothing00;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import PermissionsPlus.manager.GroupPlus;
import PermissionsPlus.manager.UserPlus;
import mainmc.folders.Conf;

public class PermissionsPlus {

	private Player p;

	public PermissionsPlus(Player p) {
		this.p = p;
	}

	public boolean hasFormat() {
		if (!active())
			return false;
		boolean t = false;
		UserPlus user = new UserPlus(this.p.getName());
		Conf config = new Conf();

		if (user.getGroups().isEmpty())
			return config.getGroupsFormat().contains(GroupPlus.getDefault());
		for (String g : user.getGroups()) {
			if (config.getGroupsFormat().contains(g)) {
				
				t = true;
			}
		}
		return t;
	}

	public String getGroupWithFormat() {
		if (!active())
			return null;
		String t = null;
		UserPlus user = new UserPlus(this.p.getName());
		Conf config = new Conf();
		if (user.getGroups().isEmpty())
			return GroupPlus.getDefault();
		for (String g : user.getGroups()) {
			if (config.getGroupsFormat().contains(g)) {
				t = g;
			}
		}
		return t;
	}

	public String getFormat() {
		if (!active())
			return null;
		UserPlus user = new UserPlus(this.p.getName());
		Conf config = new Conf();
		if (user.getGroups().isEmpty())
			return config.getString("Groups." + GroupPlus.getDefault()).replaceAll("&", "§")
					.replace("{player}", this.p.getName()).replace("{displayname}", this.p.getDisplayName());
		return config.getString("Groups." + getGroupWithFormat()).replaceAll("&", "§")
				.replace("{player}", this.p.getName()).replace("{displayname}", this.p.getDisplayName());
	}

	public String getPrefix() {
		if (!active())
			return "";
		String prefix = "";
		UserPlus user = new UserPlus(this.p.getName());
		if (user.getGroups().isEmpty()) {
			GroupPlus group = new GroupPlus(GroupPlus.getDefault());
			if (group.getPrefix() != null)
				prefix = group.getPrefix();
			if (user.getPrefix() != null)
				prefix += user.getPrefix();
			return prefix.replaceAll("&", "§");
		}

		GroupPlus group = new GroupPlus(user.getGroups().get(0));
		if (group.getPrefix() != null)
			prefix = group.getPrefix();
		if (user.getPrefix() != null)
			prefix += user.getPrefix();
		return prefix.replaceAll("&", "§");

	}

	public String getSuffix() {
		if (!active())
			return "";
		String prefix = "";
		UserPlus user = new UserPlus(this.p.getName());
		if (user.getGroups().isEmpty()) {
			GroupPlus group = new GroupPlus(GroupPlus.getDefault());
			if (group.getSuffix() != null)
				prefix = group.getSuffix();
			if (user.getSuffix() != null)
				prefix += user.getSuffix();
			return prefix.replaceAll("&", "§");
		}

		GroupPlus group = new GroupPlus(user.getGroups().get(0));
		if (group.getSuffix() != null)
			prefix = group.getSuffix();
		if (user.getSuffix() != null)
			prefix += user.getSuffix();
		return prefix.replaceAll("&", "§");

	}

	public static boolean active() {
		return Bukkit.getPluginManager().getPlugin("PermissionsPlus") != null
				&& Bukkit.getPluginManager().getPlugin("PermissionsPlus").isEnabled();
	}

}
