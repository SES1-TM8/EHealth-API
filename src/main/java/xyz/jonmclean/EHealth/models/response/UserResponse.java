package xyz.jonmclean.EHealth.models.response;

import java.sql.Date;

import xyz.jonmclean.EHealth.models.User;

public class UserResponse {
	
	public long userId;
	public String firstName;
	public String lastName;
	public Date dob;
	public String emailAddress;
	public String phoneNumber;
	
	public UserResponse() {}
	
	public UserResponse(long userId, String firstName, String lastName, Date dob, String emailAddress,
			String phoneNumber) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
	}
	
	public UserResponse(User user) {
		this.userId = user.getUserId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.dob = user.getDob();
		this.emailAddress = user.getEmailAddress();
		this.phoneNumber = user.getPhoneNumber();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
}
