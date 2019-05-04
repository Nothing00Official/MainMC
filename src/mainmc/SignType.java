package mainmc;

public enum SignType {

	BALANCE("balance"), SELL("sell"), BUY("buy"), FREE("free"), DISPOSAL("disposal"), WARP("warp"), KIT("kit"), ENCHANT(
			"enchant"), GAMEMODE("gamemode"), HEAL("heal"), REPAIR("repair"), TIME("time"), WEATHER("weather");

	private String type;

	SignType(String type) {
		this.type = type;
	}
	
	public String toString() {
		return this.type;
	}

}
