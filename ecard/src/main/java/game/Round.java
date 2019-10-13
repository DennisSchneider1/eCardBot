package game;

import net.dv8tion.jda.core.entities.User;

public class Round {

	Player no1;
	Player no2;
	public User p1;
	public User p2;
	boolean no1Win;
	boolean roundEnd;
	boolean nextMoveNo1;
	boolean secPlayerSaved = false;
	int table;
	
	Card[] kingSet = { new Card(1),
			 		   new Card(3),
			 		   new Card(3),
			 		   new Card(3),
			 		   new Card(3)};
			   
	Card[] slaveSet = { new Card(2),
	 		   			new Card(3),
	 		   			new Card(3),
	 		   			new Card(3),
	 		   			new Card(3)};
	
	private Card[] shuffleCards(Card[] cards) {
		double rnd = Math.random() * 100;
		int pos = (int) (rnd%5);
		Card firstC = new Card();
		firstC.setType(cards[0].getType());
		cards[0].setType(3);
		cards[pos].setType(firstC.getType());
		return cards;
	}
	
	public Round(boolean no1_isKing) {
		no1 = new Player(no1_isKing, no1_isKing ? shuffleCards(kingSet) : shuffleCards(slaveSet));
		no2 = new Player(!no1_isKing, !no1_isKing ? shuffleCards(kingSet) : shuffleCards(slaveSet));
		no1Win = false;
		roundEnd = false;
		nextMoveNo1 = no1_isKing;
		table = 0;
	}
}
