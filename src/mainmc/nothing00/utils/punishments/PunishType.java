package mainmc.nothing00.utils.punishments;

public enum PunishType {
	MUTE("MUTE"), KICK("KICK"), BAN("BAN");

	private String type;

	PunishType(String type) {
		this.type = type;
	}

	public String toString() {
		return this.type;
	}
}
