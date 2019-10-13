package game;

public class Card {
	
	private int type;
	
	public Card() {
		type = 0;
	}
	
	public Card(int type) {
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type){
		this.type = type;
	}
}
