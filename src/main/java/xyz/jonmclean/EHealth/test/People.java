package xyz.jonmclean.EHealth.test;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class People {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long peopleId;
	
	@Column
	public String name;
	
	@Column
	public Date dob;
	
	@Column
	public String favouriteColour;
	
	public People() {}
	
	public People(String name, Date dob, String favouriteColour) {
		this.name = name;
		this.dob = dob;
		this.favouriteColour = favouriteColour;
	}

	public long getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(long peopleId) {
		this.peopleId = peopleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getFavouriteColour() {
		return favouriteColour;
	}

	public void setFavouriteColour(String favouriteColour) {
		this.favouriteColour = favouriteColour;
	}
}
