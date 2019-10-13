package game;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Game {
	public static Control c = new Control();
	public static Round r;
	
	public static void printToDiscordServer(String text, MessageReceivedEvent event) {
		event.getChannel().sendMessage(text).queue();
	}
	
	public static void printToPlayer(boolean isNo1, String text, MessageReceivedEvent event) {
		User currentPlayer = isNo1?r.p1:r.p2;
		currentPlayer.openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(text).queue();
        });
	}
	
	public static String cardName(int cardNum) {
		String ret = "";
		switch(cardNum) {
			case 1:
				ret = "König";
				break;
			case 2:
				ret = "Sklave";
				break;
			case 3:
				ret = "Bauer";
				break;
		}
		return ret;
	}
	
	public static String numToSym(int Num) {
		String ret = "";
		switch(Num) {
			case 1:
				ret = " :one: ";
				break;
			case 2:
				ret = " :two: ";
				break;
			case 3:
				ret = " :three: ";
				break;
			case 4:
				ret = " :four: ";
				break;
			case 5:
				ret = " :five: ";
				break;
			case 6:
				ret = " :six: ";
				break;
			case 7:
				ret = " :seven: ";
				break;
			case 8:
				ret = " :eight: ";
				break;
			case 9:
				ret = " :nine: ";
				break;
			default:
				break;
		}
		return ret;
	}
	
	//p start
	public static void startMenu(MessageReceivedEvent event) {
		printToDiscordServer("Willkommen bei E-Card\n" +
				 "sie haben folgende Auswahl:\n\n" +

				 "**!e 1**		 Starten als Spieler 1 (König)\n" +
				 //"!e 3		Starten mit Spieler 2 als König\n" +
				 "**!e 2**		Anleitung ansehen\n", event);
	}
	
	public static boolean startGameMenu(int menuPoint, MessageReceivedEvent event) {
		boolean repeat = false;
		switch(menuPoint) {
			case 0:
					break;
				case 1:
					r = new Round(true);
					printToDiscordServer("**"+ event.getAuthor().getName() + "** ist nun Spieler 1\n" +
										 "Spieler 2 kann jetzt mit **!e** beitreten", event);
					r.p1 = event.getAuthor();
					break;
					
					/*
				 	case 3:
					r = new Round(false);
					printToDiscordServer("Die Runde beginnt\n", event);
					showNextPlayerCards(event);
					break;
					*/
					
				case 2:
					printToDiscordServer("TODO", event);
					repeat = true;
					break;
				
				default:
					printToDiscordServer("**Fehler.** Bitte eine Zahl zwischen 1-2 eingeben", event);
					repeat = true;
					break;
		}
		return repeat;
	}
	
	public static void player2acc(MessageReceivedEvent event) {
		if(!r.secPlayerSaved){
			r.p2 = event.getAuthor();
			printToDiscordServer("**"+ event.getAuthor().getName() + "** ist nun Spieler 2\n", event);
			printToDiscordServer("```Die Runde beginnt```\n", event);
			showNextPlayerCards(event);
			r.secPlayerSaved = true;
		}
	}
	
	public static void showNextPlayerCards(MessageReceivedEvent event) {
		String text = "";
		String ptext = "";
		int cPlay = r.nextMoveNo1 ? 1 : 2;
		String cPlayName = r.nextMoveNo1 ? r.p1.getName() : r.p2.getName();

		ptext += ("\n**" + cPlayName + "** (Spieler " + cPlay + ", " +
				 ((r.no1.get_isKing() == r.nextMoveNo1) ? "König" : "Sklave") + ")\n") +
				 "Du hast folgende Karten:\n\n";
		
		int cardNum = 1;
		for (Card card : r.nextMoveNo1 ? r.no1.getAllCards() : r.no2.getAllCards()) {
			if(cardNum != 1) {
				ptext += ", ";
			}
			ptext += numToSym(cardNum) + " = " + cardName(card.getType());
			cardNum++;
		}
		ptext += ("\n");
		printToPlayer(r.nextMoveNo1, ptext, event);

		int arrLength = r.nextMoveNo1 ? r.no1.getAllCards().length : r.no2.getAllCards().length;
		text += ("**" + cPlayName + "** (Spieler " + cPlay + ") wählt eine Karte mit:\n");
		text += "**!e (1-" + (arrLength) + ")**\n";
		printToDiscordServer(text, event);
		text = "";
	}
	
	public static boolean playerUsesCards(int cardNum, MessageReceivedEvent event) {
		String text = "";
		
		if(r.table == 0) {
			do {
				r.table = r.nextMoveNo1 ? r.no1.useCard(cardNum) : r.no2.useCard(cardNum);
			} while(r.table == 0);
			/*text = ("die Karte: " + r.table + " wurde gespielt\n");
			printToDiscordServer(text, event);
			text = "";*/
			
		} else {
			int secCard = 0;
			do {
				secCard =  r.nextMoveNo1 ? r.no1.useCard(cardNum) : r.no2.useCard(cardNum);
			} while(secCard == 0);
			//text += ("die Karte: " + secCard + " wurde gespielt\n");
			
			if((r.table == 1 && secCard == 3)||(r.table == 2 && secCard == 1)||(r.table == 3 && secCard == 2)) {
				//cPlay loses
				r.no1Win = !r.nextMoveNo1;
				r.roundEnd = true;
				printToDiscordServer(cardName(r.table) + " und " + cardName(secCard) + " wurden gespielt.\n", event);
			} else if((r.table == 3 && secCard == 1)||(r.table == 1 && secCard == 2)||(r.table == 2 && secCard == 3)) {
				//cPlay wins
				r.no1Win = r.nextMoveNo1;
				r.roundEnd = true;
				printToDiscordServer(cardName(r.table) + " und " + cardName(secCard) + " wurden gespielt.\n", event);
			} else {
				//draw
				printToDiscordServer("Beide Spieler haben Bauer gelegt. -> **Unentschieden**\n", event);
				printToDiscordServer("```Die nächste Runde beginnt```\n", event);
				r.roundEnd = false;
				r.nextMoveNo1 = !r.nextMoveNo1;
			}
			r.table = 0;
		}
		r.nextMoveNo1 = !r.nextMoveNo1;
		
		if(r.roundEnd) {
			text = "";
			String wPlayName = r.no1Win?r.p1.getName():r.p2.getName();
			if(r.p1.getName()!=r.p2.getName()) {
				if(r.no1Win) {
					core.Main.savedUsers.addUserPoints(core.Main.conv2UsrArrPos(r.p1.getIdLong()), 100);
				} else {
					core.Main.savedUsers.addUserPoints(core.Main.conv2UsrArrPos(r.p2.getIdLong()), 100);
				}
				text += "Der Gewinner hat einen Punkt bekommen.\n\n";
			} else {
				text += "Du hast keinen Punkt bekommen,\n" +
						"da du gegen dich selbst gespielt hast.\n\n";
			}
			text += ("**"+ wPlayName +"** (Spieler " + (r.no1Win?1:2) + ") hat gewonnen!!");
			printToDiscordServer(text, event);
			text = "";
		} else {
			showNextPlayerCards(event);
		}
		
		return r.roundEnd;
	}
	
	public static void main(String[] args) {
		//runECard();
	}

	
}
