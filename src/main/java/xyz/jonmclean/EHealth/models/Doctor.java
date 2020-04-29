package xyz.jonmclean.EHealth.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Doctor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long doctorId;
	
	@Column(unique = true)
	public String registraitonNumber;
	
	@Column(nullable = false)
	public long userId;
	
	@Column
	public boolean verified = false;
	
	public Doctor() {}
	
	public Doctor(String registrationNumber, long userId) {
		this.registraitonNumber = registrationNumber;
		this.userId = userId;
		this.verified = false;
	}

	public long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(long doctorId) {
		this.doctorId = doctorId;
	}

	public String getRegistraitonNumber() {
		return registraitonNumber;
	}

	public void setRegistraitonNumber(String registraitonNumber) {
		this.registraitonNumber = registraitonNumber;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
}
