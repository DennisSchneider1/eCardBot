package game;

import java.util.Scanner;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Control {
	public static String s = "";
	static Scanner sc = new Scanner(s);
	
	public Control() {
	}
	
	public int getInt(MessageReceivedEvent event) {
		int ret = 0;
		
		while(!sc.hasNextInt()) {
			while(!event.getMessage().getContentRaw().contains("!e ")) {	
			}
			s = event.getMessage().getContentRaw().substring(3);
		}
		ret = sc.nextInt();
		
		return ret;
	}
	
	public int getCardNum(Round r, MessageReceivedEvent event){
		int givenNum = 0;
		int arrLength = r.nextMoveNo1 ? r.no1.getAllCards().length : r.no2.getAllCards().length;
		do {
			givenNum = getInt(event);
		} while((givenNum >= arrLength) || (givenNum < 0));
		return givenNum;
	}
}
