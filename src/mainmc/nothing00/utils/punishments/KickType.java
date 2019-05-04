package mainmc.nothing00.utils.punishments;

public enum KickType implements Punish{
	
	NORMAL("NORMAL"), ALL("ALL");
	
	private String type;
	
	KickType(String type){
		this.type=type;
	}
	
	public String toString() {
		return this.type;
	}

}
