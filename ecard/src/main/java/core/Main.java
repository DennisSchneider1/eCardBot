package core;

import javax.security.auth.login.LoginException;


import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

//load useres if data exists and alluseres empty
//reaction to msg event 
public class Main extends ListenerAdapter{
	public boolean gameStarted = false;
	public boolean repeat = true;
	public boolean p2acc = false;
	public MessageChannel playingChannel;
	public static boolean moreThanOneUsr = false; //set to true if reloading a userlist file is wanted
	
	public boolean nextTextP = false;
	public int payPointsNum = 0;
	
	public static BotUsrList savedUsers = new BotUsrList(moreThanOneUsr);

	public static String TOKEN = ""; //put the bot token here
	
	public static void main(String[] args) throws LoginException {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		String token = "";//
		builder.setGame(Game.listening("!game !eHelp"));
		builder.setToken(token);
		builder.addEventListener(new Main());
		builder.buildAsync();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		System.out.println("Received from " +
						   event.getAuthor().getName() + ": " +
						   event.getMessage().getContentDisplay());
		
		userListCommands(event);
		
		if (event.getMessage().getContentRaw().equals("!debug")) {
			event.getChannel().sendMessage(savedUsers.getAllUsers().length + " Users saved").queue();
		}

		
		if (event.getMessage().getContentRaw().equals("!eHelp")) {
			event.getChannel().sendMessage("**Hallo " + event.getAuthor().getName() + "** :wave: \n"
					+ "Meine Befehle findest du hier:\n" + "**!eHelp**" + "	zeigt dir diesen Text :wink: \n"
					+ "**!game**" + "	 startet eine Runde E-Card\n" + "**!amk**"
					+ "		finde heraus welches BMW teil hier liegt\n" + "**!marco**" + "	who would have guessed ...")
					.queue();
		}

		if (event.getMessage().getContentRaw().equals("!marco")) {
			event.getChannel().sendMessage("polo!").queue();
		}
		
		if (event.getMessage().getContentRaw().equals("!points")) {
			event.getChannel().sendMessage(event.getAuthor().getName() + " hat " +
					savedUsers.getUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong())) + " Punkte").queue();
		}
		
		String upMsg = event.getMessage().getContentRaw().toUpperCase();
		if (!event.getAuthor().getName().equals("E-Card Bot")&& !event.getAuthor().getName().equals("Rythm") &&
			(upMsg.contains("HALLO") || upMsg.contains("HEY") || upMsg.contains("AHOI") || upMsg.contains("MOIN") ||
			upMsg.startsWith("EY") || upMsg.contains("WHATS UP") || upMsg.contains("OLLA") || upMsg.contains("MAHLZEIT") ||
			upMsg.contains("GUTEN") || upMsg.contains("TAG") || upMsg.contains("TACH") || upMsg.contains("BACK") ||
			upMsg.startsWith("HI") || upMsg.contains("MOIN") || upMsg.contains("KONICHIWA") ||
			upMsg.contains("SERVUS") || upMsg.startsWith("JO") || upMsg.contains("GEHT") || upMsg.contains("ABEND"))) {
			
			int randomResponse = (int) (Math.random() * 10);
			switch (randomResponse) {
				case 0:
					event.getChannel().sendMessage("Moin Moin").queue();
					break;
				case 1:
					event.getChannel().sendMessage("Servus!").queue();
					break;
				case 2:
					event.getChannel().sendMessage("salve").queue();
					break;
				case 3:
					event.getChannel().sendMessage("na du").queue();
					break;
				case 4:
					event.getChannel().sendMessage("huhu").queue();
					break;
				case 5:
					event.getChannel().sendMessage("Mahlzeit").queue();
					break;
				default:
					event.getChannel().sendMessage("Hallo " + event.getAuthor().getName() + "! (:").queue();
			}
		}
		
		givePoints(event);
		amkCommands(event);
		eCardCommands(event);
		lotto(event);
	}
	
	//commands---------------------------------------------------------------------------------------------------------------------------
	public void lotto(MessageReceivedEvent event) {
		if (event.getMessage().getContentRaw().equals("!banana")
				&& savedUsers.getUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong())) >= 1) {
			event.getChannel().sendMessage(
					"MEHR BANANAS = MEHR $$$\n" + "Eine Runde kostet einen Punkt\n" + "und wird mit !b gestartet.\n" +
					"Wenn mit !b EINSATZ mehr gesetzt wird, dann steigen deine Chancen")
					.queue();
		}
		
		if (event.getMessage().getContentRaw().equals("!b")
				&& savedUsers.getUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong())) >= 1) {
			bananaGame(event, 1);
		}
		
		if (event.getMessage().getContentRaw().startsWith("!b ")) {
			String s = event.getMessage().getContentRaw().substring(3);
			int einsatz = Integer.parseInt(s);
			if(savedUsers.getUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong())) >= einsatz) {
				bananaGame(event, einsatz);
			} else {
				
			}
		}

		if (event.getMessage().getContentRaw().equals("!banana")
				&& savedUsers.getUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong())) < 1) {
			event.getChannel().sendMessage("Du hast keine Punkte. \n" + ":shrug:").queue();
		}
	}
	
	public void bananaGame(MessageReceivedEvent event, int einsatz) {
		String bananaString = "";
		String wordBanana = "";
		String wordPoint = "";
		
		int bananas;
		int i = (int)(Math.random()*6);
		double i2 = i==1?(-20)*Math.log(Math.random()*4):
						   Math.random()*1.15;
		bananas = (int) (i2>0?i2:0);
		bananas *= einsatz;
		bananas += (int) einsatz/10;
		
		i = bananas;
		if(bananas <= 10000) {
			if(bananas == 0) {
				bananaString += ":stuck_out_tongue: ";
			}
			while(bananas >= 100) {
				bananas -= 100;
				bananaString += ":palm_tree: ";
			}
			while(bananas>0) {
				bananas--;
				bananaString += ":banana: ";
			}
		} else {
			bananaString = ":JP: ";
		}
		wordBanana = (i!=1)?"Bananen":"Banane";
		wordPoint = (einsatz!=1)?"Punkte":"Punkt";
		
		event.getChannel().sendMessage(bananaString).queue();
		event.getChannel().sendMessage(event.getAuthor().getName() + " hat " +
										einsatz + " " + wordPoint + " benutzt und\n" +
										i + " " + wordBanana + " gewonnen!").queue();
		
		savedUsers.addUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong()), -einsatz);
		savedUsers.addUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong()), i);
		
		event.getChannel().sendMessage(event.getAuthor().getName() + " hat jetzt "
				+ savedUsers.getUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong())) + " Punkte").queue();
	}
	
	public void givePoints(MessageReceivedEvent event) {
		if (event.getMessage().getContentRaw().startsWith("!ddd ")) {
			String s = event.getMessage().getContentRaw().substring(5);
			payPointsNum = Integer.parseInt(s);
			nextTextP = true;
		}
		
		if (event.getMessage().getContentRaw().startsWith("!givePoints ") && nextTextP) {
			nextTextP = false;
			
			String strId = "0";
			try {
				strId = event.getMessage().getContentRaw().substring(14);
				strId = strId.replaceAll("[^\\d.]", "");
				System.out.println(strId);
				savedUsers.addUserPoints(conv2UsrArrPos(Long.parseLong(strId)), payPointsNum);
			} catch(ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println(strId);
				event.getChannel().sendMessage("keine gueltige ID").queue();
			} catch(StringIndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println(strId);
				event.getChannel().sendMessage("keine gueltige ID").queue();
			}
			event.getChannel().sendMessage(strId + " hat " +
					savedUsers.getUserPoints(conv2UsrArrPos(Long.parseLong(strId))) + " Punkte").queue();
		}
		
		if (event.getMessage().getContentRaw().equals("!getPoints") && nextTextP) {
			nextTextP = false;
			savedUsers.addUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong()), payPointsNum);
			event.getChannel().sendMessage(event.getAuthor().getName() + " hat " +
					savedUsers.getUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong())) + " Punkte").queue();
		}
	}
	
	public void amkCommands(MessageReceivedEvent event) {
		if (event.getMessage().getContentRaw().equals("!amk")) {
			//event.getChannel().sendMessage("DAS IST DEIN KOLBEN").queue();
			event.getAuthor().openPrivateChannel().queue((channel) ->
	        {
	            channel.sendMessage("DAS IST DEIN KOLBEN").queue();
	        });
		}
		
		if (event.getMessage().getContentRaw().startsWith("!amk ") &&
				savedUsers.getUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong())) < 100) {
			event.getChannel().sendMessage("Du hast unter 100 Punkte. \n" +
					   					   ":shrug:").queue();
		}
		
		if (event.getMessage().getContentRaw().startsWith("!amk ") &&
			savedUsers.getUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong())) >= 100) {
			String strId = "0";
			try {
				strId = event.getMessage().getContentRaw().substring(7);
				strId = strId.replaceAll("[^\\d.]", "");
				
				if(isIDInList(Long.parseLong(strId))) {
					if(isUserDataInList(Long.parseLong(strId))) {
						User aUser = savedUsers.getAllUsers()[conv2UsrArrPos(Long.parseLong(strId))].getUserP();
						savedUsers.addUserPoints(conv2UsrArrPos(event.getAuthor().getIdLong()), -100);
						aUser.openPrivateChannel().queue((channel) -> {
							channel.sendMessage("DAS IST DEIN KOLBEN").queue();
						});
					} else {
						event.getChannel().sendMessage("Benutzerdaten unvollstaendig\n" +
								   "(moeglicherweise hat er noch nichts geschieben)").queue();
					}
				} else {
					event.getChannel().sendMessage("Benutzer nicht gespeichert \n" +
							   "(moeglicherweise hat er noch nichts geschieben)").queue();
				}
			} catch(StringIndexOutOfBoundsException e) {
				e.printStackTrace();
				event.getChannel().sendMessage("keine gueltige ID").queue();
			}
			System.out.println(strId);
		}
	}
	
	public void userListCommands(MessageReceivedEvent event) {
		if(moreThanOneUsr && !isIDInList(event.getAuthor().getIdLong())) {
			BotUsr[] longerUsrArr = new BotUsr[savedUsers.getAllUsers().length + 1];
			int i = 0;
			for(BotUsr user : savedUsers.getAllUsers()) {
				longerUsrArr[i] = user;
				i++;
			}
			
			BotUsr newUsr = new BotUsr(event.getAuthor());
			longerUsrArr[longerUsrArr.length-1] = newUsr;
			savedUsers.setAllUsers(longerUsrArr);
		}
		
		if(moreThanOneUsr && isIDInList(event.getAuthor().getIdLong()) && !isUserDataInList(event.getAuthor().getIdLong())) {
			int arrpos = conv2UsrArrPos(event.getAuthor().getIdLong());
			savedUsers.getAllUsers()[arrpos].setUserP(event.getAuthor());
		}
		
		if((savedUsers.getAllUsers().length == 1) && !moreThanOneUsr) {
			savedUsers.firstBotUsr(new BotUsr(event.getAuthor()));
			moreThanOneUsr = true;
		}
	}

	public void eCardCommands(MessageReceivedEvent event) {
		if(playingChannel == event.getChannel()){
			if((event.getMessage().getContentRaw().startsWith("!e ")) && (gameStarted) && (!repeat) && p2acc) {
				String s = event.getMessage().getContentRaw().substring(3);
				int playedcard = Integer.parseInt(s);
				gameStarted = !game.Game.playerUsesCards((playedcard - 1), event);
			}
			
			if((event.getMessage().getContentRaw().equals("!e")) && (gameStarted) && (!repeat) && !p2acc) {
				p2acc = true;
				game.Game.player2acc(event);
			}
			
			if((event.getMessage().getContentRaw().startsWith("!e ")) && (gameStarted) && (repeat)) {
				String s = event.getMessage().getContentRaw().substring(3);
				int menuPoint = Integer.parseInt(s);
				repeat = game.Game.startGameMenu(menuPoint, event);
			}
		}
		
		if(event.getMessage().getContentRaw().equals("!game")) {
			game.Game.startMenu(event);
			gameStarted = true;
			repeat = true;
			p2acc = false;
			playingChannel = event.getChannel();
		}
	}
	
	public boolean isNameInList(String name) {
		for(BotUsr oneuser:savedUsers.getAllUsers()){
			if((name==oneuser.getUserP().getName())){
				return true;
			}
		}
		return false; 
	}
	
	public boolean isUserDataInList(long id) {
		for(BotUsr oneuser:savedUsers.getAllUsers()){
			if(id==oneuser.getId()){
				if(oneuser.isUserset()) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false; 
	}
	
	public boolean isIDInList(long id) {
		for(BotUsr oneuser:savedUsers.getAllUsers()){
			if(id==oneuser.getId()){
				return true;
			}
		}
		return false; 
	}
	
	public static int conv2UsrArrPos(long usrID) {
		int i = 0;
		for(BotUsr oneuser:savedUsers.getAllUsers()){
			if(usrID==oneuser.getId()){
				return i;
			}
			i++;
		}
		return i;
	}
}
