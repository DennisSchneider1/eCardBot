package core;

import net.dv8tion.jda.core.entities.User;

public class BotUsr {

	private User userP;
	private boolean userset;
	private long id; 
	private int points;
	
	public BotUsr() {
		userset = false;
		setPoints(10);
	}
	
	public BotUsr(User user) {
		userset = true;
		setUserP(user);
		setId(user.getIdLong());
		setPoints(10);
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public User getUserP() {
		return userP;
	}

	public void setUserP(User userP) {
		userset = true;
		this.userP = userP;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isUserset() {
		return userset;
	}

	public void setUserset(boolean userset) {
		this.userset = userset;
	}

}
