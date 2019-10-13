package core;

public class BotUsrList {
	private BotUsr[] allUsers = new BotUsr[1];
	
	public BotUsrList() {
		allUsers = new BotUsr[1];
	}
	
	public BotUsrList(boolean searchNLoadList) {
		if(searchNLoadList) {
			FileMGR a = new FileMGR();
			allUsers = new BotUsrList(a.loadUsrList()).getAllUsers();
		} else {
			allUsers = new BotUsr[0];
		}
	}
	
	public BotUsrList(BotUsr[] newList) {
		this.allUsers = newList;
	}
	
	public BotUsrList(BotUsrList newList) {
		this.allUsers = newList.getAllUsers();
	}
	
	public BotUsr[] getAllUsers() {
		return this.allUsers;
	}
	
	public void firstBotUsr(BotUsr firstUser) {
		this.allUsers[0] = firstUser;
		this.toFile();
	}
	
	public void setAllUsers(BotUsrList newList) {
		 this.allUsers = newList.getAllUsers();
		 this.toFile();
	}
	
	public void setAllUsers(BotUsr[] newList) {
		 BotUsrList botUsrList = new BotUsrList(newList);
		 setAllUsers(botUsrList);
	}
	
	public int getUserPoints(int arrpos) {
		 return allUsers[arrpos].getPoints();
	}
	
	public void setUserPoints(int arrpos, int points) {
		 allUsers[arrpos].setPoints(points);
		 this.toFile();
	}
	
	public void addUserPoints(int arrpos, int points) {
		int currentPoints = getUserPoints(arrpos);
		setUserPoints(arrpos, (currentPoints+points));
	}
	
	public void toFile() {
		FileMGR a = new FileMGR();
		//a.writeJson(this);				//json file
		a.writeStr(this.allUsers);			//txt file
	}
}
