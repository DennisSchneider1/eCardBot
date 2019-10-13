package core;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import net.dv8tion.jda.core.entities.User;
*/

public class FileMGR {

	public FileMGR() {
	}
	
	public void writeStr(String s){
		FileWriter writer;
		try {
			writer = new FileWriter("UserListe.txt");
			
			writer.write(s, 0, s.length());
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("fehler");
		}
	}
	
	public void writeStr(BotUsr[] usrList){
		String s = "";
		for(BotUsr oneusr : usrList) {
			s += oneusr.getId();
			s += ":";
			s += oneusr.getPoints() + ";";
		}
		writeStr(s);
	}
	
	public String readTxt(){
		FileReader reader;
		int x = 0;
		String ergebnis = "";
		try {
			reader = new FileReader("UserListe.txt");
			do{
				x = reader.read();
				char c = (char) x;
				ergebnis = ergebnis + c;
			}while(x != -1);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("fehler");
		}
		return ergebnis;
	}
	
	//load on start
	public BotUsr[] loadUsrList() {
		String usrDataStr = readTxt();
		BotUsr[] retList = new BotUsr[0];
		String[] udArr = usrDataStr.split(";");
		udArr[udArr.length-1]=";";
		
		for(String oneUsr : udArr){
			if(!oneUsr.equals(";")) {
				boolean namePart = true;
				BotUsr thisusr = new BotUsr();
				for(String oneUsrData : oneUsr.split(":")){
					if(namePart) {
						thisusr.setId(Long.parseLong(oneUsrData));
					} else {
						thisusr.setPoints(Integer.parseInt(oneUsrData));
						retList = addtolist(retList, thisusr);
					}
					namePart = false;
				}
			}
		}
		
		return retList;
	}
	
	public BotUsr[] addtolist(BotUsr[] oldArr, BotUsr thisusr) {
		BotUsr[] longerUsrArr = new BotUsr[oldArr.length + 1];
		int i = 0;
		for(BotUsr user : oldArr) {
			longerUsrArr[i] = user;
			i++;
		}
		
		longerUsrArr[longerUsrArr.length-1] = thisusr;
		return longerUsrArr;
	}
	/*
	public void writeJson(BotUsrList savedUsers) {
		JSONObject obj = new JSONObject();
		JSONArray usrlist = new JSONArray();
		
		for(BotUsr aUsr : savedUsers.getAllUsers()) {
			JSONArray usrdata = new JSONArray();
			usrdata.put(aUsr.getUserP().getIdLong());
			usrdata.put(aUsr.getPoints());
			usrlist.put(usrdata);
		}
		
		obj.put("users", usrlist);
		writeJson(obj.toString());
	}
	
	public void writeJson(String s){
		FileWriter writer;
		try {
			writer = new FileWriter("UserListe.json");
			
			writer.write(s);
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("fehler");
		}
	}
	*/
}
