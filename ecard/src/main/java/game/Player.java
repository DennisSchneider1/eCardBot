package game;

public class Player {
	
	private boolean isKingPlayer;
	private Card[] handOfPlayer;
	
	public Player() {
		isKingPlayer = false;
		Card emptyCard = new Card(0);
		handOfPlayer = new Card[]{emptyCard, emptyCard, emptyCard,
								  emptyCard, emptyCard};
	}
	
	public Player(boolean isKing, Card[] newHand) {
		isKingPlayer = isKing;
		handOfPlayer = newHand;
	}
	
	public boolean get_isKing() {
		return this.isKingPlayer;
	}
	
	public Card getCard(int position) {
		return handOfPlayer[position];
	}
	
	public Card[] getAllCards() {
		return handOfPlayer;
	}
	
	public int useCard(int position) {
		int typeOfUsed = handOfPlayer[position].getType();
		handOfPlayer[position].setType(0);
		
		compressHand();
		return typeOfUsed;
	}
	
	private void compressHand() {
		Card[] ret = new Card[handOfPlayer.length - 1];
		int i = 0;
		
		for(Card a:handOfPlayer){
			if(a.getType() != 0) {
				ret[i] = a;
				i++;
			}
		}
		handOfPlayer = ret;
	}
	
	public void drawCardSet(Card[] set) {
		handOfPlayer = set;
	}
}
