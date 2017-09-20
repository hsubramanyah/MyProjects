

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String firstName;
	private String lastName;
	private String emailId;
	private String password;
	private String userId;
	private ArrayList<String> seats = new ArrayList<String>();

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String emailId, String firstName, String lastName, String userId, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.password = password;
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public ArrayList<String> getSeats() {
		return seats;
	}

	public void setSeats(ArrayList<String> seats) {
		this.seats = seats;
	}
	
}
