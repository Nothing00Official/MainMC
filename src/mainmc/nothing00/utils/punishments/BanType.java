package mainmc.nothing00.utils.punishments;

public enum BanType implements Punish{
	
	IP("IP"), PERM("NORMAL"), TEMP("TEMPORARILY");
	
	private String type;
	
	BanType(String type){
		this.type=type;
	}
	
	public String toString() {
		return this.type;
	}

}
