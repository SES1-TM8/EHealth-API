package xyz.jonmclean.EHealth.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long patientId;
	
	@Column
	public String concessionType = "NONE";
	
	@Column(nullable = true, unique = true)
	public String concessionNumber = null;
	
	@Column(nullable = false, unique = true)
	public String medicareNumber;
	
	@Column(nullable = false)
	public long userId;

	public Patient() {}
	
	public Patient(String concessionType, String medicareNumber, long userId) {
		this.concessionType = concessionType;
		this.medicareNumber = medicareNumber;
		this.userId = userId;
	}

	public Patient(String concessionType, String concessionNumber, String medicareNumber, long userId) {
		this.concessionType = concessionType;
		this.concessionNumber = concessionNumber;
		this.medicareNumber = medicareNumber;
		this.userId = userId;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public String getConcessionType() {
		return concessionType;
	}

	public void setConcessionType(String concessionType) {
		this.concessionType = concessionType;
	}

	public String getConcessionNumber() {
		return concessionNumber;
	}

	public void setConcessionNumber(String concessionNumber) {
		this.concessionNumber = concessionNumber;
	}

	public String getMedicareNumber() {
		return medicareNumber;
	}

	public void setMedicareNumber(String medicareNumber) {
		this.medicareNumber = medicareNumber;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
