package mainmc.nothing00.utils.punishments;

public enum MuteType implements Punish{
	
	PERM("NORMAL"), TEMP("TEMPORARILY");
	
	private String type;
	
	MuteType(String type){
		this.type=type;
	}
	
	public String toString() {
		return this.type;
	}

}
