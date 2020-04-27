package xyz.jonmclean.EHealth.test.people;

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
	public long personId;
	
	// name, date of birth, favourite colour
	@Column
	public String name;
	
	@Column
	public Date dob;
	
	@Column
	public String favouriteColour;
	
	public People() {}
	
	public People(String name, Date dob, String colour) {
		this.name = name;
		this.dob = dob;
		this.favouriteColour = colour;
	}

	public long getPersonId() {
		return personId;
	}

	public void setPersonId(long personId) {
		this.personId = personId;
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
